package com.celera.message.cmmf;

import com.celera.thread.AbstractTask;

public abstract class AbstractCmmfService extends AbstractTask
{
	protected final ICmmfMessageListener listener;
	
	public AbstractCmmfService(ICmmfMessageListener listener)
	{
		this.listener = listener;
	}

	@Override
	abstract public void run();
}
