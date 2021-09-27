import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IPacientes } from '../pacientes.model';
import { PacientesService } from '../service/pacientes.service';

@Component({
  templateUrl: './pacientes-delete-dialog.component.html',
})
export class PacientesDeleteDialogComponent {
  pacientes?: IPacientes;

  constructor(protected pacientesService: PacientesService, public activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.pacientesService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
