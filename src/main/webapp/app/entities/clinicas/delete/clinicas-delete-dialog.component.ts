import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IClinicas } from '../clinicas.model';
import { ClinicasService } from '../service/clinicas.service';

@Component({
  templateUrl: './clinicas-delete-dialog.component.html',
})
export class ClinicasDeleteDialogComponent {
  clinicas?: IClinicas;

  constructor(protected clinicasService: ClinicasService, public activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.clinicasService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
