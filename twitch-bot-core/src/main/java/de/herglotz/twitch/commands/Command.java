package de.herglotz.twitch.commands;

import de.herglotz.twitch.api.irc.TwitchChat;
import de.herglotz.twitch.messages.CommandMessage;

public interface Command {

	void run(TwitchChat twitch, CommandMessage commandMessage);

	boolean isResponsible(String commandMessage);
}
