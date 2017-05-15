package com.celera.mongo.entity;

public interface ICustomizeMongoDocument<T> extends IMongoDocument
{
	public String getKey();
}
