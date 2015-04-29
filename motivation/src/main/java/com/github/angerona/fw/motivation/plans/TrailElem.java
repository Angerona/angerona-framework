package com.github.angerona.fw.motivation.plans;

import net.sf.tweety.logics.fol.syntax.FolFormula;

import com.github.angerona.fw.Action;

/**
 * 
 * @author Manuel Barbi
 * 
 */
public class TrailElem {

	private Action action;
	private String src;
	private String dst;
	private FolFormula cond;
	private boolean fin;

	public TrailElem(Action action, String src, String dst, FolFormula cond, boolean fin) {
		this.action = action;
		this.src = src;
		this.dst = dst;
		this.cond = cond;
		this.fin = fin;
	}

	public Action getAction() {
		return action;
	}

	public String getSrc() {
		return src;
	}

	public String getDst() {
		return dst;
	}

	public FolFormula getCond() {
		return cond;
	}

	public boolean isFin() {
		return fin;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((action == null) ? 0 : action.hashCode());
		result = prime * result + ((cond == null) ? 0 : cond.hashCode());
		result = prime * result + ((dst == null) ? 0 : dst.hashCode());
		result = prime * result + (fin ? 1231 : 1237);
		result = prime * result + ((src == null) ? 0 : src.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		TrailElem other = (TrailElem) obj;
		if (action == null) {
			if (other.action != null)
				return false;
		} else if (!action.equals(other.action))
			return false;
		if (cond == null) {
			if (other.cond != null)
				return false;
		} else if (!cond.equals(other.cond))
			return false;
		if (dst == null) {
			if (other.dst != null)
				return false;
		} else if (!dst.equals(other.dst))
			return false;
		if (fin != other.fin)
			return false;
		if (src == null) {
			if (other.src != null)
				return false;
		} else if (!src.equals(other.src))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "[" + action + ", " + src + ", " + dst + "," + cond + ", " + fin + "]";
	}

}
