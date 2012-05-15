package angerona.fw.logic;

import junit.framework.TestCase;
import net.sf.tweety.logics.firstorderlogic.syntax.Atom;
import net.sf.tweety.logics.firstorderlogic.syntax.Predicate;
import angerona.fw.Desire;

public class DesireTest extends TestCase {
	// test for set bug
	public void testCRD() {
		Desires desComp = new Desires();
		Atom a = new Atom(new Predicate("test"));
		desComp.add(a);
		
		assertNotNull(desComp.getDesire(a));
		assertNotNull(desComp.getDesire(new Desire(a)));
		desComp.remove(new Desire(a));
		assertNull(desComp.getDesire(a));
	}
}
