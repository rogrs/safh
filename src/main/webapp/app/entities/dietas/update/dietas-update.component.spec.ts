jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject } from 'rxjs';

import { DietasService } from '../service/dietas.service';
import { IDietas, Dietas } from '../dietas.model';

import { DietasUpdateComponent } from './dietas-update.component';

describe('Component Tests', () => {
  describe('Dietas Management Update Component', () => {
    let comp: DietasUpdateComponent;
    let fixture: ComponentFixture<DietasUpdateComponent>;
    let activatedRoute: ActivatedRoute;
    let dietasService: DietasService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [DietasUpdateComponent],
        providers: [FormBuilder, ActivatedRoute],
      })
        .overrideTemplate(DietasUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(DietasUpdateComponent);
      activatedRoute = TestBed.inject(ActivatedRoute);
      dietasService = TestBed.inject(DietasService);

      comp = fixture.componentInstance;
    });

    describe('ngOnInit', () => {
      it('Should update editForm', () => {
        const dietas: IDietas = { id: 456 };

        activatedRoute.data = of({ dietas });
        comp.ngOnInit();

        expect(comp.editForm.value).toEqual(expect.objectContaining(dietas));
      });
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', () => {
        // GIVEN
        const saveSubject = new Subject();
        const dietas = { id: 123 };
        spyOn(dietasService, 'update').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ dietas });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: dietas }));
        saveSubject.complete();

        // THEN
        expect(comp.previousState).toHaveBeenCalled();
        expect(dietasService.update).toHaveBeenCalledWith(dietas);
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', () => {
        // GIVEN
        const saveSubject = new Subject();
        const dietas = new Dietas();
        spyOn(dietasService, 'create').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ dietas });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: dietas }));
        saveSubject.complete();

        // THEN
        expect(dietasService.create).toHaveBeenCalledWith(dietas);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).toHaveBeenCalled();
      });

      it('Should set isSaving to false on error', () => {
        // GIVEN
        const saveSubject = new Subject();
        const dietas = { id: 123 };
        spyOn(dietasService, 'update').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ dietas });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.error('This is an error!');

        // THEN
        expect(dietasService.update).toHaveBeenCalledWith(dietas);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).not.toHaveBeenCalled();
      });
    });
  });
});
