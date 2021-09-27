import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IPrescricoes } from '../prescricoes.model';
import { PrescricoesService } from '../service/prescricoes.service';

@Component({
  templateUrl: './prescricoes-delete-dialog.component.html',
})
export class PrescricoesDeleteDialogComponent {
  prescricoes?: IPrescricoes;

  constructor(protected prescricoesService: PrescricoesService, public activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.prescricoesService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
