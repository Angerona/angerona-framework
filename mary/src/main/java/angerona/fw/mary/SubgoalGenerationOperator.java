package angerona.fw.mary;
import javax.swing.JOptionPane;

import angerona.fw.operators.parameter.SubgoalGenerationParameter;


public class SubgoalGenerationOperator extends
		angerona.fw.operators.def.SubgoalGenerationOperator {
	@Override
	protected Boolean processInt(SubgoalGenerationParameter pp) {
		JOptionPane.showMessageDialog(null, "It works");
		return super.processInt(pp);
	}
}
