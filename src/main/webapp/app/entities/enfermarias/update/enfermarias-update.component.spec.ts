jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject } from 'rxjs';

import { EnfermariasService } from '../service/enfermarias.service';
import { IEnfermarias, Enfermarias } from '../enfermarias.model';

import { EnfermariasUpdateComponent } from './enfermarias-update.component';

describe('Component Tests', () => {
  describe('Enfermarias Management Update Component', () => {
    let comp: EnfermariasUpdateComponent;
    let fixture: ComponentFixture<EnfermariasUpdateComponent>;
    let activatedRoute: ActivatedRoute;
    let enfermariasService: EnfermariasService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [EnfermariasUpdateComponent],
        providers: [FormBuilder, ActivatedRoute],
      })
        .overrideTemplate(EnfermariasUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(EnfermariasUpdateComponent);
      activatedRoute = TestBed.inject(ActivatedRoute);
      enfermariasService = TestBed.inject(EnfermariasService);

      comp = fixture.componentInstance;
    });

    describe('ngOnInit', () => {
      it('Should update editForm', () => {
        const enfermarias: IEnfermarias = { id: 456 };

        activatedRoute.data = of({ enfermarias });
        comp.ngOnInit();

        expect(comp.editForm.value).toEqual(expect.objectContaining(enfermarias));
      });
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', () => {
        // GIVEN
        const saveSubject = new Subject();
        const enfermarias = { id: 123 };
        spyOn(enfermariasService, 'update').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ enfermarias });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: enfermarias }));
        saveSubject.complete();

        // THEN
        expect(comp.previousState).toHaveBeenCalled();
        expect(enfermariasService.update).toHaveBeenCalledWith(enfermarias);
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', () => {
        // GIVEN
        const saveSubject = new Subject();
        const enfermarias = new Enfermarias();
        spyOn(enfermariasService, 'create').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ enfermarias });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: enfermarias }));
        saveSubject.complete();

        // THEN
        expect(enfermariasService.create).toHaveBeenCalledWith(enfermarias);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).toHaveBeenCalled();
      });

      it('Should set isSaving to false on error', () => {
        // GIVEN
        const saveSubject = new Subject();
        const enfermarias = { id: 123 };
        spyOn(enfermariasService, 'update').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ enfermarias });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.error('This is an error!');

        // THEN
        expect(enfermariasService.update).toHaveBeenCalledWith(enfermarias);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).not.toHaveBeenCalled();
      });
    });
  });
});
