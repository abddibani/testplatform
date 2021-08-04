import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import * as dayjs from 'dayjs';

import { DATE_FORMAT } from 'app/config/input.constants';
import { ICustomer, Customer } from '../customer.model';

import { CustomerService } from './customer.service';

describe('Service Tests', () => {
  describe('Customer Service', () => {
    let service: CustomerService;
    let httpMock: HttpTestingController;
    let elemDefault: ICustomer;
    let expectedResult: ICustomer | ICustomer[] | boolean | null;
    let currentDate: dayjs.Dayjs;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
      });
      expectedResult = null;
      service = TestBed.inject(CustomerService);
      httpMock = TestBed.inject(HttpTestingController);
      currentDate = dayjs();

      elemDefault = {
        id: 'AAAAAAA',
        email: 'AAAAAAA',
        lastname: 'AAAAAAA',
        firstname: 'AAAAAAA',
        phone: 'AAAAAAA',
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

      it('should create a Customer', () => {
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

        service.create(new Customer()).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'POST' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should update a Customer', () => {
        const returnedFromService = Object.assign(
          {
            id: 'BBBBBB',
            email: 'BBBBBB',
            lastname: 'BBBBBB',
            firstname: 'BBBBBB',
            phone: 'BBBBBB',
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

      it('should partial update a Customer', () => {
        const patchObject = Object.assign(
          {
            email: 'BBBBBB',
            firstname: 'BBBBBB',
            createAt: currentDate.format(DATE_FORMAT),
            modifiedAt: currentDate.format(DATE_FORMAT),
          },
          new Customer()
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

      it('should return a list of Customer', () => {
        const returnedFromService = Object.assign(
          {
            id: 'BBBBBB',
            email: 'BBBBBB',
            lastname: 'BBBBBB',
            firstname: 'BBBBBB',
            phone: 'BBBBBB',
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

      it('should delete a Customer', () => {
        service.delete('ABC').subscribe(resp => (expectedResult = resp.ok));

        const req = httpMock.expectOne({ method: 'DELETE' });
        req.flush({ status: 200 });
        expect(expectedResult);
      });

      describe('addCustomerToCollectionIfMissing', () => {
        it('should add a Customer to an empty array', () => {
          const customer: ICustomer = { id: 'ABC' };
          expectedResult = service.addCustomerToCollectionIfMissing([], customer);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(customer);
        });

        it('should not add a Customer to an array that contains it', () => {
          const customer: ICustomer = { id: 'ABC' };
          const customerCollection: ICustomer[] = [
            {
              ...customer,
            },
            { id: 'CBA' },
          ];
          expectedResult = service.addCustomerToCollectionIfMissing(customerCollection, customer);
          expect(expectedResult).toHaveLength(2);
        });

        it("should add a Customer to an array that doesn't contain it", () => {
          const customer: ICustomer = { id: 'ABC' };
          const customerCollection: ICustomer[] = [{ id: 'CBA' }];
          expectedResult = service.addCustomerToCollectionIfMissing(customerCollection, customer);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(customer);
        });

        it('should add only unique Customer to an array', () => {
          const customerArray: ICustomer[] = [{ id: 'ABC' }, { id: 'CBA' }, { id: '729322f3-e238-4043-a099-2ac31e53c36c' }];
          const customerCollection: ICustomer[] = [{ id: 'ABC' }];
          expectedResult = service.addCustomerToCollectionIfMissing(customerCollection, ...customerArray);
          expect(expectedResult).toHaveLength(3);
        });

        it('should accept varargs', () => {
          const customer: ICustomer = { id: 'ABC' };
          const customer2: ICustomer = { id: 'CBA' };
          expectedResult = service.addCustomerToCollectionIfMissing([], customer, customer2);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(customer);
          expect(expectedResult).toContain(customer2);
        });

        it('should accept null and undefined values', () => {
          const customer: ICustomer = { id: 'ABC' };
          expectedResult = service.addCustomerToCollectionIfMissing([], null, customer, undefined);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(customer);
        });

        it('should return initial array if no Customer is added', () => {
          const customerCollection: ICustomer[] = [{ id: 'ABC' }];
          expectedResult = service.addCustomerToCollectionIfMissing(customerCollection, undefined, null);
          expect(expectedResult).toEqual(customerCollection);
        });
      });
    });

    afterEach(() => {
      httpMock.verify();
    });
  });
});
