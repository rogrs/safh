import { Injectable } from '@angular/core';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes } from '@angular/router';

import { UserRouteAccessService } from '../../shared';
import { JhiPaginationUtil } from 'ng-jhipster';

import { PrescricoesComponent } from './prescricoes.component';
import { PrescricoesDetailComponent } from './prescricoes-detail.component';
import { PrescricoesPopupComponent } from './prescricoes-dialog.component';
import { PrescricoesDeletePopupComponent } from './prescricoes-delete-dialog.component';

export const prescricoesRoute: Routes = [
    {
        path: 'prescricoes',
        component: PrescricoesComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'safhApp.prescricoes.home.title'
        },
        canActivate: [UserRouteAccessService]
    }, {
        path: 'prescricoes/:id',
        component: PrescricoesDetailComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'safhApp.prescricoes.home.title'
        },
        canActivate: [UserRouteAccessService]
    }
];

export const prescricoesPopupRoute: Routes = [
    {
        path: 'prescricoes-new',
        component: PrescricoesPopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'safhApp.prescricoes.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'prescricoes/:id/edit',
        component: PrescricoesPopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'safhApp.prescricoes.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'prescricoes/:id/delete',
        component: PrescricoesDeletePopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'safhApp.prescricoes.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    }
];
