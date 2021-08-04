jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject } from 'rxjs';

import { ExchangeService } from '../service/exchange.service';
import { IExchange, Exchange } from '../exchange.model';
import { IClient } from 'app/entities/client/client.model';
import { ClientService } from 'app/entities/client/service/client.service';

import { ExchangeUpdateComponent } from './exchange-update.component';

describe('Component Tests', () => {
  describe('Exchange Management Update Component', () => {
    let comp: ExchangeUpdateComponent;
    let fixture: ComponentFixture<ExchangeUpdateComponent>;
    let activatedRoute: ActivatedRoute;
    let exchangeService: ExchangeService;
    let clientService: ClientService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [ExchangeUpdateComponent],
        providers: [FormBuilder, ActivatedRoute],
      })
        .overrideTemplate(ExchangeUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(ExchangeUpdateComponent);
      activatedRoute = TestBed.inject(ActivatedRoute);
      exchangeService = TestBed.inject(ExchangeService);
      clientService = TestBed.inject(ClientService);

      comp = fixture.componentInstance;
    });

    describe('ngOnInit', () => {
      it('Should call Client query and add missing value', () => {
        const exchange: IExchange = { id: 'CBA' };
        const client: IClient = { id: '1d711fe1-ebef-4bf4-b562-afd39fdaef48' };
        exchange.client = client;

        const clientCollection: IClient[] = [{ id: 'e7c1a256-e1d1-4053-8216-cbe4515cf0c0' }];
        jest.spyOn(clientService, 'query').mockReturnValue(of(new HttpResponse({ body: clientCollection })));
        const additionalClients = [client];
        const expectedCollection: IClient[] = [...additionalClients, ...clientCollection];
        jest.spyOn(clientService, 'addClientToCollectionIfMissing').mockReturnValue(expectedCollection);

        activatedRoute.data = of({ exchange });
        comp.ngOnInit();

        expect(clientService.query).toHaveBeenCalled();
        expect(clientService.addClientToCollectionIfMissing).toHaveBeenCalledWith(clientCollection, ...additionalClients);
        expect(comp.clientsSharedCollection).toEqual(expectedCollection);
      });

      it('Should update editForm', () => {
        const exchange: IExchange = { id: 'CBA' };
        const client: IClient = { id: '2d4f7c6b-e15a-4886-8ad0-f1b915f5b2c4' };
        exchange.client = client;

        activatedRoute.data = of({ exchange });
        comp.ngOnInit();

        expect(comp.editForm.value).toEqual(expect.objectContaining(exchange));
        expect(comp.clientsSharedCollection).toContain(client);
      });
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', () => {
        // GIVEN
        const saveSubject = new Subject<HttpResponse<Exchange>>();
        const exchange = { id: 'ABC' };
        jest.spyOn(exchangeService, 'update').mockReturnValue(saveSubject);
        jest.spyOn(comp, 'previousState');
        activatedRoute.data = of({ exchange });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: exchange }));
        saveSubject.complete();

        // THEN
        expect(comp.previousState).toHaveBeenCalled();
        expect(exchangeService.update).toHaveBeenCalledWith(exchange);
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', () => {
        // GIVEN
        const saveSubject = new Subject<HttpResponse<Exchange>>();
        const exchange = new Exchange();
        jest.spyOn(exchangeService, 'create').mockReturnValue(saveSubject);
        jest.spyOn(comp, 'previousState');
        activatedRoute.data = of({ exchange });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: exchange }));
        saveSubject.complete();

        // THEN
        expect(exchangeService.create).toHaveBeenCalledWith(exchange);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).toHaveBeenCalled();
      });

      it('Should set isSaving to false on error', () => {
        // GIVEN
        const saveSubject = new Subject<HttpResponse<Exchange>>();
        const exchange = { id: 'ABC' };
        jest.spyOn(exchangeService, 'update').mockReturnValue(saveSubject);
        jest.spyOn(comp, 'previousState');
        activatedRoute.data = of({ exchange });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.error('This is an error!');

        // THEN
        expect(exchangeService.update).toHaveBeenCalledWith(exchange);
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
