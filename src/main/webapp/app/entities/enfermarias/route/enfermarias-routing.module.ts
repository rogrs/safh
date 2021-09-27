import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { EnfermariasComponent } from '../list/enfermarias.component';
import { EnfermariasDetailComponent } from '../detail/enfermarias-detail.component';
import { EnfermariasUpdateComponent } from '../update/enfermarias-update.component';
import { EnfermariasRoutingResolveService } from './enfermarias-routing-resolve.service';

const enfermariasRoute: Routes = [
  {
    path: '',
    component: EnfermariasComponent,
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: EnfermariasDetailComponent,
    resolve: {
      enfermarias: EnfermariasRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: EnfermariasUpdateComponent,
    resolve: {
      enfermarias: EnfermariasRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: EnfermariasUpdateComponent,
    resolve: {
      enfermarias: EnfermariasRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(enfermariasRoute)],
  exports: [RouterModule],
})
export class EnfermariasRoutingModule {}
