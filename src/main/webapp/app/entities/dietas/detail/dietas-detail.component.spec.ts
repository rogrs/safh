import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { DietasDetailComponent } from './dietas-detail.component';

describe('Component Tests', () => {
  describe('Dietas Management Detail Component', () => {
    let comp: DietasDetailComponent;
    let fixture: ComponentFixture<DietasDetailComponent>;

    beforeEach(() => {
      TestBed.configureTestingModule({
        declarations: [DietasDetailComponent],
        providers: [
          {
            provide: ActivatedRoute,
            useValue: { data: of({ dietas: { id: 123 } }) },
          },
        ],
      })
        .overrideTemplate(DietasDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(DietasDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should load dietas on init', () => {
        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.dietas).toEqual(jasmine.objectContaining({ id: 123 }));
      });
    });
  });
});
