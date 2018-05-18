package de.herglotz.twitch.events.listeners;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import de.herglotz.twitch.api.irc.TwitchApiFacade;
import de.herglotz.twitch.commands.Command;
import de.herglotz.twitch.commands.CustomCommands;
import de.herglotz.twitch.commands.HiCommand;
import de.herglotz.twitch.events.CommandMessageEvent;
import de.herglotz.twitch.events.Event;

public class CommandHandler implements EventListener {

	private Set<Command> commands;

	public CommandHandler() {
		commands = new HashSet<>();
		register(new HiCommand());
		register(new CustomCommands(new ArrayList<>()));
	}

	@Override
	public void handleEvent(Event event) {
		if (event instanceof CommandMessageEvent) {
			commands.stream()//
					.filter(c -> c.isResponsible(((CommandMessageEvent) event).getMessage().getCommand()))//
					.forEach(c -> c.run(TwitchApiFacade.instance().getWriter(),
							((CommandMessageEvent) event).getMessage()));
		}
	}

	public void register(Command command) {
		commands.add(command);
	}

}
