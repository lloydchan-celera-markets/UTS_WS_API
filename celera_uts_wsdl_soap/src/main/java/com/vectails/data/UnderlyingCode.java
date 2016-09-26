package com.vectails.data;

import com.vectails.xml.IXmlParser;

public class UnderlyingCode implements IXmlParser{
	private String Code;
	private String Name;
	
	public void setCode(String code) {
		Code = code;
	}

	public void setName(String name) {
		Name = name;
	}

	@Override
	public String toString() {
		return "UnderlyingCode [Code=" + Code + ", Name=" + Name + "]";
	}
}
