import { Injectable } from '@angular/core';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes } from '@angular/router';

import { UserRouteAccessService } from '../../shared';
import { JhiPaginationUtil } from 'ng-jhipster';

import { PosologiasComponent } from './posologias.component';
import { PosologiasDetailComponent } from './posologias-detail.component';
import { PosologiasPopupComponent } from './posologias-dialog.component';
import { PosologiasDeletePopupComponent } from './posologias-delete-dialog.component';

export const posologiasRoute: Routes = [
    {
        path: 'posologias',
        component: PosologiasComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'safhApp.posologias.home.title'
        },
        canActivate: [UserRouteAccessService]
    }, {
        path: 'posologias/:id',
        component: PosologiasDetailComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'safhApp.posologias.home.title'
        },
        canActivate: [UserRouteAccessService]
    }
];

export const posologiasPopupRoute: Routes = [
    {
        path: 'posologias-new',
        component: PosologiasPopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'safhApp.posologias.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'posologias/:id/edit',
        component: PosologiasPopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'safhApp.posologias.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'posologias/:id/delete',
        component: PosologiasDeletePopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'safhApp.posologias.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    }
];
