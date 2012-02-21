package angerona.fw.gui.dummy;
import angerona.fw.gui.BeliefbaseComponent;
import angerona.fw.logic.dummy.DummyBeliefbase;

/**
 * Small "Enhancement" to the beliefbase component view, setting the title "DUMMY" ;-)
 * This class helps to test the functionality of the dynamic creation of belief base tabs and so on.
 * 
 * @author Tim Janus
 */
public class DummyBeliefbaseComponent extends BeliefbaseComponent {
	/** kill warning */
	private static final long serialVersionUID = 8044649633781759407L;

	@Override
	public void init() {
		super.init();
		setTitle("Dummy");
	}
	
	@Override
	public Class<?> getObservationObjectType() {
		return DummyBeliefbase.class;
	}
	
	@Override
	public void setObservationObject(Object obj) {
		if(! (obj instanceof DummyBeliefbase)) {
			throw new IllegalArgumentException("Observation Object must be of type '" +  DummyBeliefbase.class.getSimpleName() + "'");
		}
		this.ref = (DummyBeliefbase)obj;
		this.actual = this.ref;
		setTitle("DUMMY");
	}
}
