package angerona.fw.gui.dummy;
import angerona.fw.gui.BeliefbaseComponent;
import angerona.fw.logic.dummy.DummyBeliefbase;

/**
 * Small "Enhancement" to the beliefbase component view: "DUMMY" is title! ;-)
 * @author Tim Janus
 */
public class DummyBeliefbaseComponent extends BeliefbaseComponent {
	/** kill warning */
	private static final long serialVersionUID = 8044649633781759407L;

	public void init() {
		super.init();
		setTitle("Dummy");
	}
	
	@Override
	public Class<?> getObservationObjectType() {
		return DummyBeliefbase.class;
	}
	
	public void setObservationObject(Object obj) {
		if(! (obj instanceof DummyBeliefbase)) {
			throw new IllegalArgumentException("Observation Object must be of type 'DummyBeliefbase'");
		}
		this.beliefbase = (DummyBeliefbase)obj;
		this.actualBeliefbase = this.beliefbase;
		setTitle("DUMMY");
	}
}
