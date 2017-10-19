import { Injectable } from '@angular/core';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes } from '@angular/router';

import { UserRouteAccessService } from '../../shared';
import { JhiPaginationUtil } from 'ng-jhipster';

import { MedicosComponent } from './medicos.component';
import { MedicosDetailComponent } from './medicos-detail.component';
import { MedicosPopupComponent } from './medicos-dialog.component';
import { MedicosDeletePopupComponent } from './medicos-delete-dialog.component';

export const medicosRoute: Routes = [
    {
        path: 'medicos',
        component: MedicosComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'safhApp.medicos.home.title'
        },
        canActivate: [UserRouteAccessService]
    }, {
        path: 'medicos/:id',
        component: MedicosDetailComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'safhApp.medicos.home.title'
        },
        canActivate: [UserRouteAccessService]
    }
];

export const medicosPopupRoute: Routes = [
    {
        path: 'medicos-new',
        component: MedicosPopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'safhApp.medicos.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'medicos/:id/edit',
        component: MedicosPopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'safhApp.medicos.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'medicos/:id/delete',
        component: MedicosDeletePopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'safhApp.medicos.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    }
];
