package com.celera.library.javamail;

import javax.mail.Message;

public interface IMailListener
{
	public void onEmail(Message message);
}
