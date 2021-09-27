import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IEspecialidades } from '../especialidades.model';
import { EspecialidadesService } from '../service/especialidades.service';

@Component({
  templateUrl: './especialidades-delete-dialog.component.html',
})
export class EspecialidadesDeleteDialogComponent {
  especialidades?: IEspecialidades;

  constructor(protected especialidadesService: EspecialidadesService, public activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.especialidadesService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
