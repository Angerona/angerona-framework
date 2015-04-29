package com.github.angerona.fw.motivation.plans.dto;

import java.util.Collection;

/**
 * 
 * @author Manuel Barbi
 * 
 */
public class TrailDTO {

	protected String des;
	protected String org;
	protected Collection<ActionDTO> pln;

	public TrailDTO() {}

	public TrailDTO(String des, String org, Collection<ActionDTO> pln) {
		this.des = des;
		this.org = org;
		this.pln = pln;
	}

	public String getDes() {
		return des;
	}

	public void setDes(String des) {
		this.des = des;
	}

	public String getOrg() {
		return org;
	}

	public void setOrg(String org) {
		this.org = org;
	}

	public Collection<ActionDTO> getPln() {
		return pln;
	}

	public void setPln(Collection<ActionDTO> pln) {
		this.pln = pln;
	}

	@Override
	public String toString() {
		return "[" + des + ", " + org + ", " + pln + "]";
	}

}
