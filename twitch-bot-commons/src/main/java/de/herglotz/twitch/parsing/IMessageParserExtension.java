package de.herglotz.twitch.parsing;

import de.herglotz.twitch.messages.Message;

public interface IMessageParserExtension {

	boolean isResponsible(String message);

	Message parse(String username, String targetChannel, String message);

}
