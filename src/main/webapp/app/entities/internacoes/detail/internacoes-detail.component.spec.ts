import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { InternacoesDetailComponent } from './internacoes-detail.component';

describe('Component Tests', () => {
  describe('Internacoes Management Detail Component', () => {
    let comp: InternacoesDetailComponent;
    let fixture: ComponentFixture<InternacoesDetailComponent>;

    beforeEach(() => {
      TestBed.configureTestingModule({
        declarations: [InternacoesDetailComponent],
        providers: [
          {
            provide: ActivatedRoute,
            useValue: { data: of({ internacoes: { id: 123 } }) },
          },
        ],
      })
        .overrideTemplate(InternacoesDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(InternacoesDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should load internacoes on init', () => {
        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.internacoes).toEqual(jasmine.objectContaining({ id: 123 }));
      });
    });
  });
});
