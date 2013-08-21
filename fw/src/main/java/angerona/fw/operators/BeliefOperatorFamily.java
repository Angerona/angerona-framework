package angerona.fw.operators;

import java.util.Map;

/**
 * This interface represents a belief operator family. Such a family defines
 * an order on reasoners, so that they can be used in a preference relation
 * like manner. Credulous < Skeptical means everything than can be conclude using 
 * the skeptical reasoner can also be conclude by the credulous reasoner.
 * 
 * @author Tim Janus
 *
 */
public interface BeliefOperatorFamily extends BeliefOperatorFamilyIterator {
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
	
	/**
	 * Processes the distance between from and to as a numerical value.
	 * @param from	One operator of this belief operator family
	 * @param to	Another operator of this belief operator family
	 * @return		A double representing the distance between from and to
	 */
	double distance(OperatorCallWrapper from, OperatorCallWrapper to);
	
	/**
	 * returns an operator that is member of this belief operator family and that 
	 * fulfills the following properties:
	 * - The full java class name is operatorCls
	 * - The settings of the operator are represented by the map given in the settings parameter
	 * @param operatorCls
	 * @param settings
	 * @return	An operator instances if the belief operator family contains an operator described
	 * 			by the properties given as parameter and null if such a operator does not exists
	 * 			in the belief-operator-family.
	 */
	OperatorCallWrapper getOperator(String operatorCls, Map<String, String> settings);
}
