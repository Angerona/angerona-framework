package angerona.fw.knowhow;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import net.sf.tweety.logicprogramming.asplibrary.syntax.Atom;
import net.sf.tweety.logicprogramming.asplibrary.syntax.Program;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import angerona.fw.AgentComponent;

/**
 * Extends a generic ASP Beliefbase with know-how.
 * @author Tim Janus
 */
public class KnowhowBase extends AgentComponent {
	
	/** reference to the logback logger instance */
	private Logger LOG = LoggerFactory.getLogger(KnowhowBase.class);
	
	private Program nextAction;
	
	private Program initTree;
	
	private List<KnowHowStatement> statements = new LinkedList<KnowHowStatement>();
	
	public KnowhowBase() {
		super();
		InputStream is = this.getClass().getClassLoader().getResourceAsStream("programs/InitTree");
		initTree = Program.loadFrom(new InputStreamReader(is));
		
		is = this.getClass().getClassLoader().getResourceAsStream("programs/NextAction");
		nextAction = Program.loadFrom(new InputStreamReader(is));
	}
	
	public KnowhowBase(KnowhowBase other) {
		super(other);
		nextAction = (Program)other.nextAction.clone();
		initTree = (Program)other.initTree.clone();
		this.statements = new LinkedList<KnowHowStatement>(other.statements);
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
			String [] comps = line.split(";");
			if(comps.length != 3) {
				LOG.warn("Cannot parse know-statement: '{}'", line);
				continue;
			}
			
			LOG.info("Parse know-statement: '{}'", line);
			
			Atom target = new Atom(comps[0]+".");
			Vector<Atom> subtargets = new Vector<Atom>();
			Vector<Atom> conditions = new Vector<Atom>();
			subtargets.add(new Atom(comps[1]+"."));
			if(!comps[2].trim().isEmpty())
				conditions.add(new Atom(comps[2]+"."));
			
			statements.add(new KnowHowStatement(target, subtargets, conditions));
		}
	}
	
	/*
	private KnowHowStatement parse(String line) {
		Atom target = null;
		Vector<Atom> subtargets = new Vector<Atom>();
		Vector<Atom> conditions = new Vector<Atom>();
		
		int pos = 0;
		int state = 0;
		line = line.trim();
		while(pos < line.length()) {
			if(state == 0) {
				int i = line.indexOf(";");
				if(i == -1) {
					state = -1;
				} else {
					target = new Atom(line.substring(pos, i-1));
					pos = i;
					++state;
				}
			} else if(state == 1) {
				int ic = line.indexOf(";", pos);
				int ip = line.indexOf("(", pos);
				
				if(ic == -1 && ip == -1) {
					state = -1;
					continue;
				} else if(ic < ip) {
					subtargets.add(new Atom(line.substring(pos, ic-1)));
				} else {
					int ei = line.indexOf(")", pos);
					String [] sts = line.substring(ip, ei).split(",");
					for(String sub : sts) {
						subtargets.add(new Atom(sub));
					}
				}
				pos = ic;
				++state;
			} else if (state == 2) {
				int ic = line.indexOf(",", pos);
				int ip = line.indexOf("(", pos);
				
				if(ic == -1 && ip == -1) {
					state = -1;
					continue;
				} else if(ic < ip) {
					conditions.add(new Atom(line.substring(pos, ic-1)));
				} else {
					int ei = line.indexOf(")", pos);
					String [] sts = line.substring(ip, ei).split(",");
					for(String sub : sts) {
						conditions.add(new Atom(sub));
					}
				}
				pos = ic;
				++state;
			} else {
				LOG.warn("Cannot parse know-statement: '{}'", line);
				return null;
			}
			
		}
		
		return new KnowHowStatement(target, subtargets, conditions);
	}
	*/
	
	@Override
	public String toString() {
		return statements.toString();
	}
	
	@Override
	public Object clone() {
		return new KnowhowBase(this);
	}
}
