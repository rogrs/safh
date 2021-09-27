jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject } from 'rxjs';

import { LeitosService } from '../service/leitos.service';
import { ILeitos, Leitos } from '../leitos.model';

import { LeitosUpdateComponent } from './leitos-update.component';

describe('Component Tests', () => {
  describe('Leitos Management Update Component', () => {
    let comp: LeitosUpdateComponent;
    let fixture: ComponentFixture<LeitosUpdateComponent>;
    let activatedRoute: ActivatedRoute;
    let leitosService: LeitosService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [LeitosUpdateComponent],
        providers: [FormBuilder, ActivatedRoute],
      })
        .overrideTemplate(LeitosUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(LeitosUpdateComponent);
      activatedRoute = TestBed.inject(ActivatedRoute);
      leitosService = TestBed.inject(LeitosService);

      comp = fixture.componentInstance;
    });

    describe('ngOnInit', () => {
      it('Should update editForm', () => {
        const leitos: ILeitos = { id: 456 };

        activatedRoute.data = of({ leitos });
        comp.ngOnInit();

        expect(comp.editForm.value).toEqual(expect.objectContaining(leitos));
      });
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', () => {
        // GIVEN
        const saveSubject = new Subject();
        const leitos = { id: 123 };
        spyOn(leitosService, 'update').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ leitos });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: leitos }));
        saveSubject.complete();

        // THEN
        expect(comp.previousState).toHaveBeenCalled();
        expect(leitosService.update).toHaveBeenCalledWith(leitos);
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', () => {
        // GIVEN
        const saveSubject = new Subject();
        const leitos = new Leitos();
        spyOn(leitosService, 'create').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ leitos });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: leitos }));
        saveSubject.complete();

        // THEN
        expect(leitosService.create).toHaveBeenCalledWith(leitos);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).toHaveBeenCalled();
      });

      it('Should set isSaving to false on error', () => {
        // GIVEN
        const saveSubject = new Subject();
        const leitos = { id: 123 };
        spyOn(leitosService, 'update').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ leitos });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.error('This is an error!');

        // THEN
        expect(leitosService.update).toHaveBeenCalledWith(leitos);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).not.toHaveBeenCalled();
      });
    });
  });
});
