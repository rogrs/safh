import { NgModule } from '@angular/core';

import { SharedModule } from 'app/shared/shared.module';
import { MedicamentosComponent } from './list/medicamentos.component';
import { MedicamentosDetailComponent } from './detail/medicamentos-detail.component';
import { MedicamentosUpdateComponent } from './update/medicamentos-update.component';
import { MedicamentosDeleteDialogComponent } from './delete/medicamentos-delete-dialog.component';
import { MedicamentosRoutingModule } from './route/medicamentos-routing.module';

@NgModule({
  imports: [SharedModule, MedicamentosRoutingModule],
  declarations: [MedicamentosComponent, MedicamentosDetailComponent, MedicamentosUpdateComponent, MedicamentosDeleteDialogComponent],
  entryComponents: [MedicamentosDeleteDialogComponent],
})
export class MedicamentosModule {}
