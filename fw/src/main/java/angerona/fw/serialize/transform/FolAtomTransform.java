package angerona.fw.serialize.transform;


import net.sf.tweety.logics.firstorderlogic.syntax.Atom;
import net.sf.tweety.logics.firstorderlogic.syntax.Predicate;

import org.simpleframework.xml.transform.Transform;

public class FolAtomTransform implements Transform<Atom> {

	@Override
	public Atom read(String arg0) throws Exception {
		return new Atom(new Predicate(arg0));
	}

	@Override
	public String write(Atom arg0) throws Exception {
		return arg0.toString();
	}

}
