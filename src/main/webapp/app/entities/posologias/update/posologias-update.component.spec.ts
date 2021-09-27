jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject } from 'rxjs';

import { PosologiasService } from '../service/posologias.service';
import { IPosologias, Posologias } from '../posologias.model';

import { PosologiasUpdateComponent } from './posologias-update.component';

describe('Component Tests', () => {
  describe('Posologias Management Update Component', () => {
    let comp: PosologiasUpdateComponent;
    let fixture: ComponentFixture<PosologiasUpdateComponent>;
    let activatedRoute: ActivatedRoute;
    let posologiasService: PosologiasService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [PosologiasUpdateComponent],
        providers: [FormBuilder, ActivatedRoute],
      })
        .overrideTemplate(PosologiasUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(PosologiasUpdateComponent);
      activatedRoute = TestBed.inject(ActivatedRoute);
      posologiasService = TestBed.inject(PosologiasService);

      comp = fixture.componentInstance;
    });

    describe('ngOnInit', () => {
      it('Should update editForm', () => {
        const posologias: IPosologias = { id: 456 };

        activatedRoute.data = of({ posologias });
        comp.ngOnInit();

        expect(comp.editForm.value).toEqual(expect.objectContaining(posologias));
      });
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', () => {
        // GIVEN
        const saveSubject = new Subject();
        const posologias = { id: 123 };
        spyOn(posologiasService, 'update').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ posologias });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: posologias }));
        saveSubject.complete();

        // THEN
        expect(comp.previousState).toHaveBeenCalled();
        expect(posologiasService.update).toHaveBeenCalledWith(posologias);
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', () => {
        // GIVEN
        const saveSubject = new Subject();
        const posologias = new Posologias();
        spyOn(posologiasService, 'create').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ posologias });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: posologias }));
        saveSubject.complete();

        // THEN
        expect(posologiasService.create).toHaveBeenCalledWith(posologias);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).toHaveBeenCalled();
      });

      it('Should set isSaving to false on error', () => {
        // GIVEN
        const saveSubject = new Subject();
        const posologias = { id: 123 };
        spyOn(posologiasService, 'update').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ posologias });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.error('This is an error!');

        // THEN
        expect(posologiasService.update).toHaveBeenCalledWith(posologias);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).not.toHaveBeenCalled();
      });
    });
  });
});
