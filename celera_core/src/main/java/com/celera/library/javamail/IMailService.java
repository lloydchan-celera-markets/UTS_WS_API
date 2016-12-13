package com.celera.library.javamail;

import java.util.Date;
import java.util.List;

import com.celera.mongo.entity.IMongoDocument;

public interface IMailService
{
	public void setListener(IMailListener cb);
	public List<IMongoDocument> getAllFromInbox();
	public List<IMongoDocument> getBetween(Date somePastDate, Date someFutureDate);
}
