import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { PosologiasDetailComponent } from './posologias-detail.component';

describe('Component Tests', () => {
  describe('Posologias Management Detail Component', () => {
    let comp: PosologiasDetailComponent;
    let fixture: ComponentFixture<PosologiasDetailComponent>;

    beforeEach(() => {
      TestBed.configureTestingModule({
        declarations: [PosologiasDetailComponent],
        providers: [
          {
            provide: ActivatedRoute,
            useValue: { data: of({ posologias: { id: 123 } }) },
          },
        ],
      })
        .overrideTemplate(PosologiasDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(PosologiasDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should load posologias on init', () => {
        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.posologias).toEqual(jasmine.objectContaining({ id: 123 }));
      });
    });
  });
});
