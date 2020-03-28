import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Params } from '@angular/router';

import { Command } from '../command.model';
import { CommandsService } from '../commands.service';

@Component({
  selector: 'app-command-detail',
  templateUrl: './command-detail.component.html',
  styleUrls: ['./command-detail.component.css']
})
export class CommandDetailComponent implements OnInit {

  command: Command;

  commandNameInput: string;
  commandMessageInput: string;

  constructor(private commandsService: CommandsService, private route: ActivatedRoute) { }

  ngOnInit() {
    this.route.params.subscribe(
      (params: Params) => {
        this.command = this.commandsService.getCommandForName(params.name);
        this.onCancel();
      }
    );
  }

  onSave() {
    this.commandsService.updateCommand(this.command.name, this.commandNameInput, this.commandMessageInput);
  }

  onCancel() {
    this.commandNameInput = this.command.name;
    this.commandMessageInput = this.command.message;
  }

}
