import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { ILeitos } from '../leitos.model';
import { LeitosService } from '../service/leitos.service';

@Component({
  templateUrl: './leitos-delete-dialog.component.html',
})
export class LeitosDeleteDialogComponent {
  leitos?: ILeitos;

  constructor(protected leitosService: LeitosService, public activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.leitosService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
