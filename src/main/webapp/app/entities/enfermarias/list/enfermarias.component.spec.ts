import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { of } from 'rxjs';

import { EnfermariasService } from '../service/enfermarias.service';

import { EnfermariasComponent } from './enfermarias.component';

describe('Component Tests', () => {
  describe('Enfermarias Management Component', () => {
    let comp: EnfermariasComponent;
    let fixture: ComponentFixture<EnfermariasComponent>;
    let service: EnfermariasService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [EnfermariasComponent],
      })
        .overrideTemplate(EnfermariasComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(EnfermariasComponent);
      comp = fixture.componentInstance;
      service = TestBed.inject(EnfermariasService);

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
      expect(comp.enfermarias?.[0]).toEqual(jasmine.objectContaining({ id: 123 }));
    });
  });
});
