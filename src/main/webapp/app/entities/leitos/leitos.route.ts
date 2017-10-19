import { Injectable } from '@angular/core';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes } from '@angular/router';

import { UserRouteAccessService } from '../../shared';
import { JhiPaginationUtil } from 'ng-jhipster';

import { LeitosComponent } from './leitos.component';
import { LeitosDetailComponent } from './leitos-detail.component';
import { LeitosPopupComponent } from './leitos-dialog.component';
import { LeitosDeletePopupComponent } from './leitos-delete-dialog.component';

export const leitosRoute: Routes = [
    {
        path: 'leitos',
        component: LeitosComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'safhApp.leitos.home.title'
        },
        canActivate: [UserRouteAccessService]
    }, {
        path: 'leitos/:id',
        component: LeitosDetailComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'safhApp.leitos.home.title'
        },
        canActivate: [UserRouteAccessService]
    }
];

export const leitosPopupRoute: Routes = [
    {
        path: 'leitos-new',
        component: LeitosPopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'safhApp.leitos.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'leitos/:id/edit',
        component: LeitosPopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'safhApp.leitos.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'leitos/:id/delete',
        component: LeitosDeletePopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'safhApp.leitos.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    }
];
