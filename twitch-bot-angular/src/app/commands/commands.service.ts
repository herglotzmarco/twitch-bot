import { environment } from './../../environments/environment';
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
      this.reloadAllCommands();
    }
    return this.commands.slice();
  }

  getCommandForName(name: string) {
    if (this.commandsCache.get(name)) {
      return of(this.commandsCache.get(name));
    } else {
      return this.http.get(environment.restBase + '/commands/' + name).pipe(
        map((response: { command: string, message: string }) => {
          const result = new Command(response.command, response.message);
          this.commandsCache.set(response.command, result);
          return result;
        })
      );
    }
  }

  updateCommand(oldName: string, updatedCommand: Command) {
    this.http.put(environment.restBase + '/commands/' + oldName, { command: updatedCommand.name, message: updatedCommand.message })
      .subscribe(res => this.reloadAllCommands());
  }

  deleteCommand(name: string) {
    this.http.delete(environment.restBase + '/commands/' + name).subscribe(res => this.reloadAllCommands());
  }

  private reloadAllCommands() {
    this.http.get(environment.restBase + '/commands').subscribe((commands: string[]) => {
      this.commands = commands;
      this.commandsCache.clear();
      this.commandsChanged.next(this.getCommands());
    });
  }
}
