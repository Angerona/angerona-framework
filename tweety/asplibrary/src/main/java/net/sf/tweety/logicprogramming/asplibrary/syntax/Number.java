package net.sf.tweety.logicprogramming.asplibrary.syntax;

public class Number implements Term<Integer> {

	protected int	num;
	
	public	Number(int n) {
		this.num = n;
	}
	
	public	Number(String n) {
		this.num = Integer.parseInt(n);
	}

	public void set(String value) {
		// TODO Auto-generated method stub
		this.num = Integer.parseInt(value);
	}

	@Override
	public Integer get() {
		return num;
	}

	@Override
	public void set(Integer value) {
		this.num = value;
	}

	@Override
	public String	toString() {
		return ""+this.num;
	}
}
