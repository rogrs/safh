import { Injectable } from '@angular/core';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes } from '@angular/router';

import { UserRouteAccessService } from '../../shared';
import { JhiPaginationUtil } from 'ng-jhipster';

import { EnfermariasComponent } from './enfermarias.component';
import { EnfermariasDetailComponent } from './enfermarias-detail.component';
import { EnfermariasPopupComponent } from './enfermarias-dialog.component';
import { EnfermariasDeletePopupComponent } from './enfermarias-delete-dialog.component';

export const enfermariasRoute: Routes = [
    {
        path: 'enfermarias',
        component: EnfermariasComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'safhApp.enfermarias.home.title'
        },
        canActivate: [UserRouteAccessService]
    }, {
        path: 'enfermarias/:id',
        component: EnfermariasDetailComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'safhApp.enfermarias.home.title'
        },
        canActivate: [UserRouteAccessService]
    }
];

export const enfermariasPopupRoute: Routes = [
    {
        path: 'enfermarias-new',
        component: EnfermariasPopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'safhApp.enfermarias.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'enfermarias/:id/edit',
        component: EnfermariasPopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'safhApp.enfermarias.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'enfermarias/:id/delete',
        component: EnfermariasDeletePopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'safhApp.enfermarias.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    }
];
