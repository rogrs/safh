jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject } from 'rxjs';

import { InternacoesService } from '../service/internacoes.service';
import { IInternacoes, Internacoes } from '../internacoes.model';
import { IPacientes } from 'app/entities/pacientes/pacientes.model';
import { PacientesService } from 'app/entities/pacientes/service/pacientes.service';
import { IClinicas } from 'app/entities/clinicas/clinicas.model';
import { ClinicasService } from 'app/entities/clinicas/service/clinicas.service';
import { IMedicos } from 'app/entities/medicos/medicos.model';
import { MedicosService } from 'app/entities/medicos/service/medicos.service';

import { InternacoesUpdateComponent } from './internacoes-update.component';

describe('Component Tests', () => {
  describe('Internacoes Management Update Component', () => {
    let comp: InternacoesUpdateComponent;
    let fixture: ComponentFixture<InternacoesUpdateComponent>;
    let activatedRoute: ActivatedRoute;
    let internacoesService: InternacoesService;
    let pacientesService: PacientesService;
    let clinicasService: ClinicasService;
    let medicosService: MedicosService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [InternacoesUpdateComponent],
        providers: [FormBuilder, ActivatedRoute],
      })
        .overrideTemplate(InternacoesUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(InternacoesUpdateComponent);
      activatedRoute = TestBed.inject(ActivatedRoute);
      internacoesService = TestBed.inject(InternacoesService);
      pacientesService = TestBed.inject(PacientesService);
      clinicasService = TestBed.inject(ClinicasService);
      medicosService = TestBed.inject(MedicosService);

      comp = fixture.componentInstance;
    });

    describe('ngOnInit', () => {
      it('Should call Pacientes query and add missing value', () => {
        const internacoes: IInternacoes = { id: 456 };
        const pacientes: IPacientes = { id: 26353 };
        internacoes.pacientes = pacientes;

        const pacientesCollection: IPacientes[] = [{ id: 72106 }];
        spyOn(pacientesService, 'query').and.returnValue(of(new HttpResponse({ body: pacientesCollection })));
        const additionalPacientes = [pacientes];
        const expectedCollection: IPacientes[] = [...additionalPacientes, ...pacientesCollection];
        spyOn(pacientesService, 'addPacientesToCollectionIfMissing').and.returnValue(expectedCollection);

        activatedRoute.data = of({ internacoes });
        comp.ngOnInit();

        expect(pacientesService.query).toHaveBeenCalled();
        expect(pacientesService.addPacientesToCollectionIfMissing).toHaveBeenCalledWith(pacientesCollection, ...additionalPacientes);
        expect(comp.pacientesSharedCollection).toEqual(expectedCollection);
      });

      it('Should call Clinicas query and add missing value', () => {
        const internacoes: IInternacoes = { id: 456 };
        const clinicas: IClinicas = { id: 22885 };
        internacoes.clinicas = clinicas;

        const clinicasCollection: IClinicas[] = [{ id: 50815 }];
        spyOn(clinicasService, 'query').and.returnValue(of(new HttpResponse({ body: clinicasCollection })));
        const additionalClinicas = [clinicas];
        const expectedCollection: IClinicas[] = [...additionalClinicas, ...clinicasCollection];
        spyOn(clinicasService, 'addClinicasToCollectionIfMissing').and.returnValue(expectedCollection);

        activatedRoute.data = of({ internacoes });
        comp.ngOnInit();

        expect(clinicasService.query).toHaveBeenCalled();
        expect(clinicasService.addClinicasToCollectionIfMissing).toHaveBeenCalledWith(clinicasCollection, ...additionalClinicas);
        expect(comp.clinicasSharedCollection).toEqual(expectedCollection);
      });

      it('Should call Medicos query and add missing value', () => {
        const internacoes: IInternacoes = { id: 456 };
        const medicos: IMedicos = { id: 17224 };
        internacoes.medicos = medicos;

        const medicosCollection: IMedicos[] = [{ id: 81175 }];
        spyOn(medicosService, 'query').and.returnValue(of(new HttpResponse({ body: medicosCollection })));
        const additionalMedicos = [medicos];
        const expectedCollection: IMedicos[] = [...additionalMedicos, ...medicosCollection];
        spyOn(medicosService, 'addMedicosToCollectionIfMissing').and.returnValue(expectedCollection);

        activatedRoute.data = of({ internacoes });
        comp.ngOnInit();

        expect(medicosService.query).toHaveBeenCalled();
        expect(medicosService.addMedicosToCollectionIfMissing).toHaveBeenCalledWith(medicosCollection, ...additionalMedicos);
        expect(comp.medicosSharedCollection).toEqual(expectedCollection);
      });

      it('Should update editForm', () => {
        const internacoes: IInternacoes = { id: 456 };
        const pacientes: IPacientes = { id: 58627 };
        internacoes.pacientes = pacientes;
        const clinicas: IClinicas = { id: 41918 };
        internacoes.clinicas = clinicas;
        const medicos: IMedicos = { id: 19590 };
        internacoes.medicos = medicos;

        activatedRoute.data = of({ internacoes });
        comp.ngOnInit();

        expect(comp.editForm.value).toEqual(expect.objectContaining(internacoes));
        expect(comp.pacientesSharedCollection).toContain(pacientes);
        expect(comp.clinicasSharedCollection).toContain(clinicas);
        expect(comp.medicosSharedCollection).toContain(medicos);
      });
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', () => {
        // GIVEN
        const saveSubject = new Subject();
        const internacoes = { id: 123 };
        spyOn(internacoesService, 'update').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ internacoes });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: internacoes }));
        saveSubject.complete();

        // THEN
        expect(comp.previousState).toHaveBeenCalled();
        expect(internacoesService.update).toHaveBeenCalledWith(internacoes);
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', () => {
        // GIVEN
        const saveSubject = new Subject();
        const internacoes = new Internacoes();
        spyOn(internacoesService, 'create').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ internacoes });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: internacoes }));
        saveSubject.complete();

        // THEN
        expect(internacoesService.create).toHaveBeenCalledWith(internacoes);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).toHaveBeenCalled();
      });

      it('Should set isSaving to false on error', () => {
        // GIVEN
        const saveSubject = new Subject();
        const internacoes = { id: 123 };
        spyOn(internacoesService, 'update').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ internacoes });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.error('This is an error!');

        // THEN
        expect(internacoesService.update).toHaveBeenCalledWith(internacoes);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).not.toHaveBeenCalled();
      });
    });

    describe('Tracking relationships identifiers', () => {
      describe('trackPacientesById', () => {
        it('Should return tracked Pacientes primary key', () => {
          const entity = { id: 123 };
          const trackResult = comp.trackPacientesById(0, entity);
          expect(trackResult).toEqual(entity.id);
        });
      });

      describe('trackClinicasById', () => {
        it('Should return tracked Clinicas primary key', () => {
          const entity = { id: 123 };
          const trackResult = comp.trackClinicasById(0, entity);
          expect(trackResult).toEqual(entity.id);
        });
      });

      describe('trackMedicosById', () => {
        it('Should return tracked Medicos primary key', () => {
          const entity = { id: 123 };
          const trackResult = comp.trackMedicosById(0, entity);
          expect(trackResult).toEqual(entity.id);
        });
      });
    });
  });
});
