package com.vectails.xml.data;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public
@interface LegMapping
{
	// com.celera.core.dm.Leg
	String[] value();	// methods: setter, type, type conversion
//	String value();	
}
