package de.herglotz.twitch.commands;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import de.herglotz.twitch.api.irc.TwitchChat;
import de.herglotz.twitch.messages.CommandMessage;

public class HiCommand implements Command {

	private Map<Integer, String> replies;

	public HiCommand() {
		replies = new HashMap<>();
		loadReplies();
	}

	@Override
	public void run(TwitchChat twitch, CommandMessage commandMessage) {
		if (isResponsible(commandMessage.getCommand())) {
			String reply = pickRandomMessage();
			twitch.sendChatMessage(String.format(reply, commandMessage.getUsername()));
		}
	}

	protected String pickRandomMessage() {
		Random rand = new Random();
		int i = rand.nextInt(replies.size());
		return replies.get(i);
	}

	@Override
	public boolean isResponsible(String commandMessage) {
		return commandMessage.equals("hi");
	}

	private void loadReplies() {
		replies.put(0, "Hallöchen %s! <3 -lich willkommen bei uns. Genieß den Stream!");
		replies.put(1, "Hey %s! Schön, dass du bei uns einschaltet hast :)");
		replies.put(2, "Oh %s ist auch hier! Setzt dich doch entspannt zu uns! :D");
		replies.put(3, "Huhu %s! Ich freu mich dass du da bist! Lehn dich zurück und genieß den Stream :)");
		replies.put(4, "Schaut mal %s hat gerade zugeschalten! Willkommen bei uns! Ich wünsche dir viel Spass! :D");
	}

}
