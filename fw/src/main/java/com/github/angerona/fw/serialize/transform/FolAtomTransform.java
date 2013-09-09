package com.github.angerona.fw.serialize.transform;

import java.io.StringReader;

import net.sf.tweety.logics.firstorderlogic.parser.FolParserB;
import net.sf.tweety.logics.firstorderlogic.syntax.FOLAtom;
import net.sf.tweety.logics.firstorderlogic.syntax.FolSignature;

import org.simpleframework.xml.transform.Transform;

/**
 * Extension to simple xml deserialization: Transforms a FOL-Atom.
 * @author Tim Janus
 */
public class FolAtomTransform implements Transform<FOLAtom>{

	@Override
	public FOLAtom read(String strAtom) throws Exception {
		FolParserB parser = new FolParserB(new StringReader(strAtom));
		return parser.atom(new FolSignature());
	}

	@Override
	public String write(FOLAtom atom) throws Exception {
		return atom.toString();
	}


}
