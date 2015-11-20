package com.github.angerona.fw.operators.parameter;

import java.util.Set;

import net.sf.tweety.logics.fol.syntax.FolFormula;
import net.sf.tweety.lp.nlp.syntax.NLPProgram;
import net.sf.tweety.lp.nlp.syntax.NLPRule;

import com.github.angerona.fw.BaseBeliefbase;
import com.github.angerona.fw.Perception;

public class TranslatorParameter extends BeliefbasePluginParameter {
	private Perception percept;
	
	private NLPProgram program;
	
	public TranslatorParameter() {}
	
	public TranslatorParameter(BaseBeliefbase bb, Perception p) {
		super(bb);
		if(p==null)
			throw new IllegalArgumentException("Peception p must not be null.");
		this.percept = p;
	}
	
	public TranslatorParameter(BaseBeliefbase bb, FolFormula information) {
		super(bb);
		if(information == null)
			throw new IllegalArgumentException("information must not be null.");
		this.program = new NLPProgram();
		program.add(new NLPRule(information));
	}
	
	public TranslatorParameter(BaseBeliefbase bb, Set<FolFormula> informations) {
		super(bb);
		if(informations == null)
			throw new IllegalArgumentException("informations must not be null.");
		this.program = new NLPProgram();
		for(FolFormula formula : informations) {
			program.add(new NLPRule(formula));
		}
	}
	
	public TranslatorParameter(BaseBeliefbase bb, NLPRule rule) {
		super(bb);
		if(rule == null)
			throw new IllegalArgumentException("rule must not be null.");
		this.program = new NLPProgram();
		this.program.add(rule);
	}
	
	public TranslatorParameter(BaseBeliefbase bb, NLPProgram program) {
		super(bb);
		if(program == null)
			throw new IllegalArgumentException("program must not be null.");
		this.program = program;
	}
	
	public Perception getPerception() {
		return percept;
	}
	
	public NLPProgram getInformation() {
		return program;
	}
}
