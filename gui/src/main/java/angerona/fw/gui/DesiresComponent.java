package angerona.fw.gui;

import java.util.LinkedList;
import java.util.List;

import net.sf.tweety.Formula;
import angerona.fw.logic.Desires;
import angerona.fw.report.Entity;

public class DesiresComponent extends ListViewColored<Desires> {

	/** kill warning */
	private static final long serialVersionUID = 8925750149764894623L;

	
	@Override
	public String getComponentTypeName() {
		return "Desires";
	}
	
	@Override
	public Class<?> getObservationObjectType() {
		return Desires.class;
	}

	@Override
	public void setObservationObject(Object obj) {
		if(! (obj instanceof Desires))
			throw new IllegalArgumentException("The observation Object must be of type '" + Desires.class.getSimpleName() + "'");
		
		ref = (Desires) obj;
		actual = ref;
	}

	@Override
	protected List<String> getStringRepresentation(Entity obj) {
		if(obj instanceof Desires) {
			Desires des = (Desires)obj;
			List<String> reval = new LinkedList<String>();
			for(Formula f : des) {
				reval.add(f.toString());
			}
			
			return reval;
		}
		return null;
	}

}
