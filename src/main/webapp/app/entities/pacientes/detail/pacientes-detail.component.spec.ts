import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { PacientesDetailComponent } from './pacientes-detail.component';

describe('Component Tests', () => {
  describe('Pacientes Management Detail Component', () => {
    let comp: PacientesDetailComponent;
    let fixture: ComponentFixture<PacientesDetailComponent>;

    beforeEach(() => {
      TestBed.configureTestingModule({
        declarations: [PacientesDetailComponent],
        providers: [
          {
            provide: ActivatedRoute,
            useValue: { data: of({ pacientes: { id: 123 } }) },
          },
        ],
      })
        .overrideTemplate(PacientesDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(PacientesDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should load pacientes on init', () => {
        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.pacientes).toEqual(jasmine.objectContaining({ id: 123 }));
      });
    });
  });
});
