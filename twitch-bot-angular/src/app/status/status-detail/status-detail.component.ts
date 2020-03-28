import { StatusService } from './../status.service';
import { Status } from './../status.model';
import { Component, OnInit } from '@angular/core';

@Component({
  selector: 'app-status-detail',
  templateUrl: './status-detail.component.html',
  styleUrls: ['./status-detail.component.css']
})
export class StatusDetailComponent implements OnInit {

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

}
