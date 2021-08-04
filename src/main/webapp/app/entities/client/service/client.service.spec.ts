import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { IClient, Client } from '../client.model';

import { ClientService } from './client.service';

describe('Service Tests', () => {
  describe('Client Service', () => {
    let service: ClientService;
    let httpMock: HttpTestingController;
    let elemDefault: IClient;
    let expectedResult: IClient | IClient[] | boolean | null;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
      });
      expectedResult = null;
      service = TestBed.inject(ClientService);
      httpMock = TestBed.inject(HttpTestingController);

      elemDefault = {
        id: 'AAAAAAA',
        email: 'AAAAAAA',
        username: 'AAAAAAA',
        password: 'AAAAAAA',
        activated: false,
      };
    });

    describe('Service methods', () => {
      it('should find an element', () => {
        const returnedFromService = Object.assign({}, elemDefault);

        service.find('ABC').subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'GET' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(elemDefault);
      });

      it('should create a Client', () => {
        const returnedFromService = Object.assign(
          {
            id: 'ID',
          },
          elemDefault
        );

        const expected = Object.assign({}, returnedFromService);

        service.create(new Client()).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'POST' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should update a Client', () => {
        const returnedFromService = Object.assign(
          {
            id: 'BBBBBB',
            email: 'BBBBBB',
            username: 'BBBBBB',
            password: 'BBBBBB',
            activated: true,
          },
          elemDefault
        );

        const expected = Object.assign({}, returnedFromService);

        service.update(expected).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'PUT' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should partial update a Client', () => {
        const patchObject = Object.assign(
          {
            username: 'BBBBBB',
            password: 'BBBBBB',
            activated: true,
          },
          new Client()
        );

        const returnedFromService = Object.assign(patchObject, elemDefault);

        const expected = Object.assign({}, returnedFromService);

        service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'PATCH' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should return a list of Client', () => {
        const returnedFromService = Object.assign(
          {
            id: 'BBBBBB',
            email: 'BBBBBB',
            username: 'BBBBBB',
            password: 'BBBBBB',
            activated: true,
          },
          elemDefault
        );

        const expected = Object.assign({}, returnedFromService);

        service.query().subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'GET' });
        req.flush([returnedFromService]);
        httpMock.verify();
        expect(expectedResult).toContainEqual(expected);
      });

      it('should delete a Client', () => {
        service.delete('ABC').subscribe(resp => (expectedResult = resp.ok));

        const req = httpMock.expectOne({ method: 'DELETE' });
        req.flush({ status: 200 });
        expect(expectedResult);
      });

      describe('addClientToCollectionIfMissing', () => {
        it('should add a Client to an empty array', () => {
          const client: IClient = { id: 'ABC' };
          expectedResult = service.addClientToCollectionIfMissing([], client);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(client);
        });

        it('should not add a Client to an array that contains it', () => {
          const client: IClient = { id: 'ABC' };
          const clientCollection: IClient[] = [
            {
              ...client,
            },
            { id: 'CBA' },
          ];
          expectedResult = service.addClientToCollectionIfMissing(clientCollection, client);
          expect(expectedResult).toHaveLength(2);
        });

        it("should add a Client to an array that doesn't contain it", () => {
          const client: IClient = { id: 'ABC' };
          const clientCollection: IClient[] = [{ id: 'CBA' }];
          expectedResult = service.addClientToCollectionIfMissing(clientCollection, client);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(client);
        });

        it('should add only unique Client to an array', () => {
          const clientArray: IClient[] = [{ id: 'ABC' }, { id: 'CBA' }, { id: 'a836d419-4552-42d0-b4e3-d2854862c30a' }];
          const clientCollection: IClient[] = [{ id: 'ABC' }];
          expectedResult = service.addClientToCollectionIfMissing(clientCollection, ...clientArray);
          expect(expectedResult).toHaveLength(3);
        });

        it('should accept varargs', () => {
          const client: IClient = { id: 'ABC' };
          const client2: IClient = { id: 'CBA' };
          expectedResult = service.addClientToCollectionIfMissing([], client, client2);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(client);
          expect(expectedResult).toContain(client2);
        });

        it('should accept null and undefined values', () => {
          const client: IClient = { id: 'ABC' };
          expectedResult = service.addClientToCollectionIfMissing([], null, client, undefined);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(client);
        });

        it('should return initial array if no Client is added', () => {
          const clientCollection: IClient[] = [{ id: 'ABC' }];
          expectedResult = service.addClientToCollectionIfMissing(clientCollection, undefined, null);
          expect(expectedResult).toEqual(clientCollection);
        });
      });
    });

    afterEach(() => {
      httpMock.verify();
    });
  });
});
