import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { FabricantesDetailComponent } from './fabricantes-detail.component';

describe('Component Tests', () => {
  describe('Fabricantes Management Detail Component', () => {
    let comp: FabricantesDetailComponent;
    let fixture: ComponentFixture<FabricantesDetailComponent>;

    beforeEach(() => {
      TestBed.configureTestingModule({
        declarations: [FabricantesDetailComponent],
        providers: [
          {
            provide: ActivatedRoute,
            useValue: { data: of({ fabricantes: { id: 123 } }) },
          },
        ],
      })
        .overrideTemplate(FabricantesDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(FabricantesDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should load fabricantes on init', () => {
        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.fabricantes).toEqual(jasmine.objectContaining({ id: 123 }));
      });
    });
  });
});
