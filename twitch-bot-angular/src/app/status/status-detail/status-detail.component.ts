import { Subscription } from 'rxjs';
import { StatusService } from './../status.service';
import { Status } from './../status.model';
import { Component, OnInit, OnDestroy } from '@angular/core';

@Component({
  selector: 'app-status-detail',
  templateUrl: './status-detail.component.html',
  styleUrls: ['./status-detail.component.css']
})
export class StatusDetailComponent implements OnInit, OnDestroy {

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

}
