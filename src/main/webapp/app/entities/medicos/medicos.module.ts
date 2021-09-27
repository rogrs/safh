import { NgModule } from '@angular/core';

import { SharedModule } from 'app/shared/shared.module';
import { MedicosComponent } from './list/medicos.component';
import { MedicosDetailComponent } from './detail/medicos-detail.component';
import { MedicosUpdateComponent } from './update/medicos-update.component';
import { MedicosDeleteDialogComponent } from './delete/medicos-delete-dialog.component';
import { MedicosRoutingModule } from './route/medicos-routing.module';

@NgModule({
  imports: [SharedModule, MedicosRoutingModule],
  declarations: [MedicosComponent, MedicosDetailComponent, MedicosUpdateComponent, MedicosDeleteDialogComponent],
  entryComponents: [MedicosDeleteDialogComponent],
})
export class MedicosModule {}
