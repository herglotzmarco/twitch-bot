import { Command } from './../../command.model';
import { Component, Input } from '@angular/core';
import { CommandsService } from '../../commands.service';

@Component({
  selector: 'app-command-list-item',
  templateUrl: './command-list-item.component.html',
  styleUrls: ['./command-list-item.component.css']
})
export class CommandListItemComponent {

  @Input() command: Command;

  constructor(private commandsService: CommandsService) { }

  onSelectCommand() {
    this.commandsService.commandSelected.emit(this.command);
  }

}
