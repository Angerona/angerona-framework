package com.github.angerona.knowhow.situation;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import net.sf.tweety.lp.asp.syntax.DLPAtom;

import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Root;

@Root(name="investigation-situation")
public class InvestigationSituation extends Situation {
	@ElementList(entry="query", inline=true, required=true)
	private Set<DLPAtom> queries = new HashSet<>();
	
	@ElementList(entry="source", inline=true, required=true)
	private Set<String> sources = new HashSet<>();
	
	public Set<DLPAtom> getQueries() {
		return Collections.unmodifiableSet(queries);
	}
	
	public Set<String> getSources() {
		return Collections.unmodifiableSet(sources);
	}
	
}
