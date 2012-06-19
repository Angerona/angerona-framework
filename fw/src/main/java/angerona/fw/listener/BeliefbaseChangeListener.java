package angerona.fw.listener;

import angerona.fw.BaseBeliefbase;

/**
 * A listener interface for listening to changes of the
 * belief-base content.
 * @author Tim Janus
 */
public interface BeliefbaseChangeListener {
	/**
	 * is called when a beliefbase changed its content.
	 * @param 	bb reference to the beliefbase with the changed content.
	 */
	void changed(BaseBeliefbase bb);
}
