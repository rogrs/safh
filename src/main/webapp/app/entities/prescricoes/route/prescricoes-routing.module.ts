import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { PrescricoesComponent } from '../list/prescricoes.component';
import { PrescricoesDetailComponent } from '../detail/prescricoes-detail.component';
import { PrescricoesUpdateComponent } from '../update/prescricoes-update.component';
import { PrescricoesRoutingResolveService } from './prescricoes-routing-resolve.service';

const prescricoesRoute: Routes = [
  {
    path: '',
    component: PrescricoesComponent,
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: PrescricoesDetailComponent,
    resolve: {
      prescricoes: PrescricoesRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: PrescricoesUpdateComponent,
    resolve: {
      prescricoes: PrescricoesRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: PrescricoesUpdateComponent,
    resolve: {
      prescricoes: PrescricoesRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(prescricoesRoute)],
  exports: [RouterModule],
})
export class PrescricoesRoutingModule {}
