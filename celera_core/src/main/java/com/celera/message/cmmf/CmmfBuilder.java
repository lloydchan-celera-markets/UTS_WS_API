package com.celera.message.cmmf;

public class CmmfBuilder
{

	public static byte[] buildMessage(EApp sender, EMessageType type, ECommand cmd, byte[] body)
	{
		int cap = body.length + 3;
		byte[] combine = new byte[cap];
		combine[0] = (byte) sender.asChar();
		combine[1] = (byte) type.asChar();
		combine[2] = (byte) cmd.asChar();
		System.arraycopy(body, 0, combine, 2, body.length);
		return combine;
	}
}
