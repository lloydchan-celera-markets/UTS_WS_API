package com.vectails.xml.data.tag;

public class ParameterTag
{
	private String NameInString;
	private String value;
	
	public void setNameInString(String nameInString)
	{
		NameInString = nameInString;
	}

	public void setValue(String value)
	{
		this.value = value;
	}

	public String getNameInString()
	{
		return NameInString;
	}

	public String getValue()
	{
		return value;
	}

	@Override
	public String toString()
	{
		return "ParameterTag [NameInString=" + NameInString + ", " + value + "]";
	}
}
