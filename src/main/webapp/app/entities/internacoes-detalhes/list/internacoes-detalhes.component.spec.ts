import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { of } from 'rxjs';

import { InternacoesDetalhesService } from '../service/internacoes-detalhes.service';

import { InternacoesDetalhesComponent } from './internacoes-detalhes.component';

describe('Component Tests', () => {
  describe('InternacoesDetalhes Management Component', () => {
    let comp: InternacoesDetalhesComponent;
    let fixture: ComponentFixture<InternacoesDetalhesComponent>;
    let service: InternacoesDetalhesService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [InternacoesDetalhesComponent],
      })
        .overrideTemplate(InternacoesDetalhesComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(InternacoesDetalhesComponent);
      comp = fixture.componentInstance;
      service = TestBed.inject(InternacoesDetalhesService);

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
      expect(comp.internacoesDetalhes?.[0]).toEqual(jasmine.objectContaining({ id: 123 }));
    });
  });
});
