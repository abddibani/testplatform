import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { IExchange } from '../exchange.model';
import { ExchangeService } from '../service/exchange.service';
import { ExchangeDeleteDialogComponent } from '../delete/exchange-delete-dialog.component';

@Component({
  selector: 'jhi-exchange',
  templateUrl: './exchange.component.html',
})
export class ExchangeComponent implements OnInit {
  exchanges?: IExchange[];
  isLoading = false;

  constructor(protected exchangeService: ExchangeService, protected modalService: NgbModal) {}

  loadAll(): void {
    this.isLoading = true;

    this.exchangeService.query().subscribe(
      (res: HttpResponse<IExchange[]>) => {
        this.isLoading = false;
        this.exchanges = res.body ?? [];
      },
      () => {
        this.isLoading = false;
      }
    );
  }

  ngOnInit(): void {
    this.loadAll();
  }

  trackId(index: number, item: IExchange): string {
    return item.id!;
  }

  delete(exchange: IExchange): void {
    const modalRef = this.modalService.open(ExchangeDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.exchange = exchange;
    // unsubscribe not needed because closed completes on modal close
    modalRef.closed.subscribe(reason => {
      if (reason === 'deleted') {
        this.loadAll();
      }
    });
  }
}
