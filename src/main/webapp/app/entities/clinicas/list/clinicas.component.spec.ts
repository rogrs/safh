import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { of } from 'rxjs';

import { ClinicasService } from '../service/clinicas.service';

import { ClinicasComponent } from './clinicas.component';

describe('Component Tests', () => {
  describe('Clinicas Management Component', () => {
    let comp: ClinicasComponent;
    let fixture: ComponentFixture<ClinicasComponent>;
    let service: ClinicasService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [ClinicasComponent],
      })
        .overrideTemplate(ClinicasComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(ClinicasComponent);
      comp = fixture.componentInstance;
      service = TestBed.inject(ClinicasService);

      const headers = new HttpHeaders().append('link', 'link;link');
      spyOn(service, 'query').and.returnValue(
        of(
          new HttpResponse({
            body: [{ id: 123 }],
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
      expect(comp.clinicas?.[0]).toEqual(jasmine.objectContaining({ id: 123 }));
    });
  });
});
