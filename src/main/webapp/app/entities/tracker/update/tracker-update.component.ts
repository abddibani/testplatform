import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { ITracker, Tracker } from '../tracker.model';
import { TrackerService } from '../service/tracker.service';
import { IClient } from 'app/entities/client/client.model';
import { ClientService } from 'app/entities/client/service/client.service';

@Component({
  selector: 'jhi-tracker-update',
  templateUrl: './tracker-update.component.html',
})
export class TrackerUpdateComponent implements OnInit {
  isSaving = false;

  clientsSharedCollection: IClient[] = [];

  editForm = this.fb.group({
    id: [],
    activated: [],
    createAt: [],
    modifiedAt: [],
    activationBegin: [],
    activationEnd: [],
    client: [],
  });

  constructor(
    protected trackerService: TrackerService,
    protected clientService: ClientService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ tracker }) => {
      this.updateForm(tracker);

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const tracker = this.createFromForm();
    if (tracker.id !== undefined) {
      this.subscribeToSaveResponse(this.trackerService.update(tracker));
    } else {
      this.subscribeToSaveResponse(this.trackerService.create(tracker));
    }
  }

  trackClientById(index: number, item: IClient): string {
    return item.id!;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ITracker>>): void {
    result.pipe(finalize(() => this.onSaveFinalize())).subscribe(
      () => this.onSaveSuccess(),
      () => this.onSaveError()
    );
  }

  protected onSaveSuccess(): void {
    this.previousState();
  }

  protected onSaveError(): void {
    // Api for inheritance.
  }

  protected onSaveFinalize(): void {
    this.isSaving = false;
  }

  protected updateForm(tracker: ITracker): void {
    this.editForm.patchValue({
      id: tracker.id,
      activated: tracker.activated,
      createAt: tracker.createAt,
      modifiedAt: tracker.modifiedAt,
      activationBegin: tracker.activationBegin,
      activationEnd: tracker.activationEnd,
      client: tracker.client,
    });

    this.clientsSharedCollection = this.clientService.addClientToCollectionIfMissing(this.clientsSharedCollection, tracker.client);
  }

  protected loadRelationshipsOptions(): void {
    this.clientService
      .query()
      .pipe(map((res: HttpResponse<IClient[]>) => res.body ?? []))
      .pipe(map((clients: IClient[]) => this.clientService.addClientToCollectionIfMissing(clients, this.editForm.get('client')!.value)))
      .subscribe((clients: IClient[]) => (this.clientsSharedCollection = clients));
  }

  protected createFromForm(): ITracker {
    return {
      ...new Tracker(),
      id: this.editForm.get(['id'])!.value,
      activated: this.editForm.get(['activated'])!.value,
      createAt: this.editForm.get(['createAt'])!.value,
      modifiedAt: this.editForm.get(['modifiedAt'])!.value,
      activationBegin: this.editForm.get(['activationBegin'])!.value,
      activationEnd: this.editForm.get(['activationEnd'])!.value,
      client: this.editForm.get(['client'])!.value,
    };
  }
}
