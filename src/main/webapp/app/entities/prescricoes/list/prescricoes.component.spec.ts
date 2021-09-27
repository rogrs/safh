import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { of } from 'rxjs';

import { PrescricoesService } from '../service/prescricoes.service';

import { PrescricoesComponent } from './prescricoes.component';

describe('Component Tests', () => {
  describe('Prescricoes Management Component', () => {
    let comp: PrescricoesComponent;
    let fixture: ComponentFixture<PrescricoesComponent>;
    let service: PrescricoesService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [PrescricoesComponent],
      })
        .overrideTemplate(PrescricoesComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(PrescricoesComponent);
      comp = fixture.componentInstance;
      service = TestBed.inject(PrescricoesService);

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
      expect(comp.prescricoes?.[0]).toEqual(jasmine.objectContaining({ id: 123 }));
    });
  });
});
