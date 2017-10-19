import { Injectable } from '@angular/core';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes } from '@angular/router';

import { UserRouteAccessService } from '../../shared';
import { JhiPaginationUtil } from 'ng-jhipster';

import { DietasComponent } from './dietas.component';
import { DietasDetailComponent } from './dietas-detail.component';
import { DietasPopupComponent } from './dietas-dialog.component';
import { DietasDeletePopupComponent } from './dietas-delete-dialog.component';

export const dietasRoute: Routes = [
    {
        path: 'dietas',
        component: DietasComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'safhApp.dietas.home.title'
        },
        canActivate: [UserRouteAccessService]
    }, {
        path: 'dietas/:id',
        component: DietasDetailComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'safhApp.dietas.home.title'
        },
        canActivate: [UserRouteAccessService]
    }
];

export const dietasPopupRoute: Routes = [
    {
        path: 'dietas-new',
        component: DietasPopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'safhApp.dietas.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'dietas/:id/edit',
        component: DietasPopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'safhApp.dietas.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'dietas/:id/delete',
        component: DietasDeletePopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'safhApp.dietas.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    }
];
