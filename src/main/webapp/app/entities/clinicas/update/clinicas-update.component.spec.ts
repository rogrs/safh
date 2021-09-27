jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject } from 'rxjs';

import { ClinicasService } from '../service/clinicas.service';
import { IClinicas, Clinicas } from '../clinicas.model';

import { ClinicasUpdateComponent } from './clinicas-update.component';

describe('Component Tests', () => {
  describe('Clinicas Management Update Component', () => {
    let comp: ClinicasUpdateComponent;
    let fixture: ComponentFixture<ClinicasUpdateComponent>;
    let activatedRoute: ActivatedRoute;
    let clinicasService: ClinicasService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [ClinicasUpdateComponent],
        providers: [FormBuilder, ActivatedRoute],
      })
        .overrideTemplate(ClinicasUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(ClinicasUpdateComponent);
      activatedRoute = TestBed.inject(ActivatedRoute);
      clinicasService = TestBed.inject(ClinicasService);

      comp = fixture.componentInstance;
    });

    describe('ngOnInit', () => {
      it('Should update editForm', () => {
        const clinicas: IClinicas = { id: 456 };

        activatedRoute.data = of({ clinicas });
        comp.ngOnInit();

        expect(comp.editForm.value).toEqual(expect.objectContaining(clinicas));
      });
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', () => {
        // GIVEN
        const saveSubject = new Subject();
        const clinicas = { id: 123 };
        spyOn(clinicasService, 'update').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ clinicas });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: clinicas }));
        saveSubject.complete();

        // THEN
        expect(comp.previousState).toHaveBeenCalled();
        expect(clinicasService.update).toHaveBeenCalledWith(clinicas);
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', () => {
        // GIVEN
        const saveSubject = new Subject();
        const clinicas = new Clinicas();
        spyOn(clinicasService, 'create').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ clinicas });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: clinicas }));
        saveSubject.complete();

        // THEN
        expect(clinicasService.create).toHaveBeenCalledWith(clinicas);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).toHaveBeenCalled();
      });

      it('Should set isSaving to false on error', () => {
        // GIVEN
        const saveSubject = new Subject();
        const clinicas = { id: 123 };
        spyOn(clinicasService, 'update').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ clinicas });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.error('This is an error!');

        // THEN
        expect(clinicasService.update).toHaveBeenCalledWith(clinicas);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).not.toHaveBeenCalled();
      });
    });
  });
});
