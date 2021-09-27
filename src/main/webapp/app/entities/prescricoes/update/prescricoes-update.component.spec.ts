jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject } from 'rxjs';

import { PrescricoesService } from '../service/prescricoes.service';
import { IPrescricoes, Prescricoes } from '../prescricoes.model';

import { PrescricoesUpdateComponent } from './prescricoes-update.component';

describe('Component Tests', () => {
  describe('Prescricoes Management Update Component', () => {
    let comp: PrescricoesUpdateComponent;
    let fixture: ComponentFixture<PrescricoesUpdateComponent>;
    let activatedRoute: ActivatedRoute;
    let prescricoesService: PrescricoesService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [PrescricoesUpdateComponent],
        providers: [FormBuilder, ActivatedRoute],
      })
        .overrideTemplate(PrescricoesUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(PrescricoesUpdateComponent);
      activatedRoute = TestBed.inject(ActivatedRoute);
      prescricoesService = TestBed.inject(PrescricoesService);

      comp = fixture.componentInstance;
    });

    describe('ngOnInit', () => {
      it('Should update editForm', () => {
        const prescricoes: IPrescricoes = { id: 456 };

        activatedRoute.data = of({ prescricoes });
        comp.ngOnInit();

        expect(comp.editForm.value).toEqual(expect.objectContaining(prescricoes));
      });
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', () => {
        // GIVEN
        const saveSubject = new Subject();
        const prescricoes = { id: 123 };
        spyOn(prescricoesService, 'update').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ prescricoes });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: prescricoes }));
        saveSubject.complete();

        // THEN
        expect(comp.previousState).toHaveBeenCalled();
        expect(prescricoesService.update).toHaveBeenCalledWith(prescricoes);
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', () => {
        // GIVEN
        const saveSubject = new Subject();
        const prescricoes = new Prescricoes();
        spyOn(prescricoesService, 'create').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ prescricoes });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: prescricoes }));
        saveSubject.complete();

        // THEN
        expect(prescricoesService.create).toHaveBeenCalledWith(prescricoes);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).toHaveBeenCalled();
      });

      it('Should set isSaving to false on error', () => {
        // GIVEN
        const saveSubject = new Subject();
        const prescricoes = { id: 123 };
        spyOn(prescricoesService, 'update').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ prescricoes });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.error('This is an error!');

        // THEN
        expect(prescricoesService.update).toHaveBeenCalledWith(prescricoes);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).not.toHaveBeenCalled();
      });
    });
  });
});
