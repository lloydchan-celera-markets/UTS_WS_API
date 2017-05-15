package com.celera.mongo.entity;

import java.util.Date;

public interface IMongoDocument<T>
{
	public void setLastModified(Date d);
	public Class getRepo();
	public String getId();
	public void setId(String id);
}
