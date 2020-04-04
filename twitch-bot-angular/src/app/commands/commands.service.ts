import { Injectable } from '@angular/core';
import { Command } from './command.model';
import { Subject } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class CommandsService {

  private commands: Command[] = [
    new Command('sonorous', 'sonorous test'),
    new Command('discord', 'discord test'),
    new Command('songrequest', 'songrequest test')
  ];

  commandsChanged = new Subject<Command[]>();

  getCommands() {
    return this.commands.slice();
  }

  getCommandForName(name: string) {
    return this.getCommands().find(c => c.name === name);
  }

  updateCommand(oldName: string, updatedCommand: Command) {
    const index = this.getCommandIndexForName(oldName);
    this.commands[index] = updatedCommand;
    this.commandsChanged.next(this.getCommands());
  }

  deleteCommand(name: string) {
    const index = this.getCommandIndexForName(name);
    this.commands.splice(index, 1);
    this.commandsChanged.next(this.getCommands());
  }

  private getCommandIndexForName(name: string): number {
    return this.getCommands().findIndex(c => c.name === name);
  }

}
