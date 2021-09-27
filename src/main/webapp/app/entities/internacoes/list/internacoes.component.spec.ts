import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { of } from 'rxjs';

import { InternacoesService } from '../service/internacoes.service';

import { InternacoesComponent } from './internacoes.component';

describe('Component Tests', () => {
  describe('Internacoes Management Component', () => {
    let comp: InternacoesComponent;
    let fixture: ComponentFixture<InternacoesComponent>;
    let service: InternacoesService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [InternacoesComponent],
      })
        .overrideTemplate(InternacoesComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(InternacoesComponent);
      comp = fixture.componentInstance;
      service = TestBed.inject(InternacoesService);

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
      expect(comp.internacoes?.[0]).toEqual(jasmine.objectContaining({ id: 123 }));
    });
  });
});
