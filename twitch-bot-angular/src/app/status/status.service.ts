import { Status } from './status.model';
import { Injectable, EventEmitter } from '@angular/core';

@Injectable({
  providedIn: 'root'
})
export class StatusService {

  statusChanged: EventEmitter<Status> = new EventEmitter<Status>();

  private status: Status = Status.stopped();

  getStatus() {
    return this.status;
  }

  stopServices() {
    this.updateStatus(Status.stopping());
    setTimeout(() => {
      this.updateStatus(Status.stopped());
    }, 1000);
  }

  startServices() {
    this.updateStatus(Status.starting());
    setTimeout(() => {
      this.updateStatus(Status.started());
    }, 1000);
  }

  restartServices() {
    const subscription = this.statusChanged.subscribe(
      (status) => {
        if (status.name === Status.stopped().name) {
          this.startServices();
          subscription.unsubscribe();
        }
      }
    );
    this.stopServices();
  }

  private updateStatus(newStatus: Status) {
    this.status = newStatus;
    this.statusChanged.emit(this.status);
  }

}
