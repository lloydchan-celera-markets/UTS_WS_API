package com.vectails.xml.data;

public class LastUpdateDateTime {
	private final String name;
	private final String dateTime;
	
	public LastUpdateDateTime(String name, String dateTime) {
		this.name = name;
		this.dateTime = dateTime;
	}
	
	public String toXmlString() {
		return "<LastUpdateDateTime Name='" + name + "' dateTime='" + dateTime	+ "'/>";
	}

	@Override
	public String toString() {
		return "<LastUpdateDateTime Name='" + name + "' DateTime='" + dateTime + "'/>";
	}
}
