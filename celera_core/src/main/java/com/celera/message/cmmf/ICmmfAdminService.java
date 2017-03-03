package com.celera.message.cmmf;

public interface ICmmfAdminService
{
	public void login(String password);
	public void changePassword(String oldPwd, String newPwd);
	public void logout();
	public void setReady();
	public void SOD();
	public void subscribeMarketData();
	public void unsubscribeMarketData();
	public void query(String command, String param);
	public void getAllInstrument();
}
