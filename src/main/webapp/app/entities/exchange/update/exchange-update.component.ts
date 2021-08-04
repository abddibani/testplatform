import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { IExchange, Exchange } from '../exchange.model';
import { ExchangeService } from '../service/exchange.service';
import { IClient } from 'app/entities/client/client.model';
import { ClientService } from 'app/entities/client/service/client.service';

@Component({
  selector: 'jhi-exchange-update',
  templateUrl: './exchange-update.component.html',
})
export class ExchangeUpdateComponent implements OnInit {
  isSaving = false;

  clientsSharedCollection: IClient[] = [];

  editForm = this.fb.group({
    id: [],
    name: [],
    apikey: [],
    secrit: [],
    activated: [],
    createAt: [],
    modifiedAt: [],
    client: [],
  });

  constructor(
    protected exchangeService: ExchangeService,
    protected clientService: ClientService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ exchange }) => {
      this.updateForm(exchange);

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const exchange = this.createFromForm();
    if (exchange.id !== undefined) {
      this.subscribeToSaveResponse(this.exchangeService.update(exchange));
    } else {
      this.subscribeToSaveResponse(this.exchangeService.create(exchange));
    }
  }

  trackClientById(index: number, item: IClient): string {
    return item.id!;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IExchange>>): void {
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

  protected updateForm(exchange: IExchange): void {
    this.editForm.patchValue({
      id: exchange.id,
      name: exchange.name,
      apikey: exchange.apikey,
      secrit: exchange.secrit,
      activated: exchange.activated,
      createAt: exchange.createAt,
      modifiedAt: exchange.modifiedAt,
      client: exchange.client,
    });

    this.clientsSharedCollection = this.clientService.addClientToCollectionIfMissing(this.clientsSharedCollection, exchange.client);
  }

  protected loadRelationshipsOptions(): void {
    this.clientService
      .query()
      .pipe(map((res: HttpResponse<IClient[]>) => res.body ?? []))
      .pipe(map((clients: IClient[]) => this.clientService.addClientToCollectionIfMissing(clients, this.editForm.get('client')!.value)))
      .subscribe((clients: IClient[]) => (this.clientsSharedCollection = clients));
  }

  protected createFromForm(): IExchange {
    return {
      ...new Exchange(),
      id: this.editForm.get(['id'])!.value,
      name: this.editForm.get(['name'])!.value,
      apikey: this.editForm.get(['apikey'])!.value,
      secrit: this.editForm.get(['secrit'])!.value,
      activated: this.editForm.get(['activated'])!.value,
      createAt: this.editForm.get(['createAt'])!.value,
      modifiedAt: this.editForm.get(['modifiedAt'])!.value,
      client: this.editForm.get(['client'])!.value,
    };
  }
}
