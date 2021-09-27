import { NgModule } from '@angular/core';

import { SharedModule } from 'app/shared/shared.module';
import { DietasComponent } from './list/dietas.component';
import { DietasDetailComponent } from './detail/dietas-detail.component';
import { DietasUpdateComponent } from './update/dietas-update.component';
import { DietasDeleteDialogComponent } from './delete/dietas-delete-dialog.component';
import { DietasRoutingModule } from './route/dietas-routing.module';

@NgModule({
  imports: [SharedModule, DietasRoutingModule],
  declarations: [DietasComponent, DietasDetailComponent, DietasUpdateComponent, DietasDeleteDialogComponent],
  entryComponents: [DietasDeleteDialogComponent],
})
export class DietasModule {}
