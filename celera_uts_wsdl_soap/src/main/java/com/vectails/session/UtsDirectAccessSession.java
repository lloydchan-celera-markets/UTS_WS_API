package com.vectails.session;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class UtsDirectAccessSession {

	private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS");
	
	private final String EntityCode;
	private final String ClientCode;
	private final String Password;
	private final String SessionId;
	private final String ClientVersion;
	private String TimeOfLastRecoveredQuotes;

	private LocalDate lastAddresseeTime = LocalDate.parse("2000-01-01 00:00:00.000", formatter);
	private LocalDate lastUnderlyingTime = LocalDate.parse("2000-01-01 00:00:00.000", formatter);
	private LocalDate lastDerivativeTypeTime = LocalDate.parse("2000-01-01 00:00:00.000", formatter);
	private LocalDate lastIndexFutureTime = LocalDate.parse("2000-01-01 00:00:00.000", formatter);

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

	public void setLastAddresseeTime(String last) {
		LocalDate d = LocalDate.parse(last, formatter);
		if (d.isAfter(this.lastAddresseeTime))
			lastAddresseeTime = d;
	}

	public void setLastUnderlyingTime(String last) {
		LocalDate d = LocalDate.parse(last, formatter);
		if (d.isAfter(this.lastUnderlyingTime))
			lastUnderlyingTime = d;
	}

	public void setLastDerivativeTypeTime(String last) {
		LocalDate d = LocalDate.parse(last, formatter);
		if (d.isAfter(this.lastDerivativeTypeTime))
			lastDerivativeTypeTime = d;
	}

	public void setLastIndexFutureTime(String last) {
		LocalDate d = LocalDate.parse(last, formatter);
		if (d.isAfter(this.lastIndexFutureTime))
			lastIndexFutureTime = d;
	}

	public void setTimeOfLastRecoveredQuotes(String timeOfLastRecoveredQuotes) {
		TimeOfLastRecoveredQuotes = timeOfLastRecoveredQuotes;
	}

	@Override
	public String toString() {
		return "UtsDirectAccessSession [EntityCode=" + EntityCode + ", ClientCode=" + ClientCode + ", Password="
				+ Password + ", SessionId=" + SessionId + ", ClientVersion=" + ClientVersion
				+ ", TimeOfLastRecoveredQuotes=" + TimeOfLastRecoveredQuotes + ", lastAddresseeTime="
				+ lastAddresseeTime + ", lastUnderlyingTime=" + lastUnderlyingTime + ", lastDerivativeTypeTime="
				+ lastDerivativeTypeTime + ", lastIndexFutureTime=" + lastIndexFutureTime + "]";
	}
}
