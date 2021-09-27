import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { of } from 'rxjs';

import { PacientesService } from '../service/pacientes.service';

import { PacientesComponent } from './pacientes.component';

describe('Component Tests', () => {
  describe('Pacientes Management Component', () => {
    let comp: PacientesComponent;
    let fixture: ComponentFixture<PacientesComponent>;
    let service: PacientesService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [PacientesComponent],
      })
        .overrideTemplate(PacientesComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(PacientesComponent);
      comp = fixture.componentInstance;
      service = TestBed.inject(PacientesService);

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
      expect(comp.pacientes?.[0]).toEqual(jasmine.objectContaining({ id: 123 }));
    });
  });
});
