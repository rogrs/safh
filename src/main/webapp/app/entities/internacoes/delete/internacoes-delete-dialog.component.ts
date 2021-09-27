import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IInternacoes } from '../internacoes.model';
import { InternacoesService } from '../service/internacoes.service';

@Component({
  templateUrl: './internacoes-delete-dialog.component.html',
})
export class InternacoesDeleteDialogComponent {
  internacoes?: IInternacoes;

  constructor(protected internacoesService: InternacoesService, public activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.internacoesService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
