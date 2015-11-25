package com.github.kreaturesfw.knowhow;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.kreaturesfw.core.BaseAgentComponent;
import com.github.kreaturesfw.knowhow.parser.KnowhowParser;
import com.github.kreaturesfw.knowhow.parser.ParseException;

/**
 * A KnowhowBase is an AgentComponent adding the concept of know-how to an
 * Angerona agent. The concept of know-how was defined by Thimm, Krümpelmann
 * 2009.
 * 
 * It extends the {@link BaseAgentComponent} and therefore implements a copy-
 * constructor and the clone() method.
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
}
