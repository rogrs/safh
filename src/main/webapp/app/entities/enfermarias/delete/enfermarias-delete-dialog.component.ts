import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IEnfermarias } from '../enfermarias.model';
import { EnfermariasService } from '../service/enfermarias.service';

@Component({
  templateUrl: './enfermarias-delete-dialog.component.html',
})
export class EnfermariasDeleteDialogComponent {
  enfermarias?: IEnfermarias;

  constructor(protected enfermariasService: EnfermariasService, public activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.enfermariasService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
