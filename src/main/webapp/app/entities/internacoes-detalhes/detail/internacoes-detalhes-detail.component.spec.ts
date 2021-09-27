import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { InternacoesDetalhesDetailComponent } from './internacoes-detalhes-detail.component';

describe('Component Tests', () => {
  describe('InternacoesDetalhes Management Detail Component', () => {
    let comp: InternacoesDetalhesDetailComponent;
    let fixture: ComponentFixture<InternacoesDetalhesDetailComponent>;

    beforeEach(() => {
      TestBed.configureTestingModule({
        declarations: [InternacoesDetalhesDetailComponent],
        providers: [
          {
            provide: ActivatedRoute,
            useValue: { data: of({ internacoesDetalhes: { id: 123 } }) },
          },
        ],
      })
        .overrideTemplate(InternacoesDetalhesDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(InternacoesDetalhesDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should load internacoesDetalhes on init', () => {
        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.internacoesDetalhes).toEqual(jasmine.objectContaining({ id: 123 }));
      });
    });
  });
});
