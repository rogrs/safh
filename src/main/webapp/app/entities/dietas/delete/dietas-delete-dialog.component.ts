import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IDietas } from '../dietas.model';
import { DietasService } from '../service/dietas.service';

@Component({
  templateUrl: './dietas-delete-dialog.component.html',
})
export class DietasDeleteDialogComponent {
  dietas?: IDietas;

  constructor(protected dietasService: DietasService, public activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.dietasService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
