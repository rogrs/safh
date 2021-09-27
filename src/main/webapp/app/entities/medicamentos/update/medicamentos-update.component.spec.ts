jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject } from 'rxjs';

import { MedicamentosService } from '../service/medicamentos.service';
import { IMedicamentos, Medicamentos } from '../medicamentos.model';
import { IPosologias } from 'app/entities/posologias/posologias.model';
import { PosologiasService } from 'app/entities/posologias/service/posologias.service';
import { IFabricantes } from 'app/entities/fabricantes/fabricantes.model';
import { FabricantesService } from 'app/entities/fabricantes/service/fabricantes.service';

import { MedicamentosUpdateComponent } from './medicamentos-update.component';

describe('Component Tests', () => {
  describe('Medicamentos Management Update Component', () => {
    let comp: MedicamentosUpdateComponent;
    let fixture: ComponentFixture<MedicamentosUpdateComponent>;
    let activatedRoute: ActivatedRoute;
    let medicamentosService: MedicamentosService;
    let posologiasService: PosologiasService;
    let fabricantesService: FabricantesService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [MedicamentosUpdateComponent],
        providers: [FormBuilder, ActivatedRoute],
      })
        .overrideTemplate(MedicamentosUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(MedicamentosUpdateComponent);
      activatedRoute = TestBed.inject(ActivatedRoute);
      medicamentosService = TestBed.inject(MedicamentosService);
      posologiasService = TestBed.inject(PosologiasService);
      fabricantesService = TestBed.inject(FabricantesService);

      comp = fixture.componentInstance;
    });

    describe('ngOnInit', () => {
      it('Should call Posologias query and add missing value', () => {
        const medicamentos: IMedicamentos = { id: 456 };
        const posologiaPadrao: IPosologias = { id: 33096 };
        medicamentos.posologiaPadrao = posologiaPadrao;

        const posologiasCollection: IPosologias[] = [{ id: 12457 }];
        spyOn(posologiasService, 'query').and.returnValue(of(new HttpResponse({ body: posologiasCollection })));
        const additionalPosologias = [posologiaPadrao];
        const expectedCollection: IPosologias[] = [...additionalPosologias, ...posologiasCollection];
        spyOn(posologiasService, 'addPosologiasToCollectionIfMissing').and.returnValue(expectedCollection);

        activatedRoute.data = of({ medicamentos });
        comp.ngOnInit();

        expect(posologiasService.query).toHaveBeenCalled();
        expect(posologiasService.addPosologiasToCollectionIfMissing).toHaveBeenCalledWith(posologiasCollection, ...additionalPosologias);
        expect(comp.posologiasSharedCollection).toEqual(expectedCollection);
      });

      it('Should call Fabricantes query and add missing value', () => {
        const medicamentos: IMedicamentos = { id: 456 };
        const fabricantes: IFabricantes = { id: 95108 };
        medicamentos.fabricantes = fabricantes;

        const fabricantesCollection: IFabricantes[] = [{ id: 30401 }];
        spyOn(fabricantesService, 'query').and.returnValue(of(new HttpResponse({ body: fabricantesCollection })));
        const additionalFabricantes = [fabricantes];
        const expectedCollection: IFabricantes[] = [...additionalFabricantes, ...fabricantesCollection];
        spyOn(fabricantesService, 'addFabricantesToCollectionIfMissing').and.returnValue(expectedCollection);

        activatedRoute.data = of({ medicamentos });
        comp.ngOnInit();

        expect(fabricantesService.query).toHaveBeenCalled();
        expect(fabricantesService.addFabricantesToCollectionIfMissing).toHaveBeenCalledWith(
          fabricantesCollection,
          ...additionalFabricantes
        );
        expect(comp.fabricantesSharedCollection).toEqual(expectedCollection);
      });

      it('Should update editForm', () => {
        const medicamentos: IMedicamentos = { id: 456 };
        const posologiaPadrao: IPosologias = { id: 83200 };
        medicamentos.posologiaPadrao = posologiaPadrao;
        const fabricantes: IFabricantes = { id: 183 };
        medicamentos.fabricantes = fabricantes;

        activatedRoute.data = of({ medicamentos });
        comp.ngOnInit();

        expect(comp.editForm.value).toEqual(expect.objectContaining(medicamentos));
        expect(comp.posologiasSharedCollection).toContain(posologiaPadrao);
        expect(comp.fabricantesSharedCollection).toContain(fabricantes);
      });
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', () => {
        // GIVEN
        const saveSubject = new Subject();
        const medicamentos = { id: 123 };
        spyOn(medicamentosService, 'update').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ medicamentos });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: medicamentos }));
        saveSubject.complete();

        // THEN
        expect(comp.previousState).toHaveBeenCalled();
        expect(medicamentosService.update).toHaveBeenCalledWith(medicamentos);
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', () => {
        // GIVEN
        const saveSubject = new Subject();
        const medicamentos = new Medicamentos();
        spyOn(medicamentosService, 'create').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ medicamentos });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: medicamentos }));
        saveSubject.complete();

        // THEN
        expect(medicamentosService.create).toHaveBeenCalledWith(medicamentos);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).toHaveBeenCalled();
      });

      it('Should set isSaving to false on error', () => {
        // GIVEN
        const saveSubject = new Subject();
        const medicamentos = { id: 123 };
        spyOn(medicamentosService, 'update').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ medicamentos });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.error('This is an error!');

        // THEN
        expect(medicamentosService.update).toHaveBeenCalledWith(medicamentos);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).not.toHaveBeenCalled();
      });
    });

    describe('Tracking relationships identifiers', () => {
      describe('trackPosologiasById', () => {
        it('Should return tracked Posologias primary key', () => {
          const entity = { id: 123 };
          const trackResult = comp.trackPosologiasById(0, entity);
          expect(trackResult).toEqual(entity.id);
        });
      });

      describe('trackFabricantesById', () => {
        it('Should return tracked Fabricantes primary key', () => {
          const entity = { id: 123 };
          const trackResult = comp.trackFabricantesById(0, entity);
          expect(trackResult).toEqual(entity.id);
        });
      });
    });
  });
});
