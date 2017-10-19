import { Injectable } from '@angular/core';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes } from '@angular/router';

import { UserRouteAccessService } from '../../shared';
import { JhiPaginationUtil } from 'ng-jhipster';

import { EspecialidadesComponent } from './especialidades.component';
import { EspecialidadesDetailComponent } from './especialidades-detail.component';
import { EspecialidadesPopupComponent } from './especialidades-dialog.component';
import { EspecialidadesDeletePopupComponent } from './especialidades-delete-dialog.component';

export const especialidadesRoute: Routes = [
    {
        path: 'especialidades',
        component: EspecialidadesComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'safhApp.especialidades.home.title'
        },
        canActivate: [UserRouteAccessService]
    }, {
        path: 'especialidades/:id',
        component: EspecialidadesDetailComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'safhApp.especialidades.home.title'
        },
        canActivate: [UserRouteAccessService]
    }
];

export const especialidadesPopupRoute: Routes = [
    {
        path: 'especialidades-new',
        component: EspecialidadesPopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'safhApp.especialidades.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'especialidades/:id/edit',
        component: EspecialidadesPopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'safhApp.especialidades.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'especialidades/:id/delete',
        component: EspecialidadesDeletePopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'safhApp.especialidades.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    }
];
