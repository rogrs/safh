import { NgModule } from '@angular/core';

import { SharedModule } from 'app/shared/shared.module';
import { FabricantesComponent } from './list/fabricantes.component';
import { FabricantesDetailComponent } from './detail/fabricantes-detail.component';
import { FabricantesUpdateComponent } from './update/fabricantes-update.component';
import { FabricantesDeleteDialogComponent } from './delete/fabricantes-delete-dialog.component';
import { FabricantesRoutingModule } from './route/fabricantes-routing.module';

@NgModule({
  imports: [SharedModule, FabricantesRoutingModule],
  declarations: [FabricantesComponent, FabricantesDetailComponent, FabricantesUpdateComponent, FabricantesDeleteDialogComponent],
  entryComponents: [FabricantesDeleteDialogComponent],
})
export class FabricantesModule {}
