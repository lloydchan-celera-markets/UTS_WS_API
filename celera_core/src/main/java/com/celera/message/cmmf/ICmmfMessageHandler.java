package com.celera.message.cmmf;

import java.nio.ByteBuffer;

import com.celera.core.dm.EAdminAction;

public interface ICmmfMessageHandler
{
	default public void onMessage(ByteBuffer buf) {
		EApp sender = EApp.get(buf.getChar());
		EMessageType type = EMessageType.get(buf.getChar());
		EFoCommand command = EFoCommand.get(buf.getChar());
		
		switch (type) {
		case ADMIN: {
			EAdminAction action = EAdminAction.get(buf.getChar());	
			switch (action) {
				case LOGIN: {
					byte b[] = buf.array();
					int pos = buf.position();
					int limit = buf.limit();
					String password = new String(b, pos, limit, java.nio.charset.StandardCharsets.UTF_8);
					
					break;
				}
				case LOGOUT: {
					break;
				}
				case CHANGE_PASSWORD: {
					break;
				}
				case SOD: {
					break;
				}
				case SUBSCRIBE_MARKET_DATA: {
					break;
				}
				case UNSUBSCRIBE_MARKET_DATA: {
					break;
				}
			}
			break;
		}
		case QUERY: {
			break;
		}
		case RESPONSE: {
			break;
		}
		case TASK: {
			break;
		}
		}
	}
}
