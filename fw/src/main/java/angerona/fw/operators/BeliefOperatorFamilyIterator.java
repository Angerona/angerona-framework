package angerona.fw.operators;

public interface BeliefOperatorFamilyIterator {
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
}
