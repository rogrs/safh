jest.mock('@ng-bootstrap/ng-bootstrap');

import { ComponentFixture, TestBed, inject, fakeAsync, tick } from '@angular/core/testing';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { of } from 'rxjs';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { EnfermariasService } from '../service/enfermarias.service';

import { EnfermariasDeleteDialogComponent } from './enfermarias-delete-dialog.component';

describe('Component Tests', () => {
  describe('Enfermarias Management Delete Component', () => {
    let comp: EnfermariasDeleteDialogComponent;
    let fixture: ComponentFixture<EnfermariasDeleteDialogComponent>;
    let service: EnfermariasService;
    let mockActiveModal: NgbActiveModal;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [EnfermariasDeleteDialogComponent],
        providers: [NgbActiveModal],
      })
        .overrideTemplate(EnfermariasDeleteDialogComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(EnfermariasDeleteDialogComponent);
      comp = fixture.componentInstance;
      service = TestBed.inject(EnfermariasService);
      mockActiveModal = TestBed.inject(NgbActiveModal);
    });

    describe('confirmDelete', () => {
      it('Should call delete service on confirmDelete', inject(
        [],
        fakeAsync(() => {
          // GIVEN
          spyOn(service, 'delete').and.returnValue(of({}));

          // WHEN
          comp.confirmDelete(123);
          tick();

          // THEN
          expect(service.delete).toHaveBeenCalledWith(123);
          expect(mockActiveModal.close).toHaveBeenCalledWith('deleted');
        })
      ));

      it('Should not call delete service on clear', () => {
        // GIVEN
        spyOn(service, 'delete');

        // WHEN
        comp.cancel();

        // THEN
        expect(service.delete).not.toHaveBeenCalled();
        expect(mockActiveModal.close).not.toHaveBeenCalled();
        expect(mockActiveModal.dismiss).toHaveBeenCalled();
      });
    });
  });
});
