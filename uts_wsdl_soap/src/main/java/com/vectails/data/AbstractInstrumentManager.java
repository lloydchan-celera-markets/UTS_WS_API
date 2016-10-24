package com.vectails.data;

public class AbstractInstrumentManager
{
	static private long lastestUpdateTime = 0;

	public static void setLastUpdateTime(long lastUpdateTime)
	{
		if (lastestUpdateTime < lastUpdateTime)
			lastestUpdateTime = lastUpdateTime;
	}
}
