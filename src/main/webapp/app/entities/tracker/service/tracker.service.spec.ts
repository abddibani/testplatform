import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import * as dayjs from 'dayjs';

import { DATE_FORMAT } from 'app/config/input.constants';
import { ITracker, Tracker } from '../tracker.model';

import { TrackerService } from './tracker.service';

describe('Service Tests', () => {
  describe('Tracker Service', () => {
    let service: TrackerService;
    let httpMock: HttpTestingController;
    let elemDefault: ITracker;
    let expectedResult: ITracker | ITracker[] | boolean | null;
    let currentDate: dayjs.Dayjs;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
      });
      expectedResult = null;
      service = TestBed.inject(TrackerService);
      httpMock = TestBed.inject(HttpTestingController);
      currentDate = dayjs();

      elemDefault = {
        id: 'AAAAAAA',
        activated: false,
        createAt: currentDate,
        modifiedAt: currentDate,
        activationBegin: currentDate,
        activationEnd: currentDate,
      };
    });

    describe('Service methods', () => {
      it('should find an element', () => {
        const returnedFromService = Object.assign(
          {
            createAt: currentDate.format(DATE_FORMAT),
            modifiedAt: currentDate.format(DATE_FORMAT),
            activationBegin: currentDate.format(DATE_FORMAT),
            activationEnd: currentDate.format(DATE_FORMAT),
          },
          elemDefault
        );

        service.find('ABC').subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'GET' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(elemDefault);
      });

      it('should create a Tracker', () => {
        const returnedFromService = Object.assign(
          {
            id: 'ID',
            createAt: currentDate.format(DATE_FORMAT),
            modifiedAt: currentDate.format(DATE_FORMAT),
            activationBegin: currentDate.format(DATE_FORMAT),
            activationEnd: currentDate.format(DATE_FORMAT),
          },
          elemDefault
        );

        const expected = Object.assign(
          {
            createAt: currentDate,
            modifiedAt: currentDate,
            activationBegin: currentDate,
            activationEnd: currentDate,
          },
          returnedFromService
        );

        service.create(new Tracker()).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'POST' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should update a Tracker', () => {
        const returnedFromService = Object.assign(
          {
            id: 'BBBBBB',
            activated: true,
            createAt: currentDate.format(DATE_FORMAT),
            modifiedAt: currentDate.format(DATE_FORMAT),
            activationBegin: currentDate.format(DATE_FORMAT),
            activationEnd: currentDate.format(DATE_FORMAT),
          },
          elemDefault
        );

        const expected = Object.assign(
          {
            createAt: currentDate,
            modifiedAt: currentDate,
            activationBegin: currentDate,
            activationEnd: currentDate,
          },
          returnedFromService
        );

        service.update(expected).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'PUT' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should partial update a Tracker', () => {
        const patchObject = Object.assign(
          {
            activated: true,
            createAt: currentDate.format(DATE_FORMAT),
            modifiedAt: currentDate.format(DATE_FORMAT),
            activationBegin: currentDate.format(DATE_FORMAT),
            activationEnd: currentDate.format(DATE_FORMAT),
          },
          new Tracker()
        );

        const returnedFromService = Object.assign(patchObject, elemDefault);

        const expected = Object.assign(
          {
            createAt: currentDate,
            modifiedAt: currentDate,
            activationBegin: currentDate,
            activationEnd: currentDate,
          },
          returnedFromService
        );

        service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'PATCH' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should return a list of Tracker', () => {
        const returnedFromService = Object.assign(
          {
            id: 'BBBBBB',
            activated: true,
            createAt: currentDate.format(DATE_FORMAT),
            modifiedAt: currentDate.format(DATE_FORMAT),
            activationBegin: currentDate.format(DATE_FORMAT),
            activationEnd: currentDate.format(DATE_FORMAT),
          },
          elemDefault
        );

        const expected = Object.assign(
          {
            createAt: currentDate,
            modifiedAt: currentDate,
            activationBegin: currentDate,
            activationEnd: currentDate,
          },
          returnedFromService
        );

        service.query().subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'GET' });
        req.flush([returnedFromService]);
        httpMock.verify();
        expect(expectedResult).toContainEqual(expected);
      });

      it('should delete a Tracker', () => {
        service.delete('ABC').subscribe(resp => (expectedResult = resp.ok));

        const req = httpMock.expectOne({ method: 'DELETE' });
        req.flush({ status: 200 });
        expect(expectedResult);
      });

      describe('addTrackerToCollectionIfMissing', () => {
        it('should add a Tracker to an empty array', () => {
          const tracker: ITracker = { id: 'ABC' };
          expectedResult = service.addTrackerToCollectionIfMissing([], tracker);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(tracker);
        });

        it('should not add a Tracker to an array that contains it', () => {
          const tracker: ITracker = { id: 'ABC' };
          const trackerCollection: ITracker[] = [
            {
              ...tracker,
            },
            { id: 'CBA' },
          ];
          expectedResult = service.addTrackerToCollectionIfMissing(trackerCollection, tracker);
          expect(expectedResult).toHaveLength(2);
        });

        it("should add a Tracker to an array that doesn't contain it", () => {
          const tracker: ITracker = { id: 'ABC' };
          const trackerCollection: ITracker[] = [{ id: 'CBA' }];
          expectedResult = service.addTrackerToCollectionIfMissing(trackerCollection, tracker);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(tracker);
        });

        it('should add only unique Tracker to an array', () => {
          const trackerArray: ITracker[] = [{ id: 'ABC' }, { id: 'CBA' }, { id: '6ea3914f-3e91-4444-a328-e8cd8461d0eb' }];
          const trackerCollection: ITracker[] = [{ id: 'ABC' }];
          expectedResult = service.addTrackerToCollectionIfMissing(trackerCollection, ...trackerArray);
          expect(expectedResult).toHaveLength(3);
        });

        it('should accept varargs', () => {
          const tracker: ITracker = { id: 'ABC' };
          const tracker2: ITracker = { id: 'CBA' };
          expectedResult = service.addTrackerToCollectionIfMissing([], tracker, tracker2);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(tracker);
          expect(expectedResult).toContain(tracker2);
        });

        it('should accept null and undefined values', () => {
          const tracker: ITracker = { id: 'ABC' };
          expectedResult = service.addTrackerToCollectionIfMissing([], null, tracker, undefined);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(tracker);
        });

        it('should return initial array if no Tracker is added', () => {
          const trackerCollection: ITracker[] = [{ id: 'ABC' }];
          expectedResult = service.addTrackerToCollectionIfMissing(trackerCollection, undefined, null);
          expect(expectedResult).toEqual(trackerCollection);
        });
      });
    });

    afterEach(() => {
      httpMock.verify();
    });
  });
});
