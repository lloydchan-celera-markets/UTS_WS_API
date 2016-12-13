package com.uts.tradeconfo;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.celera.adapter.EmailServiceImpl;
import com.celera.library.javamail.IMailListener;

public class UtsEmailServiceImpl extends EmailServiceImpl
{
	final static Logger logger = LoggerFactory.getLogger(UtsEmailServiceImpl.class);

	public UtsEmailServiceImpl(IMailListener listener)
	{
		super(listener);
	}
}