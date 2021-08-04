import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import * as dayjs from 'dayjs';

import { DATE_FORMAT } from 'app/config/input.constants';
import { IExchange, Exchange } from '../exchange.model';

import { ExchangeService } from './exchange.service';

describe('Service Tests', () => {
  describe('Exchange Service', () => {
    let service: ExchangeService;
    let httpMock: HttpTestingController;
    let elemDefault: IExchange;
    let expectedResult: IExchange | IExchange[] | boolean | null;
    let currentDate: dayjs.Dayjs;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
      });
      expectedResult = null;
      service = TestBed.inject(ExchangeService);
      httpMock = TestBed.inject(HttpTestingController);
      currentDate = dayjs();

      elemDefault = {
        id: 'AAAAAAA',
        name: 'AAAAAAA',
        apikey: 'AAAAAAA',
        secrit: 'AAAAAAA',
        activated: false,
        createAt: currentDate,
        modifiedAt: currentDate,
      };
    });

    describe('Service methods', () => {
      it('should find an element', () => {
        const returnedFromService = Object.assign(
          {
            createAt: currentDate.format(DATE_FORMAT),
            modifiedAt: currentDate.format(DATE_FORMAT),
          },
          elemDefault
        );

        service.find('ABC').subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'GET' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(elemDefault);
      });

      it('should create a Exchange', () => {
        const returnedFromService = Object.assign(
          {
            id: 'ID',
            createAt: currentDate.format(DATE_FORMAT),
            modifiedAt: currentDate.format(DATE_FORMAT),
          },
          elemDefault
        );

        const expected = Object.assign(
          {
            createAt: currentDate,
            modifiedAt: currentDate,
          },
          returnedFromService
        );

        service.create(new Exchange()).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'POST' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should update a Exchange', () => {
        const returnedFromService = Object.assign(
          {
            id: 'BBBBBB',
            name: 'BBBBBB',
            apikey: 'BBBBBB',
            secrit: 'BBBBBB',
            activated: true,
            createAt: currentDate.format(DATE_FORMAT),
            modifiedAt: currentDate.format(DATE_FORMAT),
          },
          elemDefault
        );

        const expected = Object.assign(
          {
            createAt: currentDate,
            modifiedAt: currentDate,
          },
          returnedFromService
        );

        service.update(expected).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'PUT' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should partial update a Exchange', () => {
        const patchObject = Object.assign(
          {
            apikey: 'BBBBBB',
            activated: true,
            modifiedAt: currentDate.format(DATE_FORMAT),
          },
          new Exchange()
        );

        const returnedFromService = Object.assign(patchObject, elemDefault);

        const expected = Object.assign(
          {
            createAt: currentDate,
            modifiedAt: currentDate,
          },
          returnedFromService
        );

        service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'PATCH' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should return a list of Exchange', () => {
        const returnedFromService = Object.assign(
          {
            id: 'BBBBBB',
            name: 'BBBBBB',
            apikey: 'BBBBBB',
            secrit: 'BBBBBB',
            activated: true,
            createAt: currentDate.format(DATE_FORMAT),
            modifiedAt: currentDate.format(DATE_FORMAT),
          },
          elemDefault
        );

        const expected = Object.assign(
          {
            createAt: currentDate,
            modifiedAt: currentDate,
          },
          returnedFromService
        );

        service.query().subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'GET' });
        req.flush([returnedFromService]);
        httpMock.verify();
        expect(expectedResult).toContainEqual(expected);
      });

      it('should delete a Exchange', () => {
        service.delete('ABC').subscribe(resp => (expectedResult = resp.ok));

        const req = httpMock.expectOne({ method: 'DELETE' });
        req.flush({ status: 200 });
        expect(expectedResult);
      });

      describe('addExchangeToCollectionIfMissing', () => {
        it('should add a Exchange to an empty array', () => {
          const exchange: IExchange = { id: 'ABC' };
          expectedResult = service.addExchangeToCollectionIfMissing([], exchange);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(exchange);
        });

        it('should not add a Exchange to an array that contains it', () => {
          const exchange: IExchange = { id: 'ABC' };
          const exchangeCollection: IExchange[] = [
            {
              ...exchange,
            },
            { id: 'CBA' },
          ];
          expectedResult = service.addExchangeToCollectionIfMissing(exchangeCollection, exchange);
          expect(expectedResult).toHaveLength(2);
        });

        it("should add a Exchange to an array that doesn't contain it", () => {
          const exchange: IExchange = { id: 'ABC' };
          const exchangeCollection: IExchange[] = [{ id: 'CBA' }];
          expectedResult = service.addExchangeToCollectionIfMissing(exchangeCollection, exchange);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(exchange);
        });

        it('should add only unique Exchange to an array', () => {
          const exchangeArray: IExchange[] = [{ id: 'ABC' }, { id: 'CBA' }, { id: '71908884-5530-43d8-a93a-415b49818751' }];
          const exchangeCollection: IExchange[] = [{ id: 'ABC' }];
          expectedResult = service.addExchangeToCollectionIfMissing(exchangeCollection, ...exchangeArray);
          expect(expectedResult).toHaveLength(3);
        });

        it('should accept varargs', () => {
          const exchange: IExchange = { id: 'ABC' };
          const exchange2: IExchange = { id: 'CBA' };
          expectedResult = service.addExchangeToCollectionIfMissing([], exchange, exchange2);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(exchange);
          expect(expectedResult).toContain(exchange2);
        });

        it('should accept null and undefined values', () => {
          const exchange: IExchange = { id: 'ABC' };
          expectedResult = service.addExchangeToCollectionIfMissing([], null, exchange, undefined);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(exchange);
        });

        it('should return initial array if no Exchange is added', () => {
          const exchangeCollection: IExchange[] = [{ id: 'ABC' }];
          expectedResult = service.addExchangeToCollectionIfMissing(exchangeCollection, undefined, null);
          expect(expectedResult).toEqual(exchangeCollection);
        });
      });
    });

    afterEach(() => {
      httpMock.verify();
    });
  });
});
