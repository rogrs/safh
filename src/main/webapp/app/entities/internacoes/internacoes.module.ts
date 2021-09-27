import { NgModule } from '@angular/core';

import { SharedModule } from 'app/shared/shared.module';
import { InternacoesComponent } from './list/internacoes.component';
import { InternacoesDetailComponent } from './detail/internacoes-detail.component';
import { InternacoesUpdateComponent } from './update/internacoes-update.component';
import { InternacoesDeleteDialogComponent } from './delete/internacoes-delete-dialog.component';
import { InternacoesRoutingModule } from './route/internacoes-routing.module';

@NgModule({
  imports: [SharedModule, InternacoesRoutingModule],
  declarations: [InternacoesComponent, InternacoesDetailComponent, InternacoesUpdateComponent, InternacoesDeleteDialogComponent],
  entryComponents: [InternacoesDeleteDialogComponent],
})
export class InternacoesModule {}
