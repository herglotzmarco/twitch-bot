import { Subscription } from 'rxjs';
import { Component, OnInit, OnDestroy } from '@angular/core';

import { StatusService } from './../status.service';
import { Status } from '../status.model';

@Component({
  selector: 'app-status-actions',
  templateUrl: './status-actions.component.html',
  styleUrls: ['./status-actions.component.css']
})
export class StatusActionsComponent implements OnInit, OnDestroy {

  status: Status;
  subscription: Subscription;

  constructor(private statusService: StatusService) { }

  ngOnInit() {
    this.status = this.statusService.getStatus();
    this.subscription = this.statusService.statusChanged.subscribe(
      (status: Status) => {
        this.status = status;
      }
    );
  }

  ngOnDestroy() {
    this.subscription.unsubscribe();
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
