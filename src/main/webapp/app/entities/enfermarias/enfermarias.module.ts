import { NgModule } from '@angular/core';

import { SharedModule } from 'app/shared/shared.module';
import { EnfermariasComponent } from './list/enfermarias.component';
import { EnfermariasDetailComponent } from './detail/enfermarias-detail.component';
import { EnfermariasUpdateComponent } from './update/enfermarias-update.component';
import { EnfermariasDeleteDialogComponent } from './delete/enfermarias-delete-dialog.component';
import { EnfermariasRoutingModule } from './route/enfermarias-routing.module';

@NgModule({
  imports: [SharedModule, EnfermariasRoutingModule],
  declarations: [EnfermariasComponent, EnfermariasDetailComponent, EnfermariasUpdateComponent, EnfermariasDeleteDialogComponent],
  entryComponents: [EnfermariasDeleteDialogComponent],
})
export class EnfermariasModule {}
