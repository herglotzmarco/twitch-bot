package de.herglotz.twitch.api.irc;

public interface ITwitchChatWriter {

	void sendRawMessage(String message);

	void sendChatMessage(String targetChannel, String message);
}
