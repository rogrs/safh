import { Injectable } from '@angular/core';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes } from '@angular/router';

import { UserRouteAccessService } from '../../shared';
import { JhiPaginationUtil } from 'ng-jhipster';

import { PacientesComponent } from './pacientes.component';
import { PacientesDetailComponent } from './pacientes-detail.component';
import { PacientesPopupComponent } from './pacientes-dialog.component';
import { PacientesDeletePopupComponent } from './pacientes-delete-dialog.component';

export const pacientesRoute: Routes = [
    {
        path: 'pacientes',
        component: PacientesComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'safhApp.pacientes.home.title'
        },
        canActivate: [UserRouteAccessService]
    }, {
        path: 'pacientes/:id',
        component: PacientesDetailComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'safhApp.pacientes.home.title'
        },
        canActivate: [UserRouteAccessService]
    }
];

export const pacientesPopupRoute: Routes = [
    {
        path: 'pacientes-new',
        component: PacientesPopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'safhApp.pacientes.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'pacientes/:id/edit',
        component: PacientesPopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'safhApp.pacientes.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'pacientes/:id/delete',
        component: PacientesDeletePopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'safhApp.pacientes.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    }
];
