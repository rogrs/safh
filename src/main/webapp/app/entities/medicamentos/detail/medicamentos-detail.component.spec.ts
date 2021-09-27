import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { MedicamentosDetailComponent } from './medicamentos-detail.component';

describe('Component Tests', () => {
  describe('Medicamentos Management Detail Component', () => {
    let comp: MedicamentosDetailComponent;
    let fixture: ComponentFixture<MedicamentosDetailComponent>;

    beforeEach(() => {
      TestBed.configureTestingModule({
        declarations: [MedicamentosDetailComponent],
        providers: [
          {
            provide: ActivatedRoute,
            useValue: { data: of({ medicamentos: { id: 123 } }) },
          },
        ],
      })
        .overrideTemplate(MedicamentosDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(MedicamentosDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should load medicamentos on init', () => {
        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.medicamentos).toEqual(jasmine.objectContaining({ id: 123 }));
      });
    });
  });
});
