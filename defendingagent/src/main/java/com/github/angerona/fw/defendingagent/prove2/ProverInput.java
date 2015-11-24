package com.github.angerona.fw.defendingagent.Prover;

import java.util.List;

import com.github.angerona.fw.defendingagent.Prover.Prover.InferenceSystem;

public class ProverInput {
	public List<String> kFormulas;
	public String formulaToProve;
	public InferenceSystem chooseInferenceSystem = InferenceSystem.RATIONAL;
	
	public ProverInput(){
		
	}
	
	public ProverInput(List<String> kFormulas, String formulaToProve, InferenceSystem inferenceSystem) {
		this.kFormulas = kFormulas;
		this.formulaToProve = formulaToProve;
		this.chooseInferenceSystem = inferenceSystem;
	}
	
	public ProverInput(List<String> kFormulas, String formulaToProve) {
		this(kFormulas, formulaToProve, InferenceSystem.RATIONAL);
	}
	
	public String toString() {
		return "kFormulas: " + kFormulas + "\ntoProve: " + formulaToProve;
	}
}
