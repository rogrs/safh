import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { PrescricoesDetailComponent } from './prescricoes-detail.component';

describe('Component Tests', () => {
  describe('Prescricoes Management Detail Component', () => {
    let comp: PrescricoesDetailComponent;
    let fixture: ComponentFixture<PrescricoesDetailComponent>;

    beforeEach(() => {
      TestBed.configureTestingModule({
        declarations: [PrescricoesDetailComponent],
        providers: [
          {
            provide: ActivatedRoute,
            useValue: { data: of({ prescricoes: { id: 123 } }) },
          },
        ],
      })
        .overrideTemplate(PrescricoesDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(PrescricoesDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should load prescricoes on init', () => {
        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.prescricoes).toEqual(jasmine.objectContaining({ id: 123 }));
      });
    });
  });
});
