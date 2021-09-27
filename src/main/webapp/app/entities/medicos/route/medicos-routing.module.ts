import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { MedicosComponent } from '../list/medicos.component';
import { MedicosDetailComponent } from '../detail/medicos-detail.component';
import { MedicosUpdateComponent } from '../update/medicos-update.component';
import { MedicosRoutingResolveService } from './medicos-routing-resolve.service';

const medicosRoute: Routes = [
  {
    path: '',
    component: MedicosComponent,
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: MedicosDetailComponent,
    resolve: {
      medicos: MedicosRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: MedicosUpdateComponent,
    resolve: {
      medicos: MedicosRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: MedicosUpdateComponent,
    resolve: {
      medicos: MedicosRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(medicosRoute)],
  exports: [RouterModule],
})
export class MedicosRoutingModule {}
