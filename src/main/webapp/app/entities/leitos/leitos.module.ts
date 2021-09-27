import { NgModule } from '@angular/core';

import { SharedModule } from 'app/shared/shared.module';
import { LeitosComponent } from './list/leitos.component';
import { LeitosDetailComponent } from './detail/leitos-detail.component';
import { LeitosUpdateComponent } from './update/leitos-update.component';
import { LeitosDeleteDialogComponent } from './delete/leitos-delete-dialog.component';
import { LeitosRoutingModule } from './route/leitos-routing.module';

@NgModule({
  imports: [SharedModule, LeitosRoutingModule],
  declarations: [LeitosComponent, LeitosDetailComponent, LeitosUpdateComponent, LeitosDeleteDialogComponent],
  entryComponents: [LeitosDeleteDialogComponent],
})
export class LeitosModule {}
