package com.vectails.session;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.uts.tools.Uts2Dm;
import com.vectails.xml.IUtsLastTimeUpdater;
import com.vectails.xml.IXmlNode;
import com.vectails.xml.data.Addressee;
import com.vectails.xml.data.DerivativeType;
import com.vectails.xml.data.IndexFuture;
import com.vectails.xml.data.Underlying;
import com.vectails.xml.data.UtsDirectAccessResponse;

public class UtsSession implements IUtsLastTimeUpdateListener {

	private static final Logger logger = LoggerFactory.getLogger(UtsSession.class); 
	
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

	public UtsSession(String entityCode, String clientCode, String password, String sessionId,
			String clientVersion) {
		EntityCode = entityCode;
		ClientCode = clientCode;
		Password = password;
		SessionId = sessionId;
		ClientVersion = clientVersion;

		TimeOfLastRecoveredQuotes = "0";
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

	public void setTimeofLastRecoveredQuotes(String last)
	{
		if (last != null)
		{
			this.TimeOfLastRecoveredQuotes = last;
			logger.trace("setTimeofLastRecoveredQuotes[{}]", last);
		}
	}

	public void setAddresseeLT(LocalDate last)
	{
		if (this.addresseeLT.isBefore(last)) {
			addresseeLT = last;
			logger.trace("setAddresseeLT[{}]", last);
		}
	}

	public void setDerivTypeLT(LocalDate last)
	{
		if (this.derivTypeLT.isBefore(last)) {
			derivTypeLT = last;
			logger.trace("setDerivTypeLT[{}]", last);
		}
	}

	public void setUnderlyingLT(LocalDate last)
	{
		if (this.underlyingLT.isBefore(last)) {
			underlyingLT = last;
			logger.trace("setUnderlyingLT[{}]", last);
		}
	}

	public void setIdxFutureLT(LocalDate last)
	{
		if (this.idxFutureLT.isBefore(last)) {
			idxFutureLT = last;
			logger.trace("idxFutureLT[{}]", last);
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
