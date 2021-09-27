import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { ClinicasDetailComponent } from './clinicas-detail.component';

describe('Component Tests', () => {
  describe('Clinicas Management Detail Component', () => {
    let comp: ClinicasDetailComponent;
    let fixture: ComponentFixture<ClinicasDetailComponent>;

    beforeEach(() => {
      TestBed.configureTestingModule({
        declarations: [ClinicasDetailComponent],
        providers: [
          {
            provide: ActivatedRoute,
            useValue: { data: of({ clinicas: { id: 123 } }) },
          },
        ],
      })
        .overrideTemplate(ClinicasDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(ClinicasDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should load clinicas on init', () => {
        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.clinicas).toEqual(jasmine.objectContaining({ id: 123 }));
      });
    });
  });
});
