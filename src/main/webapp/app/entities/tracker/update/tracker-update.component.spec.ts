jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject } from 'rxjs';

import { TrackerService } from '../service/tracker.service';
import { ITracker, Tracker } from '../tracker.model';
import { IClient } from 'app/entities/client/client.model';
import { ClientService } from 'app/entities/client/service/client.service';

import { TrackerUpdateComponent } from './tracker-update.component';

describe('Component Tests', () => {
  describe('Tracker Management Update Component', () => {
    let comp: TrackerUpdateComponent;
    let fixture: ComponentFixture<TrackerUpdateComponent>;
    let activatedRoute: ActivatedRoute;
    let trackerService: TrackerService;
    let clientService: ClientService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [TrackerUpdateComponent],
        providers: [FormBuilder, ActivatedRoute],
      })
        .overrideTemplate(TrackerUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(TrackerUpdateComponent);
      activatedRoute = TestBed.inject(ActivatedRoute);
      trackerService = TestBed.inject(TrackerService);
      clientService = TestBed.inject(ClientService);

      comp = fixture.componentInstance;
    });

    describe('ngOnInit', () => {
      it('Should call Client query and add missing value', () => {
        const tracker: ITracker = { id: 'CBA' };
        const client: IClient = { id: '23e25b3d-5e6e-450c-8633-c07ead3e7ff1' };
        tracker.client = client;

        const clientCollection: IClient[] = [{ id: '0fa85e67-bb57-4e89-99bb-d13ff005105d' }];
        jest.spyOn(clientService, 'query').mockReturnValue(of(new HttpResponse({ body: clientCollection })));
        const additionalClients = [client];
        const expectedCollection: IClient[] = [...additionalClients, ...clientCollection];
        jest.spyOn(clientService, 'addClientToCollectionIfMissing').mockReturnValue(expectedCollection);

        activatedRoute.data = of({ tracker });
        comp.ngOnInit();

        expect(clientService.query).toHaveBeenCalled();
        expect(clientService.addClientToCollectionIfMissing).toHaveBeenCalledWith(clientCollection, ...additionalClients);
        expect(comp.clientsSharedCollection).toEqual(expectedCollection);
      });

      it('Should update editForm', () => {
        const tracker: ITracker = { id: 'CBA' };
        const client: IClient = { id: '28afd2d9-2623-4e4a-92d2-b018bf15a1f6' };
        tracker.client = client;

        activatedRoute.data = of({ tracker });
        comp.ngOnInit();

        expect(comp.editForm.value).toEqual(expect.objectContaining(tracker));
        expect(comp.clientsSharedCollection).toContain(client);
      });
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', () => {
        // GIVEN
        const saveSubject = new Subject<HttpResponse<Tracker>>();
        const tracker = { id: 'ABC' };
        jest.spyOn(trackerService, 'update').mockReturnValue(saveSubject);
        jest.spyOn(comp, 'previousState');
        activatedRoute.data = of({ tracker });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: tracker }));
        saveSubject.complete();

        // THEN
        expect(comp.previousState).toHaveBeenCalled();
        expect(trackerService.update).toHaveBeenCalledWith(tracker);
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', () => {
        // GIVEN
        const saveSubject = new Subject<HttpResponse<Tracker>>();
        const tracker = new Tracker();
        jest.spyOn(trackerService, 'create').mockReturnValue(saveSubject);
        jest.spyOn(comp, 'previousState');
        activatedRoute.data = of({ tracker });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: tracker }));
        saveSubject.complete();

        // THEN
        expect(trackerService.create).toHaveBeenCalledWith(tracker);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).toHaveBeenCalled();
      });

      it('Should set isSaving to false on error', () => {
        // GIVEN
        const saveSubject = new Subject<HttpResponse<Tracker>>();
        const tracker = { id: 'ABC' };
        jest.spyOn(trackerService, 'update').mockReturnValue(saveSubject);
        jest.spyOn(comp, 'previousState');
        activatedRoute.data = of({ tracker });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.error('This is an error!');

        // THEN
        expect(trackerService.update).toHaveBeenCalledWith(tracker);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).not.toHaveBeenCalled();
      });
    });

    describe('Tracking relationships identifiers', () => {
      describe('trackClientById', () => {
        it('Should return tracked Client primary key', () => {
          const entity = { id: 'ABC' };
          const trackResult = comp.trackClientById(0, entity);
          expect(trackResult).toEqual(entity.id);
        });
      });
    });
  });
});
