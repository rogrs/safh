import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { PacientesComponent } from '../list/pacientes.component';
import { PacientesDetailComponent } from '../detail/pacientes-detail.component';
import { PacientesUpdateComponent } from '../update/pacientes-update.component';
import { PacientesRoutingResolveService } from './pacientes-routing-resolve.service';

const pacientesRoute: Routes = [
  {
    path: '',
    component: PacientesComponent,
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: PacientesDetailComponent,
    resolve: {
      pacientes: PacientesRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: PacientesUpdateComponent,
    resolve: {
      pacientes: PacientesRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: PacientesUpdateComponent,
    resolve: {
      pacientes: PacientesRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(pacientesRoute)],
  exports: [RouterModule],
})
export class PacientesRoutingModule {}
