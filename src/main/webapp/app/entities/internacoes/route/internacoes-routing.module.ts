import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { InternacoesComponent } from '../list/internacoes.component';
import { InternacoesDetailComponent } from '../detail/internacoes-detail.component';
import { InternacoesUpdateComponent } from '../update/internacoes-update.component';
import { InternacoesRoutingResolveService } from './internacoes-routing-resolve.service';

const internacoesRoute: Routes = [
  {
    path: '',
    component: InternacoesComponent,
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: InternacoesDetailComponent,
    resolve: {
      internacoes: InternacoesRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: InternacoesUpdateComponent,
    resolve: {
      internacoes: InternacoesRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: InternacoesUpdateComponent,
    resolve: {
      internacoes: InternacoesRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(internacoesRoute)],
  exports: [RouterModule],
})
export class InternacoesRoutingModule {}
