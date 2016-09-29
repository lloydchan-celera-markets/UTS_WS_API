package com.vectails.message;

import java.time.format.DateTimeFormatter;

public interface ICommonFields
{
	static final DateTimeFormatter DT_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS");
}
