import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { MedicamentosComponent } from '../list/medicamentos.component';
import { MedicamentosDetailComponent } from '../detail/medicamentos-detail.component';
import { MedicamentosUpdateComponent } from '../update/medicamentos-update.component';
import { MedicamentosRoutingResolveService } from './medicamentos-routing-resolve.service';

const medicamentosRoute: Routes = [
  {
    path: '',
    component: MedicamentosComponent,
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: MedicamentosDetailComponent,
    resolve: {
      medicamentos: MedicamentosRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: MedicamentosUpdateComponent,
    resolve: {
      medicamentos: MedicamentosRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: MedicamentosUpdateComponent,
    resolve: {
      medicamentos: MedicamentosRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(medicamentosRoute)],
  exports: [RouterModule],
})
export class MedicamentosRoutingModule {}
