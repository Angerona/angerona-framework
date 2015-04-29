package com.github.angerona.fw.motivation.plans.dto;

/**
 * 
 * @author Manuel Barbi
 *
 */
public class ActionDTO {

	private String id;
	private String src;
	private String dst;
	private String cond;
	private String fin;

	public ActionDTO() {}

	public ActionDTO(String id, String src, String dst, String cond, String fin) {
		this.id = id;
		this.src = src;
		this.dst = dst;
		this.cond = cond;
		this.fin = fin;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
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

	public String getCond() {
		return cond;
	}

	public void setCond(String cond) {
		this.cond = cond;
	}

	public String getFin() {
		return fin;
	}

	public void setFin(String fin) {
		this.fin = fin;
	}

	@Override
	public String toString() {
		return "[" + id + ", " + src + ", " + dst + ", " + cond + ", " + fin + "]";
	}
	
	

}
