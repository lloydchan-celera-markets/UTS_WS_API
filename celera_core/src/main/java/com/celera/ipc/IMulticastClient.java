package com.celera.ipc;

import com.celera.message.cmmf.EApp;
import com.celera.message.cmmf.ICmmfListener;

public interface IMulticastClient extends Runnable
{
	public void setTcpListener(ICmmfListener listener);
	public void subscribe(EApp app);
	public void unsubscribe(EApp app);
}
