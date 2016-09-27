package com.vectails.common;

public interface IGenericFactory<T> {
	public T build() throws InstantiationException, IllegalAccessException;
	public T build(String _clazz) throws InstantiationException, IllegalAccessException, ClassNotFoundException ;
}
