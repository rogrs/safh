import { NgModule } from '@angular/core';

import { SharedModule } from 'app/shared/shared.module';
import { EspecialidadesComponent } from './list/especialidades.component';
import { EspecialidadesDetailComponent } from './detail/especialidades-detail.component';
import { EspecialidadesUpdateComponent } from './update/especialidades-update.component';
import { EspecialidadesDeleteDialogComponent } from './delete/especialidades-delete-dialog.component';
import { EspecialidadesRoutingModule } from './route/especialidades-routing.module';

@NgModule({
  imports: [SharedModule, EspecialidadesRoutingModule],
  declarations: [
    EspecialidadesComponent,
    EspecialidadesDetailComponent,
    EspecialidadesUpdateComponent,
    EspecialidadesDeleteDialogComponent,
  ],
  entryComponents: [EspecialidadesDeleteDialogComponent],
})
export class EspecialidadesModule {}
