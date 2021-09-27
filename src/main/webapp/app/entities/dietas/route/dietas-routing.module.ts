import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { DietasComponent } from '../list/dietas.component';
import { DietasDetailComponent } from '../detail/dietas-detail.component';
import { DietasUpdateComponent } from '../update/dietas-update.component';
import { DietasRoutingResolveService } from './dietas-routing-resolve.service';

const dietasRoute: Routes = [
  {
    path: '',
    component: DietasComponent,
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: DietasDetailComponent,
    resolve: {
      dietas: DietasRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: DietasUpdateComponent,
    resolve: {
      dietas: DietasRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: DietasUpdateComponent,
    resolve: {
      dietas: DietasRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(dietasRoute)],
  exports: [RouterModule],
})
export class DietasRoutingModule {}
