import { NgModule } from '@angular/core';

import { SharedModule } from 'app/shared/shared.module';
import { ClinicasComponent } from './list/clinicas.component';
import { ClinicasDetailComponent } from './detail/clinicas-detail.component';
import { ClinicasUpdateComponent } from './update/clinicas-update.component';
import { ClinicasDeleteDialogComponent } from './delete/clinicas-delete-dialog.component';
import { ClinicasRoutingModule } from './route/clinicas-routing.module';

@NgModule({
  imports: [SharedModule, ClinicasRoutingModule],
  declarations: [ClinicasComponent, ClinicasDetailComponent, ClinicasUpdateComponent, ClinicasDeleteDialogComponent],
  entryComponents: [ClinicasDeleteDialogComponent],
})
export class ClinicasModule {}
