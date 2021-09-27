import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { of } from 'rxjs';

import { DietasService } from '../service/dietas.service';

import { DietasComponent } from './dietas.component';

describe('Component Tests', () => {
  describe('Dietas Management Component', () => {
    let comp: DietasComponent;
    let fixture: ComponentFixture<DietasComponent>;
    let service: DietasService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [DietasComponent],
      })
        .overrideTemplate(DietasComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(DietasComponent);
      comp = fixture.componentInstance;
      service = TestBed.inject(DietasService);

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
      expect(comp.dietas?.[0]).toEqual(jasmine.objectContaining({ id: 123 }));
    });
  });
});
