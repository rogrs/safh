import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { of } from 'rxjs';

import { PosologiasService } from '../service/posologias.service';

import { PosologiasComponent } from './posologias.component';

describe('Component Tests', () => {
  describe('Posologias Management Component', () => {
    let comp: PosologiasComponent;
    let fixture: ComponentFixture<PosologiasComponent>;
    let service: PosologiasService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [PosologiasComponent],
      })
        .overrideTemplate(PosologiasComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(PosologiasComponent);
      comp = fixture.componentInstance;
      service = TestBed.inject(PosologiasService);

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
      expect(comp.posologias?.[0]).toEqual(jasmine.objectContaining({ id: 123 }));
    });
  });
});
