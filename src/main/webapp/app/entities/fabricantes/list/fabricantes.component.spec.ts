import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { of } from 'rxjs';

import { FabricantesService } from '../service/fabricantes.service';

import { FabricantesComponent } from './fabricantes.component';

describe('Component Tests', () => {
  describe('Fabricantes Management Component', () => {
    let comp: FabricantesComponent;
    let fixture: ComponentFixture<FabricantesComponent>;
    let service: FabricantesService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [FabricantesComponent],
      })
        .overrideTemplate(FabricantesComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(FabricantesComponent);
      comp = fixture.componentInstance;
      service = TestBed.inject(FabricantesService);

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
      expect(comp.fabricantes?.[0]).toEqual(jasmine.objectContaining({ id: 123 }));
    });
  });
});
