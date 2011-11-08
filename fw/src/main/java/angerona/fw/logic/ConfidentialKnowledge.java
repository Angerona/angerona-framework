package angerona.fw.logic;
import java.io.BufferedReader;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

import net.sf.tweety.Formula;
import net.sf.tweety.ParserException;
import net.sf.tweety.logics.firstorderlogic.parser.FolParser;
import net.sf.tweety.logics.firstorderlogic.syntax.FolSignature;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import angerona.fw.logic.base.BaseBeliefbase;
/**
 * Beliefbase containing a set of personal confidential targets.
 * @author Tim Janus
 */
public class ConfidentialKnowledge extends BaseBeliefbase {

	/** reference to the logback instance used for logging */
	private static Logger LOG = LoggerFactory.getLogger(ConfidentialKnowledge.class);
	
	/** set of confidential targets defining this beliefbase */
	private Set<ConfidentialTarget> confidentialTargets = new HashSet<ConfidentialTarget>();
	
	public ConfidentialKnowledge() {}
	
	private FolSignature signature;
	
	private ConfidentialKnowledge(ConfidentialKnowledge other) {
		for(ConfidentialTarget ct : other.confidentialTargets) {
			this.confidentialTargets.add((ConfidentialTarget)ct.clone());
		}
	}
	
	@Override
	public Object clone() {
		return new ConfidentialKnowledge(this);
	}

	@Override
	public FolSignature getSignature() {
		return signature;
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

	@Override
	protected void parseInt(BufferedReader br) throws IOException, ParserException {
		// 0 = nothing
		// 1 = agent name
		// 2 = predicate (use tweety)
		// 3 = set
		
		String content = "";
		while(br.ready())
			content += br.readLine();
		int state = 0;
		
		String name = "";
		Formula info = null;
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
				int newIndex = content.indexOf(',', i);
				String formula = content.substring(i, newIndex);
				try {
					info = fp.parseFormula(formula);
				} catch (ParserException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				i = newIndex;
				state = 3;
				break;
				
			case 3:
				newIndex = content.indexOf(')', i);
				String set = content.substring(i, newIndex);
				set = set.substring(set.indexOf('{')+1, set.indexOf('}'));
				String [] ary = set.split(",");
				Set<AnswerValue> answers = new HashSet<AnswerValue>();
				for(String s : ary) {
					AnswerValue av = AngeronaAnswer.valueOf(s);
					if(av != null)
						answers.add(av);
				}
				
				i = newIndex;
				
				if(info != null)
					addConfidentialTarget(new ConfidentialTarget(name, info, answers));
				else
					LOG.error("Cant read confidential targets formula!");
				name = "";
				info = null;
				state = 0;
			}
		}
	}

	@Override
	public String getFileEnding() {
		return "conf";
	}
}
