import { NgModule } from '@angular/core';

import { SharedModule } from 'app/shared/shared.module';
import { PacientesComponent } from './list/pacientes.component';
import { PacientesDetailComponent } from './detail/pacientes-detail.component';
import { PacientesUpdateComponent } from './update/pacientes-update.component';
import { PacientesDeleteDialogComponent } from './delete/pacientes-delete-dialog.component';
import { PacientesRoutingModule } from './route/pacientes-routing.module';

@NgModule({
  imports: [SharedModule, PacientesRoutingModule],
  declarations: [PacientesComponent, PacientesDetailComponent, PacientesUpdateComponent, PacientesDeleteDialogComponent],
  entryComponents: [PacientesDeleteDialogComponent],
})
export class PacientesModule {}
