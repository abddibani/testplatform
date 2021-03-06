import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { of } from 'rxjs';

import { TrackerService } from '../service/tracker.service';

import { TrackerComponent } from './tracker.component';

describe('Component Tests', () => {
  describe('Tracker Management Component', () => {
    let comp: TrackerComponent;
    let fixture: ComponentFixture<TrackerComponent>;
    let service: TrackerService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [TrackerComponent],
      })
        .overrideTemplate(TrackerComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(TrackerComponent);
      comp = fixture.componentInstance;
      service = TestBed.inject(TrackerService);

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
      expect(comp.trackers?.[0]).toEqual(expect.objectContaining({ id: 'ABC' }));
    });
  });
});
