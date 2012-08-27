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

import net.sf.tweety.logicprogramming.asplibrary.solver.SolverException;
import net.sf.tweety.logicprogramming.asplibrary.syntax.Program;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import angerona.fw.BaseAgentComponent;
import angerona.fw.knowhow.parser.KnowhowParser;
import angerona.fw.knowhow.parser.ParseException;
import angerona.fw.util.Pair;

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
	
	/**
	 * Copy-Ctor copies the knowhow base.
	 * @param other	reference to the other knowhow base.
	 */
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
	
	public Program getNextActionProgram() {
		return nextAction;
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
		data.put("KnowHow", "win, bluff, \nbluff, smile, ");
		kb.init(data);
		
		KnowhowStrategy ks = new KnowhowStrategy(args[0]);
		Set<Pair<String, Integer>> actions = new HashSet<>();
		actions.add(new Pair<>("smile", 0));
		Set<String> world = new HashSet<String>();
		world.add("i_play_poker");
		ks.init(kb, "win", actions, world);
		for(int i=0; i<10; i++) {
			int reval = ks.performStep();
			if(reval == -1) {
				LOG.info("NO Plan");
			} else if(reval == 1) {
				LOG.info("Action: " + ks.getActions());
				break;
			}
		}
		
		/*
		Program p = KnowhowBuilder.buildKnowhowBaseProgram(kb, false);
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
			p = KnowhowBuilder.buildKnowhowBaseProgram(kb, false);
			p.add(is_atomic);
			p.add(kb.nextAction);
			p.add(newInit);
		}
		*/
	}
}
