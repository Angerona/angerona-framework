package com.github.angerona.knowhow;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import net.sf.tweety.logicprogramming.asplibrary.solver.SolverException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.angerona.fw.BaseAgentComponent;
import com.github.angerona.knowhow.asp.KnowhowASPStrategy;
import com.github.angerona.knowhow.parser.KnowhowParser;
import com.github.angerona.knowhow.parser.ParseException;

/**
 * A KnowhowBase is an AgentComponent adding the concept of knowhow to an
 * Angerona agent. The concept of Knowhow was defined by Thimm, Krümpelmann
 * 2009.
 * 
 * @author Tim Janus
 */
public class KnowhowBase extends BaseAgentComponent {

	/** reference to the logback logger instance */
	static private Logger LOG = LoggerFactory.getLogger(KnowhowBase.class);

	/** the KnowhowStatements which define this KnowhowBase */
	private List<KnowhowStatement> statements = new LinkedList<KnowhowStatement>();

	/** Default Ctor: Generates the NextAction program */
	public KnowhowBase() {
		super();
	}

	/**
	 * Copy-Ctor copies the knowhow base.
	 * 
	 * @param other
	 *            reference to the other knowhow base.
	 */
	public KnowhowBase(KnowhowBase other) {
		super(other);
		this.statements = new LinkedList<KnowhowStatement>(other.statements);
	}

	/**
	 * parses the Knowhow Data defined in the agent section of the
	 * simulation.xml
	 */
	@Override
	public void init(Map<String, String> data) {
		if (!data.containsKey("KnowHow")) {
			LOG.warn("Knowhow Base of agent '{}' has no initial data.",
					getAgent().getName());
			return;
		}

		String value = data.get("KnowHow");
		String[] lines = value.split("\n");

		for (String line : lines) {
			LOG.info("Parse know-statement: '{}'", line);
			if (line.trim().isEmpty())
				continue;

			KnowhowParser parser = new KnowhowParser(line);
			try {
				statements.add(parser.statement());
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		if(statements.size() != 0)
			firePropertyChangeListener("statements", new ArrayList<>(), statements);
	}
	
	public void addStatement(KnowhowStatement statement) {
		this.statements.add(statement);
		firePropertyChangeListener("statements", statements, statements);
	}
	
	/**
	 * @return unmodifiable list of all KnowhowStatements saved in this
	 *         KnowhowBase
	 */
	public List<KnowhowStatement> getStatements() {
		return Collections.unmodifiableList(statements);
	}

	@Override
	public String toString() {
		return statements.toString();
	}

	@Override
	public KnowhowBase clone() {
		return new KnowhowBase(this);
	}

	/**
	 * A functional test for the Knowhow program iterations... tests the
	 * KnowhowStrategy.
	 * 
	 * @param args
	 *            Assumes that the first parameter is the path to the
	 *            dlv-complex solver
	 * @throws SolverException
	 */
	public static void main(String[] args) throws SolverException {
		LOG.info("Programm arguments: '{}'", args);

		if (args.length == 0)
			throw new IllegalArgumentException(
					"Argument dlv-complex path must be given at last.");

		File f = new File(args[0]);
		if (!f.exists() || !f.isFile()) {
			throw new IllegalArgumentException("DLV-Complex binary with path: "
					+ f.getAbsolutePath() + " not found.");
		}

		KnowhowBase kb = new KnowhowBase();
		Map<String, String> data = new HashMap<String, String>();

		data.put("KnowHow", "win, bluff, \nbluff, s_smile, \nbluff, s_laugth, ");
		// data.put("KnowHow", "win, bluff, \nbluff, (s_smile, s_laugth), ");
		kb.init(data);

		KnowhowASPStrategy ks = new KnowhowASPStrategy(args[0]);
		Set<String> actions = new HashSet<>();
		actions.add("smile");
		actions.add("laugth");
		Set<String> world = new HashSet<String>();
		world.add("i_play_poker");
		ks.init(kb, "win", actions, world);
		for (int i = 0; i < 10; i++) {
			int reval = ks.performStep();
			if (reval == -1) {
				LOG.info("NO Plan");
			} else if (reval == 1) {
				LOG.info("Action: " + ks.getAction());
				break;
			}
		}
	}
}
