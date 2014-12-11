package com.github.angerona.fw.motivation.plans.dto;

/**
 * 
 * @author Manuel Barbi
 * 
 */
public class ActionDTO implements PlanDTO {

	protected String id;
	protected String cond;
	protected String src;
	protected String dst;

	public ActionDTO() {}

	public ActionDTO(String id, String cond, String src, String dst) {
		this.id = id;
		this.cond = cond;
		this.src = src;
		this.dst = dst;
	}

	@Override
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getCond() {
		return cond;
	}

	public void setCond(String cond) {
		this.cond = cond;
	}

	public String getSrc() {
		return src;
	}

	public void setSrc(String src) {
		this.src = src;
	}

	public String getDst() {
		return dst;
	}

	public void setDst(String dst) {
		this.dst = dst;
	}

	@Override
	public String toString() {
		return "[" + id + ", " + cond + ", " + src + ", " + dst + "]";
	}

}
