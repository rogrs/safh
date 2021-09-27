import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { EspecialidadesDetailComponent } from './especialidades-detail.component';

describe('Component Tests', () => {
  describe('Especialidades Management Detail Component', () => {
    let comp: EspecialidadesDetailComponent;
    let fixture: ComponentFixture<EspecialidadesDetailComponent>;

    beforeEach(() => {
      TestBed.configureTestingModule({
        declarations: [EspecialidadesDetailComponent],
        providers: [
          {
            provide: ActivatedRoute,
            useValue: { data: of({ especialidades: { id: 123 } }) },
          },
        ],
      })
        .overrideTemplate(EspecialidadesDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(EspecialidadesDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should load especialidades on init', () => {
        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.especialidades).toEqual(jasmine.objectContaining({ id: 123 }));
      });
    });
  });
});
