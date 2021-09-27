import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IMedicamentos } from '../medicamentos.model';
import { MedicamentosService } from '../service/medicamentos.service';

@Component({
  templateUrl: './medicamentos-delete-dialog.component.html',
})
export class MedicamentosDeleteDialogComponent {
  medicamentos?: IMedicamentos;

  constructor(protected medicamentosService: MedicamentosService, public activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.medicamentosService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
