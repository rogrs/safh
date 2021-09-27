import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { PosologiasComponent } from '../list/posologias.component';
import { PosologiasDetailComponent } from '../detail/posologias-detail.component';
import { PosologiasUpdateComponent } from '../update/posologias-update.component';
import { PosologiasRoutingResolveService } from './posologias-routing-resolve.service';

const posologiasRoute: Routes = [
  {
    path: '',
    component: PosologiasComponent,
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: PosologiasDetailComponent,
    resolve: {
      posologias: PosologiasRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: PosologiasUpdateComponent,
    resolve: {
      posologias: PosologiasRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: PosologiasUpdateComponent,
    resolve: {
      posologias: PosologiasRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(posologiasRoute)],
  exports: [RouterModule],
})
export class PosologiasRoutingModule {}
