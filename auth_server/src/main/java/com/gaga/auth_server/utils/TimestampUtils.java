package com.gaga.auth_server.utils;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;

public class TimestampUtils {
	private static final String RESPONSE_TIMESTAMP_PATTERN = "yyyy-MM-dd HH:mm:ss";

	private static final DateTimeFormatter responseDateTimeFormatter = DateTimeFormatter.ofPattern(RESPONSE_TIMESTAMP_PATTERN);
	private SimpleDateFormat responseSimpleDateFormatter = new SimpleDateFormat(RESPONSE_TIMESTAMP_PATTERN);

	private TimestampUtils() {
	}

	public String getNow() {
		return convertResponseDateFormat(LocalDateTime.now());
	}

	public String convertResponseDateFormat(LocalDateTime time) {
		return time.format(responseDateTimeFormatter);
	}

	public String convertResponseDateFormat(Date time) {
		return responseSimpleDateFormatter.format(time);
	}
}
