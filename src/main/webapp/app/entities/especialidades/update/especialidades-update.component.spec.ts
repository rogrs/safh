jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject } from 'rxjs';

import { EspecialidadesService } from '../service/especialidades.service';
import { IEspecialidades, Especialidades } from '../especialidades.model';

import { EspecialidadesUpdateComponent } from './especialidades-update.component';

describe('Component Tests', () => {
  describe('Especialidades Management Update Component', () => {
    let comp: EspecialidadesUpdateComponent;
    let fixture: ComponentFixture<EspecialidadesUpdateComponent>;
    let activatedRoute: ActivatedRoute;
    let especialidadesService: EspecialidadesService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [EspecialidadesUpdateComponent],
        providers: [FormBuilder, ActivatedRoute],
      })
        .overrideTemplate(EspecialidadesUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(EspecialidadesUpdateComponent);
      activatedRoute = TestBed.inject(ActivatedRoute);
      especialidadesService = TestBed.inject(EspecialidadesService);

      comp = fixture.componentInstance;
    });

    describe('ngOnInit', () => {
      it('Should update editForm', () => {
        const especialidades: IEspecialidades = { id: 456 };

        activatedRoute.data = of({ especialidades });
        comp.ngOnInit();

        expect(comp.editForm.value).toEqual(expect.objectContaining(especialidades));
      });
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', () => {
        // GIVEN
        const saveSubject = new Subject();
        const especialidades = { id: 123 };
        spyOn(especialidadesService, 'update').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ especialidades });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: especialidades }));
        saveSubject.complete();

        // THEN
        expect(comp.previousState).toHaveBeenCalled();
        expect(especialidadesService.update).toHaveBeenCalledWith(especialidades);
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', () => {
        // GIVEN
        const saveSubject = new Subject();
        const especialidades = new Especialidades();
        spyOn(especialidadesService, 'create').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ especialidades });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: especialidades }));
        saveSubject.complete();

        // THEN
        expect(especialidadesService.create).toHaveBeenCalledWith(especialidades);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).toHaveBeenCalled();
      });

      it('Should set isSaving to false on error', () => {
        // GIVEN
        const saveSubject = new Subject();
        const especialidades = { id: 123 };
        spyOn(especialidadesService, 'update').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ especialidades });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.error('This is an error!');

        // THEN
        expect(especialidadesService.update).toHaveBeenCalledWith(especialidades);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).not.toHaveBeenCalled();
      });
    });
  });
});
