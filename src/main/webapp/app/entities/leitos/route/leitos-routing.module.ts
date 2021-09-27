import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { LeitosComponent } from '../list/leitos.component';
import { LeitosDetailComponent } from '../detail/leitos-detail.component';
import { LeitosUpdateComponent } from '../update/leitos-update.component';
import { LeitosRoutingResolveService } from './leitos-routing-resolve.service';

const leitosRoute: Routes = [
  {
    path: '',
    component: LeitosComponent,
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: LeitosDetailComponent,
    resolve: {
      leitos: LeitosRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: LeitosUpdateComponent,
    resolve: {
      leitos: LeitosRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: LeitosUpdateComponent,
    resolve: {
      leitos: LeitosRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(leitosRoute)],
  exports: [RouterModule],
})
export class LeitosRoutingModule {}
