package de.herglotz.twitch.module;

import de.herglotz.twitch.api.irc.ITwitchChatWriter;
import de.herglotz.twitch.persistence.Database;

public interface IModule {

	void startup(Database database, ITwitchChatWriter writer);

}
