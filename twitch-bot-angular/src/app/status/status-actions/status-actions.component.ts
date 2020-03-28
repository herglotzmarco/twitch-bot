import { Component, OnInit } from '@angular/core';

import { StatusService } from './../status.service';
import { Status } from '../status.model';

@Component({
  selector: 'app-status-actions',
  templateUrl: './status-actions.component.html',
  styleUrls: ['./status-actions.component.css']
})
export class StatusActionsComponent implements OnInit {

  status: Status;

  constructor(private statusService: StatusService) { }

  ngOnInit() {
    this.status = this.statusService.getStatus();
    this.statusService.statusChanged.subscribe(
      (status: Status) => {
        this.status = status;
      }
    );
  }

  canStart() {
    return this.status.name === Status.stopped().name;
  }

  canStop() {
    return this.status.name === Status.started().name;
  }

  canRestart() {
    return this.canStop();
  }

  onStart() {
    this.statusService.startServices();
  }

  onStop() {
    this.statusService.stopServices();
  }

  onRestart() {
    this.statusService.restartServices();
  }

}
