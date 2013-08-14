package angerona.fw.operators;

/**
 * This interface represents a belief operator family. Such a family defines
 * an order on reasoners, so that they can be used in a preference relation
 * like manner. Credulous < Skeptical means everything than can be conclude using 
 * the skeptical reasoner can also be conclude by the credulous reasoner.
 * 
 * @author Tim Janus
 *
 */
public interface BeliefOperatorFamily {
	/**
	 * Searches the predecessor of the given operator
	 * @param current	A operator that has to be part of this operator family
	 * @return	The predecessor of the given operator or null if the operator
	 * 			is not part of the family.
	 */
	OperatorCallWrapper getPredecessor(OperatorCallWrapper current);
	
	/**
	 * Searches the successor of the given operator
	 * @param current	A operator that has to be part of this operator family
	 * @return	The successor of the given operator or null if the operator
	 * 			is not part of the family.
	 */
	OperatorCallWrapper getSuccessor(OperatorCallWrapper current);
	
	/**
	 * Adds the given operator toAdd behind the operator predecessor, if predecessor
	 * is null the operator toAdd is added at the end of the family.
	 * @param toAdd			The operator that shall be added to the family
	 * @param predecessor	A operator that is already part of the family and shall
	 * 						be the predecessor of toAdd, if this parameter is null
	 * 						the operator toAdd is added to the end of the family.
	 * @return				true if the add operation is successful, false if
	 * 						the given predecessor is not found and therefore the
	 * 						add operation does not completed.
	 */
	boolean addOperator(OperatorCallWrapper toAdd, OperatorCallWrapper predecessor);
}
