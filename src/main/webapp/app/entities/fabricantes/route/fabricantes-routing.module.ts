import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { FabricantesComponent } from '../list/fabricantes.component';
import { FabricantesDetailComponent } from '../detail/fabricantes-detail.component';
import { FabricantesUpdateComponent } from '../update/fabricantes-update.component';
import { FabricantesRoutingResolveService } from './fabricantes-routing-resolve.service';

const fabricantesRoute: Routes = [
  {
    path: '',
    component: FabricantesComponent,
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: FabricantesDetailComponent,
    resolve: {
      fabricantes: FabricantesRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: FabricantesUpdateComponent,
    resolve: {
      fabricantes: FabricantesRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: FabricantesUpdateComponent,
    resolve: {
      fabricantes: FabricantesRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(fabricantesRoute)],
  exports: [RouterModule],
})
export class FabricantesRoutingModule {}
