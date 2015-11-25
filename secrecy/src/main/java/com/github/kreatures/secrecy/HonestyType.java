package com.github.kreatures.secrecy;

public enum HonestyType {
	HT_HONEST,
	HT_WITHHOLDING,
	HT_BULLSHITTING,
	HT_LIE;
	
	@Override
	public String toString() {
		return super.toString().substring(3).toLowerCase();
	}
}
