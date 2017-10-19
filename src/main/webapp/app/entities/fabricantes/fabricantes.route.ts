import { Injectable } from '@angular/core';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes } from '@angular/router';

import { UserRouteAccessService } from '../../shared';
import { JhiPaginationUtil } from 'ng-jhipster';

import { FabricantesComponent } from './fabricantes.component';
import { FabricantesDetailComponent } from './fabricantes-detail.component';
import { FabricantesPopupComponent } from './fabricantes-dialog.component';
import { FabricantesDeletePopupComponent } from './fabricantes-delete-dialog.component';

export const fabricantesRoute: Routes = [
    {
        path: 'fabricantes',
        component: FabricantesComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'safhApp.fabricantes.home.title'
        },
        canActivate: [UserRouteAccessService]
    }, {
        path: 'fabricantes/:id',
        component: FabricantesDetailComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'safhApp.fabricantes.home.title'
        },
        canActivate: [UserRouteAccessService]
    }
];

export const fabricantesPopupRoute: Routes = [
    {
        path: 'fabricantes-new',
        component: FabricantesPopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'safhApp.fabricantes.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'fabricantes/:id/edit',
        component: FabricantesPopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'safhApp.fabricantes.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'fabricantes/:id/delete',
        component: FabricantesDeletePopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'safhApp.fabricantes.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    }
];
