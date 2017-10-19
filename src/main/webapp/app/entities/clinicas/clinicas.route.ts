import { Injectable } from '@angular/core';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes } from '@angular/router';

import { UserRouteAccessService } from '../../shared';
import { JhiPaginationUtil } from 'ng-jhipster';

import { ClinicasComponent } from './clinicas.component';
import { ClinicasDetailComponent } from './clinicas-detail.component';
import { ClinicasPopupComponent } from './clinicas-dialog.component';
import { ClinicasDeletePopupComponent } from './clinicas-delete-dialog.component';

export const clinicasRoute: Routes = [
    {
        path: 'clinicas',
        component: ClinicasComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'safhApp.clinicas.home.title'
        },
        canActivate: [UserRouteAccessService]
    }, {
        path: 'clinicas/:id',
        component: ClinicasDetailComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'safhApp.clinicas.home.title'
        },
        canActivate: [UserRouteAccessService]
    }
];

export const clinicasPopupRoute: Routes = [
    {
        path: 'clinicas-new',
        component: ClinicasPopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'safhApp.clinicas.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'clinicas/:id/edit',
        component: ClinicasPopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'safhApp.clinicas.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'clinicas/:id/delete',
        component: ClinicasDeletePopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'safhApp.clinicas.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    }
];
