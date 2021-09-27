jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject } from 'rxjs';

import { InternacoesDetalhesService } from '../service/internacoes-detalhes.service';
import { IInternacoesDetalhes, InternacoesDetalhes } from '../internacoes-detalhes.model';
import { IInternacoes } from 'app/entities/internacoes/internacoes.model';
import { InternacoesService } from 'app/entities/internacoes/service/internacoes.service';
import { IDietas } from 'app/entities/dietas/dietas.model';
import { DietasService } from 'app/entities/dietas/service/dietas.service';
import { IPrescricoes } from 'app/entities/prescricoes/prescricoes.model';
import { PrescricoesService } from 'app/entities/prescricoes/service/prescricoes.service';
import { IPosologias } from 'app/entities/posologias/posologias.model';
import { PosologiasService } from 'app/entities/posologias/service/posologias.service';

import { InternacoesDetalhesUpdateComponent } from './internacoes-detalhes-update.component';

describe('Component Tests', () => {
  describe('InternacoesDetalhes Management Update Component', () => {
    let comp: InternacoesDetalhesUpdateComponent;
    let fixture: ComponentFixture<InternacoesDetalhesUpdateComponent>;
    let activatedRoute: ActivatedRoute;
    let internacoesDetalhesService: InternacoesDetalhesService;
    let internacoesService: InternacoesService;
    let dietasService: DietasService;
    let prescricoesService: PrescricoesService;
    let posologiasService: PosologiasService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [InternacoesDetalhesUpdateComponent],
        providers: [FormBuilder, ActivatedRoute],
      })
        .overrideTemplate(InternacoesDetalhesUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(InternacoesDetalhesUpdateComponent);
      activatedRoute = TestBed.inject(ActivatedRoute);
      internacoesDetalhesService = TestBed.inject(InternacoesDetalhesService);
      internacoesService = TestBed.inject(InternacoesService);
      dietasService = TestBed.inject(DietasService);
      prescricoesService = TestBed.inject(PrescricoesService);
      posologiasService = TestBed.inject(PosologiasService);

      comp = fixture.componentInstance;
    });

    describe('ngOnInit', () => {
      it('Should call Internacoes query and add missing value', () => {
        const internacoesDetalhes: IInternacoesDetalhes = { id: 456 };
        const internacoes: IInternacoes = { id: 16249 };
        internacoesDetalhes.internacoes = internacoes;

        const internacoesCollection: IInternacoes[] = [{ id: 47389 }];
        spyOn(internacoesService, 'query').and.returnValue(of(new HttpResponse({ body: internacoesCollection })));
        const additionalInternacoes = [internacoes];
        const expectedCollection: IInternacoes[] = [...additionalInternacoes, ...internacoesCollection];
        spyOn(internacoesService, 'addInternacoesToCollectionIfMissing').and.returnValue(expectedCollection);

        activatedRoute.data = of({ internacoesDetalhes });
        comp.ngOnInit();

        expect(internacoesService.query).toHaveBeenCalled();
        expect(internacoesService.addInternacoesToCollectionIfMissing).toHaveBeenCalledWith(
          internacoesCollection,
          ...additionalInternacoes
        );
        expect(comp.internacoesSharedCollection).toEqual(expectedCollection);
      });

      it('Should call Dietas query and add missing value', () => {
        const internacoesDetalhes: IInternacoesDetalhes = { id: 456 };
        const dietas: IDietas = { id: 66695 };
        internacoesDetalhes.dietas = dietas;

        const dietasCollection: IDietas[] = [{ id: 13302 }];
        spyOn(dietasService, 'query').and.returnValue(of(new HttpResponse({ body: dietasCollection })));
        const additionalDietas = [dietas];
        const expectedCollection: IDietas[] = [...additionalDietas, ...dietasCollection];
        spyOn(dietasService, 'addDietasToCollectionIfMissing').and.returnValue(expectedCollection);

        activatedRoute.data = of({ internacoesDetalhes });
        comp.ngOnInit();

        expect(dietasService.query).toHaveBeenCalled();
        expect(dietasService.addDietasToCollectionIfMissing).toHaveBeenCalledWith(dietasCollection, ...additionalDietas);
        expect(comp.dietasSharedCollection).toEqual(expectedCollection);
      });

      it('Should call Prescricoes query and add missing value', () => {
        const internacoesDetalhes: IInternacoesDetalhes = { id: 456 };
        const prescricoes: IPrescricoes = { id: 32848 };
        internacoesDetalhes.prescricoes = prescricoes;

        const prescricoesCollection: IPrescricoes[] = [{ id: 86226 }];
        spyOn(prescricoesService, 'query').and.returnValue(of(new HttpResponse({ body: prescricoesCollection })));
        const additionalPrescricoes = [prescricoes];
        const expectedCollection: IPrescricoes[] = [...additionalPrescricoes, ...prescricoesCollection];
        spyOn(prescricoesService, 'addPrescricoesToCollectionIfMissing').and.returnValue(expectedCollection);

        activatedRoute.data = of({ internacoesDetalhes });
        comp.ngOnInit();

        expect(prescricoesService.query).toHaveBeenCalled();
        expect(prescricoesService.addPrescricoesToCollectionIfMissing).toHaveBeenCalledWith(
          prescricoesCollection,
          ...additionalPrescricoes
        );
        expect(comp.prescricoesSharedCollection).toEqual(expectedCollection);
      });

      it('Should call Posologias query and add missing value', () => {
        const internacoesDetalhes: IInternacoesDetalhes = { id: 456 };
        const posologias: IPosologias = { id: 55432 };
        internacoesDetalhes.posologias = posologias;

        const posologiasCollection: IPosologias[] = [{ id: 72100 }];
        spyOn(posologiasService, 'query').and.returnValue(of(new HttpResponse({ body: posologiasCollection })));
        const additionalPosologias = [posologias];
        const expectedCollection: IPosologias[] = [...additionalPosologias, ...posologiasCollection];
        spyOn(posologiasService, 'addPosologiasToCollectionIfMissing').and.returnValue(expectedCollection);

        activatedRoute.data = of({ internacoesDetalhes });
        comp.ngOnInit();

        expect(posologiasService.query).toHaveBeenCalled();
        expect(posologiasService.addPosologiasToCollectionIfMissing).toHaveBeenCalledWith(posologiasCollection, ...additionalPosologias);
        expect(comp.posologiasSharedCollection).toEqual(expectedCollection);
      });

      it('Should update editForm', () => {
        const internacoesDetalhes: IInternacoesDetalhes = { id: 456 };
        const internacoes: IInternacoes = { id: 41091 };
        internacoesDetalhes.internacoes = internacoes;
        const dietas: IDietas = { id: 98789 };
        internacoesDetalhes.dietas = dietas;
        const prescricoes: IPrescricoes = { id: 20419 };
        internacoesDetalhes.prescricoes = prescricoes;
        const posologias: IPosologias = { id: 27635 };
        internacoesDetalhes.posologias = posologias;

        activatedRoute.data = of({ internacoesDetalhes });
        comp.ngOnInit();

        expect(comp.editForm.value).toEqual(expect.objectContaining(internacoesDetalhes));
        expect(comp.internacoesSharedCollection).toContain(internacoes);
        expect(comp.dietasSharedCollection).toContain(dietas);
        expect(comp.prescricoesSharedCollection).toContain(prescricoes);
        expect(comp.posologiasSharedCollection).toContain(posologias);
      });
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', () => {
        // GIVEN
        const saveSubject = new Subject();
        const internacoesDetalhes = { id: 123 };
        spyOn(internacoesDetalhesService, 'update').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ internacoesDetalhes });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: internacoesDetalhes }));
        saveSubject.complete();

        // THEN
        expect(comp.previousState).toHaveBeenCalled();
        expect(internacoesDetalhesService.update).toHaveBeenCalledWith(internacoesDetalhes);
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', () => {
        // GIVEN
        const saveSubject = new Subject();
        const internacoesDetalhes = new InternacoesDetalhes();
        spyOn(internacoesDetalhesService, 'create').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ internacoesDetalhes });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: internacoesDetalhes }));
        saveSubject.complete();

        // THEN
        expect(internacoesDetalhesService.create).toHaveBeenCalledWith(internacoesDetalhes);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).toHaveBeenCalled();
      });

      it('Should set isSaving to false on error', () => {
        // GIVEN
        const saveSubject = new Subject();
        const internacoesDetalhes = { id: 123 };
        spyOn(internacoesDetalhesService, 'update').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ internacoesDetalhes });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.error('This is an error!');

        // THEN
        expect(internacoesDetalhesService.update).toHaveBeenCalledWith(internacoesDetalhes);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).not.toHaveBeenCalled();
      });
    });

    describe('Tracking relationships identifiers', () => {
      describe('trackInternacoesById', () => {
        it('Should return tracked Internacoes primary key', () => {
          const entity = { id: 123 };
          const trackResult = comp.trackInternacoesById(0, entity);
          expect(trackResult).toEqual(entity.id);
        });
      });

      describe('trackDietasById', () => {
        it('Should return tracked Dietas primary key', () => {
          const entity = { id: 123 };
          const trackResult = comp.trackDietasById(0, entity);
          expect(trackResult).toEqual(entity.id);
        });
      });

      describe('trackPrescricoesById', () => {
        it('Should return tracked Prescricoes primary key', () => {
          const entity = { id: 123 };
          const trackResult = comp.trackPrescricoesById(0, entity);
          expect(trackResult).toEqual(entity.id);
        });
      });

      describe('trackPosologiasById', () => {
        it('Should return tracked Posologias primary key', () => {
          const entity = { id: 123 };
          const trackResult = comp.trackPosologiasById(0, entity);
          expect(trackResult).toEqual(entity.id);
        });
      });
    });
  });
});
