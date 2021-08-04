import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import * as dayjs from 'dayjs';

import { isPresent } from 'app/core/util/operators';
import { DATE_FORMAT } from 'app/config/input.constants';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { ITracker, getTrackerIdentifier } from '../tracker.model';

export type EntityResponseType = HttpResponse<ITracker>;
export type EntityArrayResponseType = HttpResponse<ITracker[]>;

@Injectable({ providedIn: 'root' })
export class TrackerService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/trackers');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(tracker: ITracker): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(tracker);
    return this.http
      .post<ITracker>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  update(tracker: ITracker): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(tracker);
    return this.http
      .put<ITracker>(`${this.resourceUrl}/${getTrackerIdentifier(tracker) as string}`, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  partialUpdate(tracker: ITracker): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(tracker);
    return this.http
      .patch<ITracker>(`${this.resourceUrl}/${getTrackerIdentifier(tracker) as string}`, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  find(id: string): Observable<EntityResponseType> {
    return this.http
      .get<ITracker>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<ITracker[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  delete(id: string): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addTrackerToCollectionIfMissing(trackerCollection: ITracker[], ...trackersToCheck: (ITracker | null | undefined)[]): ITracker[] {
    const trackers: ITracker[] = trackersToCheck.filter(isPresent);
    if (trackers.length > 0) {
      const trackerCollectionIdentifiers = trackerCollection.map(trackerItem => getTrackerIdentifier(trackerItem)!);
      const trackersToAdd = trackers.filter(trackerItem => {
        const trackerIdentifier = getTrackerIdentifier(trackerItem);
        if (trackerIdentifier == null || trackerCollectionIdentifiers.includes(trackerIdentifier)) {
          return false;
        }
        trackerCollectionIdentifiers.push(trackerIdentifier);
        return true;
      });
      return [...trackersToAdd, ...trackerCollection];
    }
    return trackerCollection;
  }

  protected convertDateFromClient(tracker: ITracker): ITracker {
    return Object.assign({}, tracker, {
      createAt: tracker.createAt?.isValid() ? tracker.createAt.format(DATE_FORMAT) : undefined,
      modifiedAt: tracker.modifiedAt?.isValid() ? tracker.modifiedAt.format(DATE_FORMAT) : undefined,
      activationBegin: tracker.activationBegin?.isValid() ? tracker.activationBegin.format(DATE_FORMAT) : undefined,
      activationEnd: tracker.activationEnd?.isValid() ? tracker.activationEnd.format(DATE_FORMAT) : undefined,
    });
  }

  protected convertDateFromServer(res: EntityResponseType): EntityResponseType {
    if (res.body) {
      res.body.createAt = res.body.createAt ? dayjs(res.body.createAt) : undefined;
      res.body.modifiedAt = res.body.modifiedAt ? dayjs(res.body.modifiedAt) : undefined;
      res.body.activationBegin = res.body.activationBegin ? dayjs(res.body.activationBegin) : undefined;
      res.body.activationEnd = res.body.activationEnd ? dayjs(res.body.activationEnd) : undefined;
    }
    return res;
  }

  protected convertDateArrayFromServer(res: EntityArrayResponseType): EntityArrayResponseType {
    if (res.body) {
      res.body.forEach((tracker: ITracker) => {
        tracker.createAt = tracker.createAt ? dayjs(tracker.createAt) : undefined;
        tracker.modifiedAt = tracker.modifiedAt ? dayjs(tracker.modifiedAt) : undefined;
        tracker.activationBegin = tracker.activationBegin ? dayjs(tracker.activationBegin) : undefined;
        tracker.activationEnd = tracker.activationEnd ? dayjs(tracker.activationEnd) : undefined;
      });
    }
    return res;
  }
}
