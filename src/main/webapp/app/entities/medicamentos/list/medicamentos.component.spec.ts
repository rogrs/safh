import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { of } from 'rxjs';

import { MedicamentosService } from '../service/medicamentos.service';

import { MedicamentosComponent } from './medicamentos.component';

describe('Component Tests', () => {
  describe('Medicamentos Management Component', () => {
    let comp: MedicamentosComponent;
    let fixture: ComponentFixture<MedicamentosComponent>;
    let service: MedicamentosService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [MedicamentosComponent],
      })
        .overrideTemplate(MedicamentosComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(MedicamentosComponent);
      comp = fixture.componentInstance;
      service = TestBed.inject(MedicamentosService);

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
      expect(comp.medicamentos?.[0]).toEqual(jasmine.objectContaining({ id: 123 }));
    });
  });
});
