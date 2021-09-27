jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject } from 'rxjs';

import { PacientesService } from '../service/pacientes.service';
import { IPacientes, Pacientes } from '../pacientes.model';
import { IClinicas } from 'app/entities/clinicas/clinicas.model';
import { ClinicasService } from 'app/entities/clinicas/service/clinicas.service';
import { IEnfermarias } from 'app/entities/enfermarias/enfermarias.model';
import { EnfermariasService } from 'app/entities/enfermarias/service/enfermarias.service';
import { ILeitos } from 'app/entities/leitos/leitos.model';
import { LeitosService } from 'app/entities/leitos/service/leitos.service';

import { PacientesUpdateComponent } from './pacientes-update.component';

describe('Component Tests', () => {
  describe('Pacientes Management Update Component', () => {
    let comp: PacientesUpdateComponent;
    let fixture: ComponentFixture<PacientesUpdateComponent>;
    let activatedRoute: ActivatedRoute;
    let pacientesService: PacientesService;
    let clinicasService: ClinicasService;
    let enfermariasService: EnfermariasService;
    let leitosService: LeitosService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [PacientesUpdateComponent],
        providers: [FormBuilder, ActivatedRoute],
      })
        .overrideTemplate(PacientesUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(PacientesUpdateComponent);
      activatedRoute = TestBed.inject(ActivatedRoute);
      pacientesService = TestBed.inject(PacientesService);
      clinicasService = TestBed.inject(ClinicasService);
      enfermariasService = TestBed.inject(EnfermariasService);
      leitosService = TestBed.inject(LeitosService);

      comp = fixture.componentInstance;
    });

    describe('ngOnInit', () => {
      it('Should call Clinicas query and add missing value', () => {
        const pacientes: IPacientes = { id: 456 };
        const clinicas: IClinicas = { id: 30995 };
        pacientes.clinicas = clinicas;

        const clinicasCollection: IClinicas[] = [{ id: 98922 }];
        spyOn(clinicasService, 'query').and.returnValue(of(new HttpResponse({ body: clinicasCollection })));
        const additionalClinicas = [clinicas];
        const expectedCollection: IClinicas[] = [...additionalClinicas, ...clinicasCollection];
        spyOn(clinicasService, 'addClinicasToCollectionIfMissing').and.returnValue(expectedCollection);

        activatedRoute.data = of({ pacientes });
        comp.ngOnInit();

        expect(clinicasService.query).toHaveBeenCalled();
        expect(clinicasService.addClinicasToCollectionIfMissing).toHaveBeenCalledWith(clinicasCollection, ...additionalClinicas);
        expect(comp.clinicasSharedCollection).toEqual(expectedCollection);
      });

      it('Should call Enfermarias query and add missing value', () => {
        const pacientes: IPacientes = { id: 456 };
        const enfermarias: IEnfermarias = { id: 41404 };
        pacientes.enfermarias = enfermarias;

        const enfermariasCollection: IEnfermarias[] = [{ id: 90421 }];
        spyOn(enfermariasService, 'query').and.returnValue(of(new HttpResponse({ body: enfermariasCollection })));
        const additionalEnfermarias = [enfermarias];
        const expectedCollection: IEnfermarias[] = [...additionalEnfermarias, ...enfermariasCollection];
        spyOn(enfermariasService, 'addEnfermariasToCollectionIfMissing').and.returnValue(expectedCollection);

        activatedRoute.data = of({ pacientes });
        comp.ngOnInit();

        expect(enfermariasService.query).toHaveBeenCalled();
        expect(enfermariasService.addEnfermariasToCollectionIfMissing).toHaveBeenCalledWith(
          enfermariasCollection,
          ...additionalEnfermarias
        );
        expect(comp.enfermariasSharedCollection).toEqual(expectedCollection);
      });

      it('Should call Leitos query and add missing value', () => {
        const pacientes: IPacientes = { id: 456 };
        const leitos: ILeitos = { id: 96686 };
        pacientes.leitos = leitos;

        const leitosCollection: ILeitos[] = [{ id: 80260 }];
        spyOn(leitosService, 'query').and.returnValue(of(new HttpResponse({ body: leitosCollection })));
        const additionalLeitos = [leitos];
        const expectedCollection: ILeitos[] = [...additionalLeitos, ...leitosCollection];
        spyOn(leitosService, 'addLeitosToCollectionIfMissing').and.returnValue(expectedCollection);

        activatedRoute.data = of({ pacientes });
        comp.ngOnInit();

        expect(leitosService.query).toHaveBeenCalled();
        expect(leitosService.addLeitosToCollectionIfMissing).toHaveBeenCalledWith(leitosCollection, ...additionalLeitos);
        expect(comp.leitosSharedCollection).toEqual(expectedCollection);
      });

      it('Should update editForm', () => {
        const pacientes: IPacientes = { id: 456 };
        const clinicas: IClinicas = { id: 88639 };
        pacientes.clinicas = clinicas;
        const enfermarias: IEnfermarias = { id: 28027 };
        pacientes.enfermarias = enfermarias;
        const leitos: ILeitos = { id: 71010 };
        pacientes.leitos = leitos;

        activatedRoute.data = of({ pacientes });
        comp.ngOnInit();

        expect(comp.editForm.value).toEqual(expect.objectContaining(pacientes));
        expect(comp.clinicasSharedCollection).toContain(clinicas);
        expect(comp.enfermariasSharedCollection).toContain(enfermarias);
        expect(comp.leitosSharedCollection).toContain(leitos);
      });
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', () => {
        // GIVEN
        const saveSubject = new Subject();
        const pacientes = { id: 123 };
        spyOn(pacientesService, 'update').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ pacientes });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: pacientes }));
        saveSubject.complete();

        // THEN
        expect(comp.previousState).toHaveBeenCalled();
        expect(pacientesService.update).toHaveBeenCalledWith(pacientes);
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', () => {
        // GIVEN
        const saveSubject = new Subject();
        const pacientes = new Pacientes();
        spyOn(pacientesService, 'create').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ pacientes });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: pacientes }));
        saveSubject.complete();

        // THEN
        expect(pacientesService.create).toHaveBeenCalledWith(pacientes);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).toHaveBeenCalled();
      });

      it('Should set isSaving to false on error', () => {
        // GIVEN
        const saveSubject = new Subject();
        const pacientes = { id: 123 };
        spyOn(pacientesService, 'update').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ pacientes });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.error('This is an error!');

        // THEN
        expect(pacientesService.update).toHaveBeenCalledWith(pacientes);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).not.toHaveBeenCalled();
      });
    });

    describe('Tracking relationships identifiers', () => {
      describe('trackClinicasById', () => {
        it('Should return tracked Clinicas primary key', () => {
          const entity = { id: 123 };
          const trackResult = comp.trackClinicasById(0, entity);
          expect(trackResult).toEqual(entity.id);
        });
      });

      describe('trackEnfermariasById', () => {
        it('Should return tracked Enfermarias primary key', () => {
          const entity = { id: 123 };
          const trackResult = comp.trackEnfermariasById(0, entity);
          expect(trackResult).toEqual(entity.id);
        });
      });

      describe('trackLeitosById', () => {
        it('Should return tracked Leitos primary key', () => {
          const entity = { id: 123 };
          const trackResult = comp.trackLeitosById(0, entity);
          expect(trackResult).toEqual(entity.id);
        });
      });
    });
  });
});
