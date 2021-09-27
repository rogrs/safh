import { NgModule } from '@angular/core';

import { SharedModule } from 'app/shared/shared.module';
import { InternacoesDetalhesComponent } from './list/internacoes-detalhes.component';
import { InternacoesDetalhesDetailComponent } from './detail/internacoes-detalhes-detail.component';
import { InternacoesDetalhesUpdateComponent } from './update/internacoes-detalhes-update.component';
import { InternacoesDetalhesDeleteDialogComponent } from './delete/internacoes-detalhes-delete-dialog.component';
import { InternacoesDetalhesRoutingModule } from './route/internacoes-detalhes-routing.module';

@NgModule({
  imports: [SharedModule, InternacoesDetalhesRoutingModule],
  declarations: [
    InternacoesDetalhesComponent,
    InternacoesDetalhesDetailComponent,
    InternacoesDetalhesUpdateComponent,
    InternacoesDetalhesDeleteDialogComponent,
  ],
  entryComponents: [InternacoesDetalhesDeleteDialogComponent],
})
export class InternacoesDetalhesModule {}
