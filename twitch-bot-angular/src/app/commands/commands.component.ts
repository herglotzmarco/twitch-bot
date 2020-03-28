import { Command } from './command.model';
import { CommandsService } from './commands.service';
import { Component, OnInit } from '@angular/core';

@Component({
  selector: 'app-commands',
  templateUrl: './commands.component.html',
  styleUrls: ['./commands.component.css']
})
export class CommandsComponent implements OnInit {

  selectedCommand: Command;

  constructor(private commandsService: CommandsService) { }

  ngOnInit() {
    this.commandsService.commandSelected.subscribe(
      (selectedCommand: Command) => {
        this.selectedCommand = selectedCommand;
      }
    );
  }
}
