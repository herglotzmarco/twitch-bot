import { Component, Input, OnChanges, SimpleChanges } from '@angular/core';
import { Command } from '../command.model';
import { CommandsService } from '../commands.service';

@Component({
  selector: 'app-command-detail',
  templateUrl: './command-detail.component.html',
  styleUrls: ['./command-detail.component.css']
})
export class CommandDetailComponent implements OnChanges {

  @Input() selectedCommand: Command;

  commandName: string;
  commandMessage: string;

  constructor(private commandsService: CommandsService) { }

  ngOnChanges(changes: SimpleChanges) {
    this.commandName = changes.selectedCommand.currentValue.name;
    this.commandMessage = changes.selectedCommand.currentValue.message;
  }

  onSave() {
    this.commandsService.updateCommand(this.selectedCommand, this.commandName, this.commandMessage);
  }

  onCancel() {
    this.commandName = this.selectedCommand.name;
    this.commandMessage = this.selectedCommand.message;
  }

}
