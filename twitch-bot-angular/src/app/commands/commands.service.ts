import { Injectable } from '@angular/core';
import { Command } from './command.model';
import { Subject, of } from 'rxjs';
import { HttpClient } from '@angular/common/http';
import { map } from 'rxjs/operators';

@Injectable({
  providedIn: 'root'
})
export class CommandsService {

  private commands: string[];
  private commandsCache = new Map<string, Command>();

  commandsChanged = new Subject<string[]>();

  constructor(private http: HttpClient) { }

  getCommands() {
    if (!this.commands) {
      this.commands = [];
      this.http.get('http://localhost:7000/commands').subscribe((commands: string[]) => {
        this.commands = commands;
        this.commandsChanged.next(this.getCommands());
      });
    }
    return this.commands.slice();
  }

  getCommandForName(name: string) {
    if (this.commandsCache.get(name)) {
      return of(this.commandsCache.get(name));
    } else {
      return this.http.get('http://localhost:7000/commands/' + name).pipe(
        map((response: { command: string, message: string }) => {
          const result = new Command(response.command, response.message);
          this.commandsCache.set(response.command, result);
          return result;
        })
      );
    }
  }

  updateCommand(oldName: string, updatedCommand: Command) {
    this.commandsCache.delete(oldName);
    const index = this.commands.indexOf(oldName);
    this.commands[index] = updatedCommand.name;
    this.commandsChanged.next(this.getCommands());
  }

  deleteCommand(name: string) {
    this.commandsCache.delete(name);
    const index = this.commands.indexOf(name);
    this.commands.splice(index, 1);
    this.commandsChanged.next(this.getCommands());
  }
}
