import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IInternacoesDetalhes } from '../internacoes-detalhes.model';
import { InternacoesDetalhesService } from '../service/internacoes-detalhes.service';

@Component({
  templateUrl: './internacoes-detalhes-delete-dialog.component.html',
})
export class InternacoesDetalhesDeleteDialogComponent {
  internacoesDetalhes?: IInternacoesDetalhes;

  constructor(protected internacoesDetalhesService: InternacoesDetalhesService, public activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.internacoesDetalhesService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
