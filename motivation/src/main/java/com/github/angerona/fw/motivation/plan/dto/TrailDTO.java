package com.github.angerona.fw.motivation.plan.dto;

import java.util.Collection;

/**
 * 
 * @author Manuel Barbi
 * 
 */
public class TrailDTO {

	protected String des;
	protected Collection<PlanDTO> pln;

	public TrailDTO() {}

	public TrailDTO(String des, Collection<PlanDTO> pln) {
		this.des = des;
		this.pln = pln;
	}

	public String getDes() {
		return des;
	}

	public void setDes(String des) {
		this.des = des;
	}

	public Collection<PlanDTO> getPln() {
		return pln;
	}

	public void setPln(Collection<PlanDTO> pln) {
		this.pln = pln;
	}

	@Override
	public String toString() {
		return "[" + des + ", " + pln + "]";
	}

}
