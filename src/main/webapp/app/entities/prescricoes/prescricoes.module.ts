import { NgModule } from '@angular/core';

import { SharedModule } from 'app/shared/shared.module';
import { PrescricoesComponent } from './list/prescricoes.component';
import { PrescricoesDetailComponent } from './detail/prescricoes-detail.component';
import { PrescricoesUpdateComponent } from './update/prescricoes-update.component';
import { PrescricoesDeleteDialogComponent } from './delete/prescricoes-delete-dialog.component';
import { PrescricoesRoutingModule } from './route/prescricoes-routing.module';

@NgModule({
  imports: [SharedModule, PrescricoesRoutingModule],
  declarations: [PrescricoesComponent, PrescricoesDetailComponent, PrescricoesUpdateComponent, PrescricoesDeleteDialogComponent],
  entryComponents: [PrescricoesDeleteDialogComponent],
})
export class PrescricoesModule {}
