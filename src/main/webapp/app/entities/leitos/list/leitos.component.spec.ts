import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { of } from 'rxjs';

import { LeitosService } from '../service/leitos.service';

import { LeitosComponent } from './leitos.component';

describe('Component Tests', () => {
  describe('Leitos Management Component', () => {
    let comp: LeitosComponent;
    let fixture: ComponentFixture<LeitosComponent>;
    let service: LeitosService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [LeitosComponent],
      })
        .overrideTemplate(LeitosComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(LeitosComponent);
      comp = fixture.componentInstance;
      service = TestBed.inject(LeitosService);

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
      expect(comp.leitos?.[0]).toEqual(jasmine.objectContaining({ id: 123 }));
    });
  });
});
