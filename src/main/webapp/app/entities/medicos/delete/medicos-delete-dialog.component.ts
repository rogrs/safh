import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IMedicos } from '../medicos.model';
import { MedicosService } from '../service/medicos.service';

@Component({
  templateUrl: './medicos-delete-dialog.component.html',
})
export class MedicosDeleteDialogComponent {
  medicos?: IMedicos;

  constructor(protected medicosService: MedicosService, public activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.medicosService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
