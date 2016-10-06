package com.celera.library.javamail;

import java.util.Date;

public interface IMailService
{
	public void setListener(IMailListener cb);
	public void getAllFromInbox();
	public void getBetween(Date somePastDate, Date someFutureDate);
}
