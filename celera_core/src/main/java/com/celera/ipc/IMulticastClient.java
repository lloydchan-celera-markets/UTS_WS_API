package com.celera.ipc;

import com.celera.message.cmmf.EApp;
import com.celera.message.cmmf.ICmmfMessageListener;

public interface IMulticastClient extends Runnable
{
	public void setTcpListener(ICmmfMessageListener listener);
	public void subscribe(EApp app);
	public void unsubscribe(EApp app);
}
