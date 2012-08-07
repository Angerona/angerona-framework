package angerona.fw.knowhow;

import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Collections;
import java.util.HashMap;
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
import net.sf.tweety.logicprogramming.asplibrary.syntax.Term;
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
		String state_str = "intentionAdded";
		while(run) {
			DLVComplex dlvComplex = new DLVComplex(args[0]);
			asl = dlvComplex.computeModels(p, 10);
			
			maxIt--;
			if(maxIt <= 0)
				run = false;
			
			// proof if we have an atomar action:
			Set<Literal> act = asl.getFactsByName("new_act");
			if(act.size()!=0) {
				LOG.info("Next action: " + act.iterator().next().toString());
				break;
			}
			
			// create new intention tree:
			Program newInit = new Program();
			Atom new_state = temp(asl, "state");
			if(new_state == null)
				newInit.add((Atom)asl.getFactsByName("state").iterator().next());
			else
				newInit.add(new_state);
			
			Atom new_khstate = temp(asl, "khstate");
			if(new_khstate == null)
				newInit.add((Atom)asl.getFactsByName("khstate").iterator().next());
			else
				newInit.add(new_khstate);
			
			if(	state_str.equals("actionPerformed") ||
				state_str.equals("khAdded")	) {
				Atom new_istack = temp(asl, "istack");
				if(new_istack == null)
					break;
				newInit.add(new_istack);
			} else {
				Literal old_istack = asl.getFactsByName("istack").iterator().next();
				newInit.add((Atom)old_istack);
			}
			
			// update state:
			if(new_state != null)
				state_str = new_state.getTermStr(0);
			LOG.info(newInit.toString());
			
			// update program:
			p = KnowhowBuilder.buildExtendedLogicProgram(kb);
			p.add(is_atomic);
			p.add(kb.nextAction);
			p.add(newInit);
		}
	}

	private static Atom temp(AnswerSetList asl, String postfix) {
		// get new state...
		String error = null;
		Set<Literal> lits = asl.getFactsByName("new_" + postfix);
		if(lits.size() == 1) {
			Literal new_literal = lits.iterator().next();
			if(new_literal instanceof Atom) {
				Atom a = (Atom)new_literal;
				if(a.getArity() == 1) {
					Term t = a.getTerm(0);
					Atom state = new Atom(postfix, t);
					return state;
				} else {
					error = "The arity must be 1. But arity of '" + a.getName() + "' is: " + a.getArity();
				}
			} else {
				error = "'" + new_literal.toString() + "' is no atom. new_ literals must be facts.";
			}
		} else if(lits.size() == 0) {
			error = "no literal 'new_"+postfix+"' found in answerset.";
		} else {
			error = "There are more than one 'new_"+postfix+"' literals.";
		}
		LOG.error(error);
		return null;
	}
}
