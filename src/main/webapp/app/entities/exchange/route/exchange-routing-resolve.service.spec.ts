jest.mock('@angular/router');

import { TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of } from 'rxjs';

import { IExchange, Exchange } from '../exchange.model';
import { ExchangeService } from '../service/exchange.service';

import { ExchangeRoutingResolveService } from './exchange-routing-resolve.service';

describe('Service Tests', () => {
  describe('Exchange routing resolve service', () => {
    let mockRouter: Router;
    let mockActivatedRouteSnapshot: ActivatedRouteSnapshot;
    let routingResolveService: ExchangeRoutingResolveService;
    let service: ExchangeService;
    let resultExchange: IExchange | undefined;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        providers: [Router, ActivatedRouteSnapshot],
      });
      mockRouter = TestBed.inject(Router);
      mockActivatedRouteSnapshot = TestBed.inject(ActivatedRouteSnapshot);
      routingResolveService = TestBed.inject(ExchangeRoutingResolveService);
      service = TestBed.inject(ExchangeService);
      resultExchange = undefined;
    });

    describe('resolve', () => {
      it('should return IExchange returned by find', () => {
        // GIVEN
        service.find = jest.fn(id => of(new HttpResponse({ body: { id } })));
        mockActivatedRouteSnapshot.params = { id: 'ABC' };

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultExchange = result;
        });

        // THEN
        expect(service.find).toBeCalledWith('ABC');
        expect(resultExchange).toEqual({ id: 'ABC' });
      });

      it('should return new IExchange if id is not provided', () => {
        // GIVEN
        service.find = jest.fn();
        mockActivatedRouteSnapshot.params = {};

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultExchange = result;
        });

        // THEN
        expect(service.find).not.toBeCalled();
        expect(resultExchange).toEqual(new Exchange());
      });

      it('should route to 404 page if data not found in server', () => {
        // GIVEN
        jest.spyOn(service, 'find').mockReturnValue(of(new HttpResponse({ body: null as unknown as Exchange })));
        mockActivatedRouteSnapshot.params = { id: 'ABC' };

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultExchange = result;
        });

        // THEN
        expect(service.find).toBeCalledWith('ABC');
        expect(resultExchange).toEqual(undefined);
        expect(mockRouter.navigate).toHaveBeenCalledWith(['404']);
      });
    });
  });
});
