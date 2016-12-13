package com.celera.message.cmmf;

import com.celera.thread.AbstractTask;

public abstract class AbstractCmmfService extends AbstractTask
{
	protected final ICmmfListener listener;
	
	public AbstractCmmfService(ICmmfListener listener)
	{
		this.listener = listener;
	}

	@Override
	abstract public void run();
}
