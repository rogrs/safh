import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { MedicosDetailComponent } from './medicos-detail.component';

describe('Component Tests', () => {
  describe('Medicos Management Detail Component', () => {
    let comp: MedicosDetailComponent;
    let fixture: ComponentFixture<MedicosDetailComponent>;

    beforeEach(() => {
      TestBed.configureTestingModule({
        declarations: [MedicosDetailComponent],
        providers: [
          {
            provide: ActivatedRoute,
            useValue: { data: of({ medicos: { id: 123 } }) },
          },
        ],
      })
        .overrideTemplate(MedicosDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(MedicosDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should load medicos on init', () => {
        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.medicos).toEqual(jasmine.objectContaining({ id: 123 }));
      });
    });
  });
});
