import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { EnfermariasDetailComponent } from './enfermarias-detail.component';

describe('Component Tests', () => {
  describe('Enfermarias Management Detail Component', () => {
    let comp: EnfermariasDetailComponent;
    let fixture: ComponentFixture<EnfermariasDetailComponent>;

    beforeEach(() => {
      TestBed.configureTestingModule({
        declarations: [EnfermariasDetailComponent],
        providers: [
          {
            provide: ActivatedRoute,
            useValue: { data: of({ enfermarias: { id: 123 } }) },
          },
        ],
      })
        .overrideTemplate(EnfermariasDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(EnfermariasDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should load enfermarias on init', () => {
        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.enfermarias).toEqual(jasmine.objectContaining({ id: 123 }));
      });
    });
  });
});
