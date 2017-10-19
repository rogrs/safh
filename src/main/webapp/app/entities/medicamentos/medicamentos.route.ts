import { Injectable } from '@angular/core';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes } from '@angular/router';

import { UserRouteAccessService } from '../../shared';
import { JhiPaginationUtil } from 'ng-jhipster';

import { MedicamentosComponent } from './medicamentos.component';
import { MedicamentosDetailComponent } from './medicamentos-detail.component';
import { MedicamentosPopupComponent } from './medicamentos-dialog.component';
import { MedicamentosDeletePopupComponent } from './medicamentos-delete-dialog.component';

export const medicamentosRoute: Routes = [
    {
        path: 'medicamentos',
        component: MedicamentosComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'safhApp.medicamentos.home.title'
        },
        canActivate: [UserRouteAccessService]
    }, {
        path: 'medicamentos/:id',
        component: MedicamentosDetailComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'safhApp.medicamentos.home.title'
        },
        canActivate: [UserRouteAccessService]
    }
];

export const medicamentosPopupRoute: Routes = [
    {
        path: 'medicamentos-new',
        component: MedicamentosPopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'safhApp.medicamentos.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'medicamentos/:id/edit',
        component: MedicamentosPopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'safhApp.medicamentos.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'medicamentos/:id/delete',
        component: MedicamentosDeletePopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'safhApp.medicamentos.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    }
];
