package com.vectails.xml;

import java.time.LocalDate;

import com.vectails.session.IUtsLastTimeUpdateListener;

public interface IUtsLastTimeUpdater
{
	public LocalDate getLastTime();
	public void updateLastTime(IUtsLastTimeUpdateListener l);
}
