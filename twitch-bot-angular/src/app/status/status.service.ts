import { Status } from './status.model';
import { Injectable } from '@angular/core';
import { Subject, Observable } from 'rxjs';
import { HttpClient } from '@angular/common/http';
import { map } from 'rxjs/operators';

@Injectable({
  providedIn: 'root'
})
export class StatusService {

  statusChanged = new Subject<Status>();

  private status: Status;

  constructor(private http: HttpClient) { }

  getStatus() {
    if (!this.status) {
      // set default, but request update from server
      this.status = Status.stopped();
      this.fetchStatus().subscribe((status: Status) => this.updateStatus(status));
    }
    return this.status;
  }

  stopServices() {
    this.http.put('http://localhost:7000/status/stop', {}).subscribe();
    this.pollUntil(Status.stopped());
  }

  startServices() {
    this.http.put('http://localhost:7000/status/start', {}).subscribe();
    this.pollUntil(Status.started());
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

  private pollUntil(target: Status) {
    setTimeout(() => {
      this.fetchStatus().subscribe((status: Status) => {
        if (status.name !== target.name) {
          this.pollUntil(target);
        }
        this.updateStatus(status);
      });
    }, 250);
  }

  private fetchStatus(): Observable<Status> {
    return this.http.get<{ status: string }>('http://localhost:7000/status')
      .pipe(map((response) => {
        return Status.forName(response.status);
      }));
  }

  private updateStatus(newStatus: Status) {
    this.status = newStatus;
    this.statusChanged.next(this.status);
  }

}
