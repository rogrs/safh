import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IPosologias } from '../posologias.model';
import { PosologiasService } from '../service/posologias.service';

@Component({
  templateUrl: './posologias-delete-dialog.component.html',
})
export class PosologiasDeleteDialogComponent {
  posologias?: IPosologias;

  constructor(protected posologiasService: PosologiasService, public activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.posologiasService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
