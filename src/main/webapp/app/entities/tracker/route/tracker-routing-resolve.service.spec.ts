jest.mock('@angular/router');

import { TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of } from 'rxjs';

import { ITracker, Tracker } from '../tracker.model';
import { TrackerService } from '../service/tracker.service';

import { TrackerRoutingResolveService } from './tracker-routing-resolve.service';

describe('Service Tests', () => {
  describe('Tracker routing resolve service', () => {
    let mockRouter: Router;
    let mockActivatedRouteSnapshot: ActivatedRouteSnapshot;
    let routingResolveService: TrackerRoutingResolveService;
    let service: TrackerService;
    let resultTracker: ITracker | undefined;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        providers: [Router, ActivatedRouteSnapshot],
      });
      mockRouter = TestBed.inject(Router);
      mockActivatedRouteSnapshot = TestBed.inject(ActivatedRouteSnapshot);
      routingResolveService = TestBed.inject(TrackerRoutingResolveService);
      service = TestBed.inject(TrackerService);
      resultTracker = undefined;
    });

    describe('resolve', () => {
      it('should return ITracker returned by find', () => {
        // GIVEN
        service.find = jest.fn(id => of(new HttpResponse({ body: { id } })));
        mockActivatedRouteSnapshot.params = { id: 'ABC' };

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultTracker = result;
        });

        // THEN
        expect(service.find).toBeCalledWith('ABC');
        expect(resultTracker).toEqual({ id: 'ABC' });
      });

      it('should return new ITracker if id is not provided', () => {
        // GIVEN
        service.find = jest.fn();
        mockActivatedRouteSnapshot.params = {};

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultTracker = result;
        });

        // THEN
        expect(service.find).not.toBeCalled();
        expect(resultTracker).toEqual(new Tracker());
      });

      it('should route to 404 page if data not found in server', () => {
        // GIVEN
        jest.spyOn(service, 'find').mockReturnValue(of(new HttpResponse({ body: null as unknown as Tracker })));
        mockActivatedRouteSnapshot.params = { id: 'ABC' };

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultTracker = result;
        });

        // THEN
        expect(service.find).toBeCalledWith('ABC');
        expect(resultTracker).toEqual(undefined);
        expect(mockRouter.navigate).toHaveBeenCalledWith(['404']);
      });
    });
  });
});
