import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { EspecialidadesComponent } from '../list/especialidades.component';
import { EspecialidadesDetailComponent } from '../detail/especialidades-detail.component';
import { EspecialidadesUpdateComponent } from '../update/especialidades-update.component';
import { EspecialidadesRoutingResolveService } from './especialidades-routing-resolve.service';

const especialidadesRoute: Routes = [
  {
    path: '',
    component: EspecialidadesComponent,
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: EspecialidadesDetailComponent,
    resolve: {
      especialidades: EspecialidadesRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: EspecialidadesUpdateComponent,
    resolve: {
      especialidades: EspecialidadesRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: EspecialidadesUpdateComponent,
    resolve: {
      especialidades: EspecialidadesRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(especialidadesRoute)],
  exports: [RouterModule],
})
export class EspecialidadesRoutingModule {}
