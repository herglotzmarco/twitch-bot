package de.herglotz.twitch.commands;

import de.herglotz.twitch.api.irc.TwitchChatWriter;
import de.herglotz.twitch.api.irc.messages.CommandMessage;

public interface Command {

	void run(TwitchChatWriter writer, CommandMessage commandMessage);

	boolean isResponsible(String commandMessage);
}
