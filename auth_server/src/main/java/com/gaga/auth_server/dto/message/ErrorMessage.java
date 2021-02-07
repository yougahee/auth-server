package com.gaga.auth_server.dto.message;

import com.gaga.auth_server.utils.TimestampUtils;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ErrorMessage {
	String timestamp;
	int status;
	String message;
	String path;

	public ErrorMessage() { }

	public ErrorMessage(String errorMessage, int status, String path) {
		this.timestamp = TimestampUtils.getNow();
		this.status = status;
		this.message = errorMessage;
		this.path = path;
	}

	public ErrorMessage(String errorMessage, String path) {
		this.timestamp = TimestampUtils.getNow();
		this.message = errorMessage;
		this.path = path;
	}
}