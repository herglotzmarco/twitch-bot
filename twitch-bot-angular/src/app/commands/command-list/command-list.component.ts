import { CommandsService } from './../commands.service';
import { Component, OnInit, OnDestroy } from '@angular/core';
import { Subscription } from 'rxjs';

@Component({
  selector: 'app-command-list',
  templateUrl: './command-list.component.html',
  styleUrls: ['./command-list.component.css']
})
export class CommandListComponent implements OnInit, OnDestroy {

  commands: string[] = [];
  subscription: Subscription;

  constructor(private commandsService: CommandsService) { }

  ngOnInit() {
    this.commands = this.commandsService.getCommands();
    this.subscription = this.commandsService.commandsChanged.subscribe(
      (commands: string[]) => {
        this.commands = commands;
      }
    );
  }

  ngOnDestroy() {
    this.subscription.unsubscribe();
  }

}
