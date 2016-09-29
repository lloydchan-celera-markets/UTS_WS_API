package com.vectails.session;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import com.celera.core.dm.IInstrument;
import com.vectails.xml.INodeUpdateListener;
import com.vectails.xml.IXmlNode;
import com.vectails.xml.data.Addressee;
import com.vectails.xml.data.DerivativeType;
import com.vectails.xml.data.IndexFuture;
import com.vectails.xml.data.Underlying;

public class UtsDirectAccessSession implements IOnUpdateNode {

	private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS");

	private final String EntityCode;
	private final String ClientCode;
	private final String Password;
	private final String SessionId;
	private final String ClientVersion;
	
	private String TimeOfLastRecoveredQuotes;

	private volatile LocalDate addresseeLT = LocalDate.parse("2000-01-01 00:00:00.000", formatter);
	private volatile LocalDate underlyingLT = LocalDate.parse("2000-01-01 00:00:00.000", formatter);
	private volatile LocalDate derivTypeLT = LocalDate.parse("2000-01-01 00:00:00.000", formatter);
	private volatile LocalDate idxFutureLT = LocalDate.parse("2000-01-01 00:00:00.000", formatter);

	// =\"CELERA\" ClientCode=\"DACTEST\" Password=\"uat\"
	// Command=\"OpenDirectAccessSession\"
	// SessionId=\"7df96e02-e058-4212-a822-bd3cce2a87db\"
	// ClientVersion=\"UtsDacV1.8\"></UtsDirectAccessMessage";

	public UtsDirectAccessSession(String entityCode, String clientCode, String password, String sessionId,
			String clientVersion) {
		EntityCode = entityCode;
		ClientCode = clientCode;
		Password = password;
		SessionId = sessionId;
		ClientVersion = clientVersion;

		TimeOfLastRecoveredQuotes = null;
	}

	public static DateTimeFormatter getFormatter() {
		return formatter;
	}

	public String getEntityCode() {
		return EntityCode;
	}

	public String getClientCode() {
		return ClientCode;
	}

	public String getPassword() {
		return Password;
	}

	public String getSessionId() {
		return SessionId;
	}

	public String getClientVersion() {
		return ClientVersion;
	}

	public String getTimeOfLastRecoveredQuotes()
	{
		return TimeOfLastRecoveredQuotes;
	}

	public LocalDate getAddresseeLT()
	{
		return addresseeLT;
	}

	public LocalDate getUnderlyingLT()
	{
		return underlyingLT;
	}

	public LocalDate getDerivTypeLT()
	{
		return derivTypeLT;
	}

	public LocalDate getIdxFutureLT()
	{
		return idxFutureLT;
	}

	@Override
	public void onUpdateNode(INodeUpdateListener listener)
	{
		LocalDate ld = listener.getLastTime();
		if (listener instanceof Underlying)
		{
			setUnderlyingLT(ld);
		} else if (listener instanceof DerivativeType)
		{
			setDerivTypeLT(ld);
		} else if (listener instanceof Addressee)
		{
			setAddresseeLT(ld);
		} else if (listener instanceof IndexFuture)
		{
			setIdxFutureLT(ld);
		}
	}
	
	public void setAddresseeLT(LocalDate last)
	{
		if (last.isAfter(this.addresseeLT)) {
			addresseeLT = last;
			System.out.println("Addressee LastTime=" + last);
		}
	}

	public void setDerivTypeLT(LocalDate last)
	{
		if (last.isAfter(this.derivTypeLT)) {
			derivTypeLT = last;
			System.out.println("DerivativeType LastTime=" + last);
		}
	}

	public void setUnderlyingLT(LocalDate last)
	{
		if (last.isAfter(this.underlyingLT)) {
			underlyingLT = last;
			System.out.println("Underlying LastTime=" + last);
		}
	}

	public void setIdxFutureLT(LocalDate last)
	{
		if (last.isAfter(this.idxFutureLT)) {
			idxFutureLT = last;
			System.out.println("IndexFuture LastTime=" + last);
		}
	}

	public void setTimeOfLastRecoveredQuotes(String timeOfLastRecoveredQuotes) {
		TimeOfLastRecoveredQuotes = timeOfLastRecoveredQuotes;
	}

	@Override
	public String toString() {
		return "UtsDirectAccessSession [EntityCode=" + EntityCode + ", ClientCode=" + ClientCode + ", Password="
				+ Password + ", SessionId=" + SessionId + ", ClientVersion=" + ClientVersion
				+ ", TimeOfLastRecoveredQuotes=" + TimeOfLastRecoveredQuotes + ", lastAddresseeTime="
				+ addresseeLT + ", lastUnderlyingTime=" + underlyingLT + ", lastDerivativeTypeTime="
				+ derivTypeLT + ", lastIndexFutureTime=" + idxFutureLT + "]";
	}
}
