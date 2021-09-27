import { NgModule } from '@angular/core';

import { SharedModule } from 'app/shared/shared.module';
import { PosologiasComponent } from './list/posologias.component';
import { PosologiasDetailComponent } from './detail/posologias-detail.component';
import { PosologiasUpdateComponent } from './update/posologias-update.component';
import { PosologiasDeleteDialogComponent } from './delete/posologias-delete-dialog.component';
import { PosologiasRoutingModule } from './route/posologias-routing.module';

@NgModule({
  imports: [SharedModule, PosologiasRoutingModule],
  declarations: [PosologiasComponent, PosologiasDetailComponent, PosologiasUpdateComponent, PosologiasDeleteDialogComponent],
  entryComponents: [PosologiasDeleteDialogComponent],
})
export class PosologiasModule {}
