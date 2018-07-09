package de.herglotz.twitch.commands;

import de.herglotz.twitch.api.irc.ITwitchChatWriter;
import de.herglotz.twitch.api.irc.messages.CommandMessage;

public interface Command {

	void run(ITwitchChatWriter writer, CommandMessage commandMessage);

	boolean isResponsible(String commandMessage);
}
