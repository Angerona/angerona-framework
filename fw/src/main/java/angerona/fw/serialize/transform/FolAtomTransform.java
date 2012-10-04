package angerona.fw.serialize.transform;

import net.sf.tweety.logics.firstorderlogic.syntax.Atom;
import net.sf.tweety.logics.firstorderlogic.syntax.Predicate;

import org.simpleframework.xml.transform.Transform;

/**
 * Extension to simple xml deserialization: Transforms a FOL-Atom.
 * @author Tim Janus
 */
public class FolAtomTransform implements Transform<Atom>{

	@Override
	public Atom read(String strAtom) throws Exception {
		return new Atom(new Predicate(strAtom));
		// TODO: Update Daniels desire handling and parse the desires correctly.
		//FolParserB parser = new FolParserB(new StringReader(strAtom));
		//return parser.atom(new FolSignature());
	}

	@Override
	public String write(Atom atom) throws Exception {
		return atom.toString();
	}


}
