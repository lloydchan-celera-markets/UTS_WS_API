package com.vectails.common;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Collection;
import java.util.Iterator;

import com.vectails.session.IUtsLastTimeUpdateListener;
import com.vectails.xml.data.tag.ParameterTag;

public class GenericFactory<T> implements IGenericFactory<T> {
	private Class<T> clazz;
	
	public GenericFactory(Class clazz) {
		this.clazz = clazz;
	}

	private void setClass(Class clazz) {
		this.clazz = clazz;
	}

	public T build() throws InstantiationException, IllegalAccessException {
		return clazz.newInstance();
	}
	
	public T build(String _clazz) throws InstantiationException, IllegalAccessException, ClassNotFoundException {
		setClass(Class.forName(_clazz));
		return build();
	}
	
	@Override
	public String toString()
	{
		StringBuilder sb = new StringBuilder();
		sb.append(this.getClass().getSimpleName()).append(" [");

		for (Field field : getClass().getDeclaredFields())
		{
			try
			{
				Class clazz = field.getType();
				if (Collection.class.isAssignableFrom(clazz))
				{
					field.setAccessible(true); // unsafe
					Object listObj = field.get(this);

					Method mthGet = field.getType().getDeclaredMethod("iterator", null);
					mthGet.setAccessible(true); // unsafe
					Iterator begin = (Iterator) mthGet.invoke(listObj, null);
					for (Iterator it = begin; it.hasNext();)
					{
						sb.append(it.next().toString()).append(", ");
					}
				}
				else
				// if (ParameterTag.class.isAssignableFrom(field.getType()))
				{
					String fieldName = field.getName();
					Method setter = this.getClass().getMethod("get" + fieldName, null);
					Object o = setter.invoke(this, null);
					sb.append(fieldName).append("=").append(o).append(", ");
				}
				// else
				// {
				// String fieldName = field.getName();
				// Method setter = this.getClass().getMethod("get" + fieldName,
				// null);
				// String s = (String) setter.invoke(this, null);
				// sb.append(fieldName).append("=").append(s).append(", ");
				// }

			}
			catch (Exception e)
			{
				e.printStackTrace();
			}
		}

		sb.append("]");
		return sb.toString();
	}
}