import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { ITracker } from '../tracker.model';
import { TrackerService } from '../service/tracker.service';
import { TrackerDeleteDialogComponent } from '../delete/tracker-delete-dialog.component';

@Component({
  selector: 'jhi-tracker',
  templateUrl: './tracker.component.html',
})
export class TrackerComponent implements OnInit {
  trackers?: ITracker[];
  isLoading = false;

  constructor(protected trackerService: TrackerService, protected modalService: NgbModal) {}

  loadAll(): void {
    this.isLoading = true;

    this.trackerService.query().subscribe(
      (res: HttpResponse<ITracker[]>) => {
        this.isLoading = false;
        this.trackers = res.body ?? [];
      },
      () => {
        this.isLoading = false;
      }
    );
  }

  ngOnInit(): void {
    this.loadAll();
  }

  trackId(index: number, item: ITracker): string {
    return item.id!;
  }

  delete(tracker: ITracker): void {
    const modalRef = this.modalService.open(TrackerDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.tracker = tracker;
    // unsubscribe not needed because closed completes on modal close
    modalRef.closed.subscribe(reason => {
      if (reason === 'deleted') {
        this.loadAll();
      }
    });
  }
}
