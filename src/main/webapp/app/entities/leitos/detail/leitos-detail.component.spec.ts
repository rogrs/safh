import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { LeitosDetailComponent } from './leitos-detail.component';

describe('Component Tests', () => {
  describe('Leitos Management Detail Component', () => {
    let comp: LeitosDetailComponent;
    let fixture: ComponentFixture<LeitosDetailComponent>;

    beforeEach(() => {
      TestBed.configureTestingModule({
        declarations: [LeitosDetailComponent],
        providers: [
          {
            provide: ActivatedRoute,
            useValue: { data: of({ leitos: { id: 123 } }) },
          },
        ],
      })
        .overrideTemplate(LeitosDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(LeitosDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should load leitos on init', () => {
        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.leitos).toEqual(jasmine.objectContaining({ id: 123 }));
      });
    });
  });
});
