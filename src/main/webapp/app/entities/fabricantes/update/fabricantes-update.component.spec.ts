jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject } from 'rxjs';

import { FabricantesService } from '../service/fabricantes.service';
import { IFabricantes, Fabricantes } from '../fabricantes.model';

import { FabricantesUpdateComponent } from './fabricantes-update.component';

describe('Component Tests', () => {
  describe('Fabricantes Management Update Component', () => {
    let comp: FabricantesUpdateComponent;
    let fixture: ComponentFixture<FabricantesUpdateComponent>;
    let activatedRoute: ActivatedRoute;
    let fabricantesService: FabricantesService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [FabricantesUpdateComponent],
        providers: [FormBuilder, ActivatedRoute],
      })
        .overrideTemplate(FabricantesUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(FabricantesUpdateComponent);
      activatedRoute = TestBed.inject(ActivatedRoute);
      fabricantesService = TestBed.inject(FabricantesService);

      comp = fixture.componentInstance;
    });

    describe('ngOnInit', () => {
      it('Should update editForm', () => {
        const fabricantes: IFabricantes = { id: 456 };

        activatedRoute.data = of({ fabricantes });
        comp.ngOnInit();

        expect(comp.editForm.value).toEqual(expect.objectContaining(fabricantes));
      });
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', () => {
        // GIVEN
        const saveSubject = new Subject();
        const fabricantes = { id: 123 };
        spyOn(fabricantesService, 'update').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ fabricantes });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: fabricantes }));
        saveSubject.complete();

        // THEN
        expect(comp.previousState).toHaveBeenCalled();
        expect(fabricantesService.update).toHaveBeenCalledWith(fabricantes);
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', () => {
        // GIVEN
        const saveSubject = new Subject();
        const fabricantes = new Fabricantes();
        spyOn(fabricantesService, 'create').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ fabricantes });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: fabricantes }));
        saveSubject.complete();

        // THEN
        expect(fabricantesService.create).toHaveBeenCalledWith(fabricantes);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).toHaveBeenCalled();
      });

      it('Should set isSaving to false on error', () => {
        // GIVEN
        const saveSubject = new Subject();
        const fabricantes = { id: 123 };
        spyOn(fabricantesService, 'update').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ fabricantes });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.error('This is an error!');

        // THEN
        expect(fabricantesService.update).toHaveBeenCalledWith(fabricantes);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).not.toHaveBeenCalled();
      });
    });
  });
});
