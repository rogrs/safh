import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IFabricantes } from '../fabricantes.model';
import { FabricantesService } from '../service/fabricantes.service';

@Component({
  templateUrl: './fabricantes-delete-dialog.component.html',
})
export class FabricantesDeleteDialogComponent {
  fabricantes?: IFabricantes;

  constructor(protected fabricantesService: FabricantesService, public activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.fabricantesService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
