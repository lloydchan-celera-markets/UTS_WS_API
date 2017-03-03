package com.celera.message.cmmf;

public class CmmfBuilder
{
	public static final int HEADER_SIZE = 4;
	
	public static byte[] buildMessage(EApp sender, EApp receiver, EMessageType type, EFoCommand cmd, byte[] body)
	{
		if (body == null) {
			byte[] combine = null;
			combine = new byte[HEADER_SIZE];
			combine[0] = (byte) sender.asChar();
			combine[1] = (byte) receiver.asChar();
			combine[2] = (byte) type.asChar();
			combine[3] = (byte) cmd.asChar();
			return combine;
		}
		else {
			byte[] combine = null;
			int cap = body.length + HEADER_SIZE;
			combine = new byte[cap];
			combine[0] = (byte) sender.asChar();
			combine[1] = (byte) receiver.asChar();
			combine[2] = (byte) type.asChar();
			combine[3] = (byte) cmd.asChar();
			System.arraycopy(body, 0, combine, HEADER_SIZE, body.length);
			return combine;
		}
	}

	public static byte[] buildMessage(EApp sender, EApp receiver, EMessageType type, EFoCommand cmd, byte[] body, int size)
	{
		int cap = size + HEADER_SIZE;
		byte[] combine = new byte[cap];
		combine[0] = (byte) sender.asChar();
		combine[1] = (byte) receiver.asChar();
		combine[2] = (byte) type.asChar();
		combine[3] = (byte) cmd.asChar();
		System.arraycopy(body, 0, combine, HEADER_SIZE, size);
		return combine;
	}
	
	public static byte[] buildJson(EApp sender, EMessageType type, EFoCommand cmd, byte[] body, int size) {
		return null;
	}
}
