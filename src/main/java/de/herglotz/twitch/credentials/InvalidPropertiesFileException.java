package de.herglotz.twitch.credentials;

public class InvalidPropertiesFileException extends Exception {

	private static final long serialVersionUID = -8673497280494625382L;

	public InvalidPropertiesFileException(String message, Throwable throwable) {
		super(message, throwable);
	}

}
