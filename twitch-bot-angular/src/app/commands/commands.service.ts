import { Injectable, EventEmitter } from '@angular/core';
import { Command } from './command.model';

@Injectable({
  providedIn: 'root'
})
export class CommandsService {

  private commands: Command[] = [
    new Command('sonorous', 'sonorous test'),
    new Command('discord', 'discord test'),
    new Command('songrequest', 'songrequest test')
  ];

  commandSelected: EventEmitter<Command> = new EventEmitter<Command>();
  commandsChanged: EventEmitter<Command[]> = new EventEmitter<Command[]>();

  getCommands() {
    return this.commands.slice();
  }

  updateCommand(command: Command, commandName: string, commandMessage: string) {
    const index = this.commands.indexOf(command);
    this.commands[index].name = commandName;
    this.commands[index].message = commandMessage;
    this.commandsChanged.emit(this.getCommands());
  }
}
