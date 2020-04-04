import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Params, Router } from '@angular/router';

import { Command } from '../command.model';
import { CommandsService } from '../commands.service';
import { NgForm, FormGroup, FormControl, Validators, ValidationErrors } from '@angular/forms';

@Component({
  selector: 'app-command-detail',
  templateUrl: './command-detail.component.html',
  styleUrls: ['./command-detail.component.css']
})
export class CommandDetailComponent implements OnInit {

  command: Command;
  form: FormGroup;

  constructor(private commandsService: CommandsService, private route: ActivatedRoute, private router: Router) { }

  ngOnInit() {
    this.route.params.subscribe(
      (params: Params) => {
        this.command = this.commandsService.getCommandForName(params.name);
        this.initForm();
      }
    );
  }

  private initForm() {
    this.form = new FormGroup({
      name: new FormControl(this.command.name, [Validators.required, this.commandNameUnique.bind(this)]),
      message: new FormControl(this.command.message, Validators.required)
    });
  }

  private commandNameUnique(control: FormControl): ValidationErrors | null {
    if (control.value !== this.command.name && this.commandsService.getCommandForName(control.value)) {
      return { commandNameUnique: 'false' };
    }
    return null;
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
