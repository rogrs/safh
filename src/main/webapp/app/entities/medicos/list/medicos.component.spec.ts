import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { of } from 'rxjs';

import { MedicosService } from '../service/medicos.service';

import { MedicosComponent } from './medicos.component';

describe('Component Tests', () => {
  describe('Medicos Management Component', () => {
    let comp: MedicosComponent;
    let fixture: ComponentFixture<MedicosComponent>;
    let service: MedicosService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [MedicosComponent],
      })
        .overrideTemplate(MedicosComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(MedicosComponent);
      comp = fixture.componentInstance;
      service = TestBed.inject(MedicosService);

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
      expect(comp.medicos?.[0]).toEqual(jasmine.objectContaining({ id: 123 }));
    });
  });
});
