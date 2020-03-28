import { Command } from './../../command.model';
import { Component, Input } from '@angular/core';

@Component({
  selector: 'app-command-list-item',
  templateUrl: './command-list-item.component.html',
  styleUrls: ['./command-list-item.component.css']
})
export class CommandListItemComponent {

  @Input() command: Command;

}
