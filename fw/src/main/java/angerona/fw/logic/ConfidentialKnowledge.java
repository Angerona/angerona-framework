package angerona.fw.logic;
import java.io.IOException;
import java.util.Collections;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import net.sf.tweety.Formula;
import net.sf.tweety.ParserException;
import net.sf.tweety.logics.firstorderlogic.parser.FolParser;
import net.sf.tweety.logics.firstorderlogic.syntax.FolFormula;
import net.sf.tweety.logics.firstorderlogic.syntax.FolSignature;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import angerona.fw.BaseAgentComponent;
/**
 * Data-Component of an agent containing a set of personal confidential targets.
 * @author Tim Janus
 */
public class ConfidentialKnowledge extends BaseAgentComponent {

	/** reference to the logback instance used for logging */
	private static Logger LOG = LoggerFactory.getLogger(ConfidentialKnowledge.class);
	
	/** set of confidential targets defining this beliefbase */
	private Set<ConfidentialTarget> confidentialTargets = new HashSet<ConfidentialTarget>();
	
	
	private FolSignature signature = new FolSignature();
	
	public ConfidentialKnowledge() {
		super();
	}
	
	public ConfidentialKnowledge(ConfidentialKnowledge other) {
		for(ConfidentialTarget ct : other.confidentialTargets) {
			this.confidentialTargets.add((ConfidentialTarget)ct.clone());
		}
	}
	
	@Override
	public Object clone() {
		return new ConfidentialKnowledge(this);
	}

	public void setSignature(FolSignature signature) {
		this.signature = signature;
	}
	
	@Override
	public String toString() {
		String reval = "";
		for(ConfidentialTarget ct : confidentialTargets)
			reval += ct.toString() + "\n";
		return reval;
	}
	
	/**
	 * adds confidential target to the beliefbase.
	 * @param cf	the new confidential target which will be added to the beliefbase.
	 * @return		true if the beliefbase didn't contain the confidential target, false otherwise.
	 */
	public boolean addConfidentialTarget(ConfidentialTarget cf) {
		return confidentialTargets.add(cf);
	}
	public boolean removeConfidentialTarget(ConfidentialTarget cf)
	{
		return confidentialTargets.remove(cf);
	}
	/**
	 * Gets the confidential target defined by the subject and the information which is confidential.
	 * @param subjectName	name of the agent who should not get the confidential information
	 * @param information	the confidential information itself.
	 * @return				A confidential target if it exists, null otherwise.
	 */
	public ConfidentialTarget getTarget(String subjectName, Formula information) {
		for(ConfidentialTarget ct : confidentialTargets)
			if(ct.getSubjectName().compareTo(subjectName) == 0 && ct.getInformation().equals(information))
				return ct;
		return null;
	}
	
	public Set<ConfidentialTarget> getTargets() {
		return Collections.unmodifiableSet(confidentialTargets);
	}

	@Override
	public void init(Map<String, String> additionalData) {
		if(!additionalData.containsKey("Confidential")) {
			LOG.warn("Confidential Knowledge of agent '{}' has no initial data.", getAgent().getName());
			return;
		} else {
			this.signature.fromSignature(getAgent().getBeliefs().getWorldKnowledge().getSignature());
			try {
				parseInt(additionalData.get("Confidential"));
			} catch (ParserException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	
	/** internal helper for parsing confidential data */
	private void parseInt(String content) throws IOException, ParserException {
		// 0 = nothing
		// 1 = agent name
		// 2 = predicate (use tweety)
		// 3 = set
		
		int state = 0;
		
		String name = "";
		FolFormula info = null;
		for(int i=0; i<content.length(); ++i) {
			switch(state) {
			case 0:
				if(content.charAt(i) == '(')
					state = 1;
				break;
			
			case 1:
				if(content.charAt(i) == ',') {
					state = 2;
				}
				else
					name += content.charAt(i);
				break;
				
			case 2:
				FolParser fp = new FolParser();
				fp.setSignature(signature);
				int newIndex = content.indexOf(')', i);
				String formula = content.substring(i, newIndex);
				LOG.info("Try to parse FolFormula: '{}'", formula);
				try {
					info = (FolFormula)fp.parseFormula(formula);
				} catch (ParserException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				i = newIndex;
				state = 3;
			
				if(info != null)
					addConfidentialTarget(new ConfidentialTarget(name, info));
				else
					LOG.error("Cant read confidential targets formula!");
				name = "";
				info = null;
				state = 0;
				
				break;
			}
		}
	}
}
