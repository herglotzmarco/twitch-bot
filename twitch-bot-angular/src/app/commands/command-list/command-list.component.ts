import { CommandsService } from './../commands.service';
import { Component, OnInit } from '@angular/core';
import { Command } from '../command.model';

@Component({
  selector: 'app-command-list',
  templateUrl: './command-list.component.html',
  styleUrls: ['./command-list.component.css']
})
export class CommandListComponent implements OnInit {

  commands: Command[] = [];

  constructor(private commandsService: CommandsService) { }

  ngOnInit() {
    this.commands = this.commandsService.getCommands();
    this.commandsService.commandsChanged.subscribe(
      (commands: Command[]) => {
        this.commands = commands;
      }
    );
  }

}
