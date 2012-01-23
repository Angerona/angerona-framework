package angerona.fw.gui.dummy;
import angerona.fw.gui.BeliefbaseComponent;

/**
 * Small "Enhancement" to the beliefbase component view: "DUMMY" is title! ;-)
 * @author Tim Janus
 */
public class DummyBeliefbaseComponent extends BeliefbaseComponent {
	/** kill warning */
	private static final long serialVersionUID = 8044649633781759407L;

	// TODO: Think about a solution for using default ctors.
	public DummyBeliefbaseComponent() {
		super("DUMMY", null);
	}
}
