import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { InternacoesDetalhesComponent } from '../list/internacoes-detalhes.component';
import { InternacoesDetalhesDetailComponent } from '../detail/internacoes-detalhes-detail.component';
import { InternacoesDetalhesUpdateComponent } from '../update/internacoes-detalhes-update.component';
import { InternacoesDetalhesRoutingResolveService } from './internacoes-detalhes-routing-resolve.service';

const internacoesDetalhesRoute: Routes = [
  {
    path: '',
    component: InternacoesDetalhesComponent,
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: InternacoesDetalhesDetailComponent,
    resolve: {
      internacoesDetalhes: InternacoesDetalhesRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: InternacoesDetalhesUpdateComponent,
    resolve: {
      internacoesDetalhes: InternacoesDetalhesRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: InternacoesDetalhesUpdateComponent,
    resolve: {
      internacoesDetalhes: InternacoesDetalhesRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(internacoesDetalhesRoute)],
  exports: [RouterModule],
})
export class InternacoesDetalhesRoutingModule {}
