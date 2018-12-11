package de.herglotz.twitch.module;

import de.herglotz.twitch.api.irc.ITwitchChatWriter;
import de.herglotz.twitch.commands.CommandHandler;
import de.herglotz.twitch.events.EventBus;
import de.herglotz.twitch.persistence.Database;

public class CommandModule implements IModule {

	@Override
	public void startup(Database database, ITwitchChatWriter writer) {
		EventBus.instance().register(new CommandHandler(database, writer));
	}

	@Override
	public String getId() {
		return CommandModule.class.getName();
	}

}
