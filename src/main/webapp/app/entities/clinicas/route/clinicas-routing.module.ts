import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ClinicasComponent } from '../list/clinicas.component';
import { ClinicasDetailComponent } from '../detail/clinicas-detail.component';
import { ClinicasUpdateComponent } from '../update/clinicas-update.component';
import { ClinicasRoutingResolveService } from './clinicas-routing-resolve.service';

const clinicasRoute: Routes = [
  {
    path: '',
    component: ClinicasComponent,
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: ClinicasDetailComponent,
    resolve: {
      clinicas: ClinicasRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: ClinicasUpdateComponent,
    resolve: {
      clinicas: ClinicasRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: ClinicasUpdateComponent,
    resolve: {
      clinicas: ClinicasRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(clinicasRoute)],
  exports: [RouterModule],
})
export class ClinicasRoutingModule {}
