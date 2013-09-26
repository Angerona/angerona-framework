package com.github.angerona.knowhow.situation;

import java.io.File;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import net.sf.tweety.logics.commons.syntax.Predicate;
import net.sf.tweety.logics.firstorderlogic.syntax.FOLAtom;

import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Root;

import com.github.angerona.fw.serialize.SerializeHelper;

@Root(name="investigation-situation")
public class InvestigationSituation extends Situation {
	@ElementList(entry="query", inline=true, required=true)
	private Set<FOLAtom> queries = new HashSet<>();
	
	@ElementList(entry="source", inline=true, required=true)
	private Set<String> sources = new HashSet<>();
	
	public Set<FOLAtom> getQueries() {
		return Collections.unmodifiableSet(queries);
	}
	
	public Set<String> getSources() {
		return Collections.unmodifiableSet(sources);
	}
	
	public static void main(String [] args) {
		InvestigationSituation sit = new InvestigationSituation();
		sit.goal = "investigate";
		
		sit.queries.add(new FOLAtom(new Predicate("os")));
		sit.queries.add(new FOLAtom(new Predicate("rap")));
		sit.queries.add(new FOLAtom(new Predicate("cai")));
		
		sit.sources.add("claire");
		sit.sources.add("detective");
		
		sit.filenameBackgroundProgram = new File("basis_background.dlp");
		
		SerializeHelper.outputXml(sit, System.out);
	}
}
