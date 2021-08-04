import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import * as dayjs from 'dayjs';

import { isPresent } from 'app/core/util/operators';
import { DATE_FORMAT } from 'app/config/input.constants';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IExchange, getExchangeIdentifier } from '../exchange.model';

export type EntityResponseType = HttpResponse<IExchange>;
export type EntityArrayResponseType = HttpResponse<IExchange[]>;

@Injectable({ providedIn: 'root' })
export class ExchangeService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/exchanges');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(exchange: IExchange): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(exchange);
    return this.http
      .post<IExchange>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  update(exchange: IExchange): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(exchange);
    return this.http
      .put<IExchange>(`${this.resourceUrl}/${getExchangeIdentifier(exchange) as string}`, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  partialUpdate(exchange: IExchange): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(exchange);
    return this.http
      .patch<IExchange>(`${this.resourceUrl}/${getExchangeIdentifier(exchange) as string}`, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  find(id: string): Observable<EntityResponseType> {
    return this.http
      .get<IExchange>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<IExchange[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  delete(id: string): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addExchangeToCollectionIfMissing(exchangeCollection: IExchange[], ...exchangesToCheck: (IExchange | null | undefined)[]): IExchange[] {
    const exchanges: IExchange[] = exchangesToCheck.filter(isPresent);
    if (exchanges.length > 0) {
      const exchangeCollectionIdentifiers = exchangeCollection.map(exchangeItem => getExchangeIdentifier(exchangeItem)!);
      const exchangesToAdd = exchanges.filter(exchangeItem => {
        const exchangeIdentifier = getExchangeIdentifier(exchangeItem);
        if (exchangeIdentifier == null || exchangeCollectionIdentifiers.includes(exchangeIdentifier)) {
          return false;
        }
        exchangeCollectionIdentifiers.push(exchangeIdentifier);
        return true;
      });
      return [...exchangesToAdd, ...exchangeCollection];
    }
    return exchangeCollection;
  }

  protected convertDateFromClient(exchange: IExchange): IExchange {
    return Object.assign({}, exchange, {
      createAt: exchange.createAt?.isValid() ? exchange.createAt.format(DATE_FORMAT) : undefined,
      modifiedAt: exchange.modifiedAt?.isValid() ? exchange.modifiedAt.format(DATE_FORMAT) : undefined,
    });
  }

  protected convertDateFromServer(res: EntityResponseType): EntityResponseType {
    if (res.body) {
      res.body.createAt = res.body.createAt ? dayjs(res.body.createAt) : undefined;
      res.body.modifiedAt = res.body.modifiedAt ? dayjs(res.body.modifiedAt) : undefined;
    }
    return res;
  }

  protected convertDateArrayFromServer(res: EntityArrayResponseType): EntityArrayResponseType {
    if (res.body) {
      res.body.forEach((exchange: IExchange) => {
        exchange.createAt = exchange.createAt ? dayjs(exchange.createAt) : undefined;
        exchange.modifiedAt = exchange.modifiedAt ? dayjs(exchange.modifiedAt) : undefined;
      });
    }
    return res;
  }
}
