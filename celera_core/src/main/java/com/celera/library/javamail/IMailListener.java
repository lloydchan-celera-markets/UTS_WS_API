package com.celera.library.javamail;

import java.util.List;

import javax.mail.Message;

import com.celera.mongo.entity.IMongoDocument;

public interface IMailListener
{
	public void onEmail(Message message, List<IMongoDocument> list);
}
