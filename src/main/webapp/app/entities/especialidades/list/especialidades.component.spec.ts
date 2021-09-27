import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { of } from 'rxjs';

import { EspecialidadesService } from '../service/especialidades.service';

import { EspecialidadesComponent } from './especialidades.component';

describe('Component Tests', () => {
  describe('Especialidades Management Component', () => {
    let comp: EspecialidadesComponent;
    let fixture: ComponentFixture<EspecialidadesComponent>;
    let service: EspecialidadesService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [EspecialidadesComponent],
      })
        .overrideTemplate(EspecialidadesComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(EspecialidadesComponent);
      comp = fixture.componentInstance;
      service = TestBed.inject(EspecialidadesService);

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
      expect(comp.especialidades?.[0]).toEqual(jasmine.objectContaining({ id: 123 }));
    });
  });
});
