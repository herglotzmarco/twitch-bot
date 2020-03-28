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

  commandsChanged: EventEmitter<Command[]> = new EventEmitter<Command[]>();

  getCommands() {
    return this.commands.slice();
  }

  getCommandForName(name: string) {
    return this.getCommands().find(c => c.name === name);
  }

  updateCommand(oldName: string, newName: string, message: string) {
    const affectedCommand = this.getCommandForName(oldName);
    affectedCommand.name = newName;
    affectedCommand.message = message;
    this.commandsChanged.emit(this.getCommands());
  }
}
