jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject } from 'rxjs';

import { MedicosService } from '../service/medicos.service';
import { IMedicos, Medicos } from '../medicos.model';
import { IEspecialidades } from 'app/entities/especialidades/especialidades.model';
import { EspecialidadesService } from 'app/entities/especialidades/service/especialidades.service';

import { MedicosUpdateComponent } from './medicos-update.component';

describe('Component Tests', () => {
  describe('Medicos Management Update Component', () => {
    let comp: MedicosUpdateComponent;
    let fixture: ComponentFixture<MedicosUpdateComponent>;
    let activatedRoute: ActivatedRoute;
    let medicosService: MedicosService;
    let especialidadesService: EspecialidadesService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [MedicosUpdateComponent],
        providers: [FormBuilder, ActivatedRoute],
      })
        .overrideTemplate(MedicosUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(MedicosUpdateComponent);
      activatedRoute = TestBed.inject(ActivatedRoute);
      medicosService = TestBed.inject(MedicosService);
      especialidadesService = TestBed.inject(EspecialidadesService);

      comp = fixture.componentInstance;
    });

    describe('ngOnInit', () => {
      it('Should call Especialidades query and add missing value', () => {
        const medicos: IMedicos = { id: 456 };
        const especialidades: IEspecialidades = { id: 72454 };
        medicos.especialidades = especialidades;

        const especialidadesCollection: IEspecialidades[] = [{ id: 76824 }];
        spyOn(especialidadesService, 'query').and.returnValue(of(new HttpResponse({ body: especialidadesCollection })));
        const additionalEspecialidades = [especialidades];
        const expectedCollection: IEspecialidades[] = [...additionalEspecialidades, ...especialidadesCollection];
        spyOn(especialidadesService, 'addEspecialidadesToCollectionIfMissing').and.returnValue(expectedCollection);

        activatedRoute.data = of({ medicos });
        comp.ngOnInit();

        expect(especialidadesService.query).toHaveBeenCalled();
        expect(especialidadesService.addEspecialidadesToCollectionIfMissing).toHaveBeenCalledWith(
          especialidadesCollection,
          ...additionalEspecialidades
        );
        expect(comp.especialidadesSharedCollection).toEqual(expectedCollection);
      });

      it('Should update editForm', () => {
        const medicos: IMedicos = { id: 456 };
        const especialidades: IEspecialidades = { id: 1400 };
        medicos.especialidades = especialidades;

        activatedRoute.data = of({ medicos });
        comp.ngOnInit();

        expect(comp.editForm.value).toEqual(expect.objectContaining(medicos));
        expect(comp.especialidadesSharedCollection).toContain(especialidades);
      });
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', () => {
        // GIVEN
        const saveSubject = new Subject();
        const medicos = { id: 123 };
        spyOn(medicosService, 'update').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ medicos });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: medicos }));
        saveSubject.complete();

        // THEN
        expect(comp.previousState).toHaveBeenCalled();
        expect(medicosService.update).toHaveBeenCalledWith(medicos);
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', () => {
        // GIVEN
        const saveSubject = new Subject();
        const medicos = new Medicos();
        spyOn(medicosService, 'create').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ medicos });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: medicos }));
        saveSubject.complete();

        // THEN
        expect(medicosService.create).toHaveBeenCalledWith(medicos);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).toHaveBeenCalled();
      });

      it('Should set isSaving to false on error', () => {
        // GIVEN
        const saveSubject = new Subject();
        const medicos = { id: 123 };
        spyOn(medicosService, 'update').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ medicos });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.error('This is an error!');

        // THEN
        expect(medicosService.update).toHaveBeenCalledWith(medicos);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).not.toHaveBeenCalled();
      });
    });

    describe('Tracking relationships identifiers', () => {
      describe('trackEspecialidadesById', () => {
        it('Should return tracked Especialidades primary key', () => {
          const entity = { id: 123 };
          const trackResult = comp.trackEspecialidadesById(0, entity);
          expect(trackResult).toEqual(entity.id);
        });
      });
    });
  });
});
