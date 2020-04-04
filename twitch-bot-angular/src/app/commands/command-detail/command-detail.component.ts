import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Params, Router } from '@angular/router';

import { Command } from '../command.model';
import { CommandsService } from '../commands.service';
import { FormGroup, FormControl, Validators } from '@angular/forms';
import { flatMap, catchError, map, switchMap } from 'rxjs/operators';
import { of, timer } from 'rxjs';

@Component({
  selector: 'app-command-detail',
  templateUrl: './command-detail.component.html',
  styleUrls: ['./command-detail.component.css']
})
export class CommandDetailComponent implements OnInit {

  form: FormGroup;

  private command: Command;

  constructor(private commandsService: CommandsService, private route: ActivatedRoute, private router: Router) { }

  ngOnInit() {
    this.initForm();
    this.route.params.pipe(
      flatMap((params: Params) => this.commandsService.getCommandForName(params.name))
    ).subscribe(
      (command: Command) => {
        this.command = command;
        this.onReset();
      }
    );
  }

  private initForm() {
    this.form = new FormGroup({
      name: new FormControl('', Validators.required, this.commandNameUnique.bind(this)),
      message: new FormControl('', Validators.required)
    });
  }

  private commandNameUnique(control: FormControl) {
    if (!control.value || control.value === this.command.name) {
      return of(null);
    }
    return timer(500).pipe(
      switchMap(() => this.commandsService.getCommandForName(control.value).pipe(
        catchError(error => of(null)),
        map(command => {
          if (command) {
            return { commandNameUnique: 'false' };
          } else {
            return null;
          }
        })
      )));
  }

  onSubmit() {
    this.commandsService.updateCommand(this.command.name, this.form.value);
    this.router.navigate(['..'], { relativeTo: this.route });
  }

  onReset() {
    this.form.reset(this.command);
  }

  onDelete() {
    this.commandsService.deleteCommand(this.command.name);
    this.router.navigate(['..'], { relativeTo: this.route });
  }

}
