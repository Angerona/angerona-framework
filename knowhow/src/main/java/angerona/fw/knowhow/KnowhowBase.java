package angerona.fw.knowhow;

import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Vector;

import net.sf.tweety.logicprogramming.asplibrary.solver.DLVComplex;
import net.sf.tweety.logicprogramming.asplibrary.solver.SolverException;
import net.sf.tweety.logicprogramming.asplibrary.syntax.Atom;
import net.sf.tweety.logicprogramming.asplibrary.syntax.Constant;
import net.sf.tweety.logicprogramming.asplibrary.syntax.Literal;
import net.sf.tweety.logicprogramming.asplibrary.syntax.Program;
import net.sf.tweety.logicprogramming.asplibrary.syntax.Rule;
import net.sf.tweety.logicprogramming.asplibrary.syntax.Variable;
import net.sf.tweety.logicprogramming.asplibrary.util.AnswerSet;
import net.sf.tweety.logicprogramming.asplibrary.util.AnswerSetList;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import angerona.fw.BaseAgentComponent;
import angerona.fw.knowhow.parser.KnowhowParser;
import angerona.fw.knowhow.parser.ParseException;

/**
 * A KnowhowBase is an AgentComponent adding the concept of knowhow to an Angerona agent.
 * The concept of Knowhow was defined by Thimm, Krümpelmann 2009.
 * @author Tim Janus
 */
public class KnowhowBase extends BaseAgentComponent {
	
	/** reference to the logback logger instance */
	static private Logger LOG = LoggerFactory.getLogger(KnowhowBase.class);
	
	/** the program responsible to calculate the next action, nextAction4 of Regina Fritsch was used as basic */
	private Program nextAction;
	
	/** the program responsible for initialization of the intention tree */
	private Program initTree;
	
	/** the KnowhowStatements which define this KnowhowBase */
	private List<KnowhowStatement> statements = new LinkedList<KnowhowStatement>();
	
	public KnowhowBase() {
		super();
		InputStream is = this.getClass().getClassLoader().getResourceAsStream("programs/InitTree");
		initTree = Program.loadFrom(new InputStreamReader(is));
		
		is = this.getClass().getClassLoader().getResourceAsStream("programs/NextActionLists");
		if(is != null)
			nextAction = Program.loadFrom(new InputStreamReader(is));
	}
	
	public KnowhowBase(KnowhowBase other) {
		super(other);
		nextAction = (Program)other.nextAction.clone();
		initTree = (Program)other.initTree.clone();
		this.statements = new LinkedList<KnowhowStatement>(other.statements);
	}

	@Override
	public void init(Map<String, String> data) {
		if(!data.containsKey("KnowHow")) {
			LOG.warn("Knowhow Base of agent '{}' has no initial data.", getAgent().getName());
			return;
		}
		
		String value = data.get("KnowHow");
		String [] lines = value.split("\n");
		
		for(String line : lines) {
			LOG.info("Parse know-statement: '{}'", line);
			if(line.trim().isEmpty())
				continue;
			
			KnowhowParser parser = new KnowhowParser(line);
			try {
				statements.add(parser.statement());
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	/** @return unmodifiable list of all KnowhowStatements saved in this KnowhowBase */
	public List<KnowhowStatement> getStatements() {
		return Collections.unmodifiableList(statements);
	}
	
	@Override
	public String toString() {
		return statements.toString();
	}
	
	@Override
	public Object clone() {
		return new KnowhowBase(this);
	}
	
	public static void main(String [] args) throws SolverException {
		LOG.info("Programm arguments: '{}'", args);
		
		if(args.length == 0)
			throw new IllegalArgumentException("Argument dlv-complex path must be given at last.");
		
		File f = new File(args[0]);
		if(!f.exists() || !f.isFile()) {
			throw new IllegalArgumentException("DLV-Complex binary with path: " + f.getAbsolutePath() + " not found.");
		}
		
		KnowhowBase kb = new KnowhowBase();
		Map<String, String> data = new HashMap<String, String>();
		/*data.put("KnowHow", 
				"cleaned_all, (cleaned_hallway, cleaned_lounge), battery_full\n" +
				"cleaned_hallway, (at_hallway, vacuumed_hallway), bag_empty\n" +
				"cleaned_lounge, ordered_robotxy_to_clean_lounge, robotxy_available\n" +
				"cleaned_lounge, (at_lounge, free_lounge, vacuumed_lounge), bag_empty\n" +
				"free_lounge, people_sent_away, at_lounge"
				);*/
		data.put("KnowHow", "win, (bluff), ");
		kb.init(data);
		KnowhowStatement stmt = new KnowhowStatement(new Atom("bluff"), new Vector<Atom>(), new Vector<Atom>());
		kb.statements.add(stmt);
		
		Program p = KnowhowBuilder.buildExtendedLogicProgram(kb);
		Rule is_atomic = new Rule();
		is_atomic.addHead(new Atom("is_atomic", new Constant("bluff")));
		p.add(is_atomic);
		p.add(kb.initTree);
		p.add(kb.nextAction);
		System.out.println(p.toString());
		System.out.println();
		
		int maxIt = 10;
		boolean run = true;
		AnswerSetList asl = null;
		while(run) {
			run = false;
			DLVComplex dlvComplex = new DLVComplex(args[0]);
			asl = dlvComplex.computeModels(p, 10);
			LOG.info(asl.toString());
			
			Atom toSearchVariable = new Atom("act", new Variable("A"));
			Set<Literal> variable = new HashSet<Literal>();
			variable.add(toSearchVariable);
			
			Atom toSearchIdentity = new Atom("act", new Constant("bluff"));
			Set<Literal> constant = new HashSet<Literal>();
			constant.add(toSearchIdentity);
			
			variable.add(toSearchVariable);
			for(int i=0; i<asl.size(); ++i) {
				AnswerSet as = asl.get(i);
				if(as.containsAll(variable)) {
					run = true;
					break;
				}
				
				if(as.containsAll(constant)) {
					run = true;
					System.out.println("Only constant worked");
					break;
				}
			}
			
			maxIt--;
			if(maxIt <= 0)
				run = false;
			
			// update program:
			p = KnowhowBuilder.buildExtendedLogicProgram(kb);
			p.add(is_atomic);
			p.add(kb.nextAction);
			
		}
	}
}
