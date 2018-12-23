package de.herglotz.twitch.commands;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

import com.google.common.base.Strings;

import de.herglotz.twitch.api.irc.ITwitchChatWriter;
import de.herglotz.twitch.messages.CommandMessage;

public class BetCommand implements Command {

	private Map<String, String> bets;
	private boolean stop;

	public BetCommand() {
		bets = new HashMap<>();
		stop = false;
	}

	@Override
	public void run(ITwitchChatWriter writer, CommandMessage commandMessage) {
		if (isResponsible(commandMessage.getCommand())) {
			if (commandMessage.getParameters().get(0).equals("start")) {
				handleStartCommand(writer, commandMessage);
			} else if (commandMessage.getParameters().get(0).equals("set")) {
				handleSetCommand(writer, commandMessage);
			} else if (commandMessage.getParameters().get(0).equals("finish")) {
				handleFinishCommand(writer, commandMessage);
			} else if (commandMessage.getParameters().get(0).equals("get")) {
				handleGetCommand(writer, commandMessage);
			} else if (commandMessage.getParameters().get(0).equals("stop")) {
				handleStopCommand(writer, commandMessage);
			}
		}
	}

	private void handleGetCommand(ITwitchChatWriter writer, CommandMessage commandMessage) {
		String bet = bets.get(commandMessage.getUsername());
		if (!Strings.isNullOrEmpty(bet)) {
			writer.sendChatMessage(commandMessage.getTargetChannel(),
					String.format("@%s dein Gebot war %s", commandMessage.getUsername(), bet));
		} else {
			writer.sendChatMessage(commandMessage.getTargetChannel(),
					String.format("@%s Du hast noch nicht geboten", commandMessage.getUsername()));
		}
	}

	private void handleFinishCommand(ITwitchChatWriter writer, CommandMessage commandMessage) {
		String winners = findWinners(commandMessage.getParameters().get(1));
		if (!Strings.isNullOrEmpty(winners)) {
			writer.sendChatMessage(commandMessage.getTargetChannel(), "Gewonnen haben: " + winners);
		} else {
			writer.sendChatMessage(commandMessage.getTargetChannel(), "Niemand hat gewonnen");
		}
	}

	private void handleSetCommand(ITwitchChatWriter writer, CommandMessage commandMessage) {
		if (stop) {
			writer.sendChatMessage(commandMessage.getTargetChannel(),
					String.format("@%s Die Wette ist bereits geschlossen", commandMessage.getUsername()));
		} else if (bets.containsKey(commandMessage.getUsername())) {
			writer.sendChatMessage(commandMessage.getTargetChannel(),
					String.format("@%s Du hast schon gewettet! Frechheit!", commandMessage.getUsername()));
		} else {
			bets.put(commandMessage.getUsername(), commandMessage.getParameters()
					.subList(1, commandMessage.getParameters().size()).stream().collect(Collectors.joining(" ")));
			writer.sendChatMessage(commandMessage.getTargetChannel(),
					String.format("@%s Dein Gebot wurde akzeptiert", commandMessage.getUsername()));
		}
	}

	private void handleStartCommand(ITwitchChatWriter writer, CommandMessage commandMessage) {
		bets.clear();
		stop = false;
		writer.sendChatMessage(commandMessage.getTargetChannel(),
				"In welchem Split stirbt Zoodles aktueller Run? Mit !bet set splitname kannst du an der Wette teilnehmen ;)");
	}

	private void handleStopCommand(ITwitchChatWriter writer, CommandMessage commandMessage) {
		stop = true;
		writer.sendChatMessage(commandMessage.getTargetChannel(), "Die Wette ist jetzt geschlossen");
	}

	private String findWinners(String string) {
		return bets.entrySet().stream().filter(e -> e.getValue().equalsIgnoreCase(string)).map(e -> e.getKey())
				.collect(Collectors.joining(", "));
	}

	@Override
	public boolean isResponsible(String commandMessage) {
		return commandMessage.equals("bet");
	}

}
