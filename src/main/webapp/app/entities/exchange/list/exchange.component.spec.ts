import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { of } from 'rxjs';

import { ExchangeService } from '../service/exchange.service';

import { ExchangeComponent } from './exchange.component';

describe('Component Tests', () => {
  describe('Exchange Management Component', () => {
    let comp: ExchangeComponent;
    let fixture: ComponentFixture<ExchangeComponent>;
    let service: ExchangeService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [ExchangeComponent],
      })
        .overrideTemplate(ExchangeComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(ExchangeComponent);
      comp = fixture.componentInstance;
      service = TestBed.inject(ExchangeService);

      const headers = new HttpHeaders().append('link', 'link;link');
      jest.spyOn(service, 'query').mockReturnValue(
        of(
          new HttpResponse({
            body: [{ id: 'ABC' }],
            headers,
          })
        )
      );
    });

    it('Should call load all on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(service.query).toHaveBeenCalled();
      expect(comp.exchanges?.[0]).toEqual(expect.objectContaining({ id: 'ABC' }));
    });
  });
});
