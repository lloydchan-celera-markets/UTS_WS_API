package com.celera.ipc;

import com.celera.message.cmmf.EApp;

public interface IMulticastClient extends Runnable
{
	public void setTcpListener(ITcpServerListener listener);
	public void subscribe(EApp app);
	public void unsubscribe(EApp app);
}
