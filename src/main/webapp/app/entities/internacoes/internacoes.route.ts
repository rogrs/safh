import { Injectable } from '@angular/core';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes } from '@angular/router';

import { UserRouteAccessService } from '../../shared';
import { JhiPaginationUtil } from 'ng-jhipster';

import { InternacoesComponent } from './internacoes.component';
import { InternacoesDetailComponent } from './internacoes-detail.component';
import { InternacoesPopupComponent } from './internacoes-dialog.component';
import { InternacoesDeletePopupComponent } from './internacoes-delete-dialog.component';

export const internacoesRoute: Routes = [
    {
        path: 'internacoes',
        component: InternacoesComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'safhApp.internacoes.home.title'
        },
        canActivate: [UserRouteAccessService]
    }, {
        path: 'internacoes/:id',
        component: InternacoesDetailComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'safhApp.internacoes.home.title'
        },
        canActivate: [UserRouteAccessService]
    }
];

export const internacoesPopupRoute: Routes = [
    {
        path: 'internacoes-new',
        component: InternacoesPopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'safhApp.internacoes.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'internacoes/:id/edit',
        component: InternacoesPopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'safhApp.internacoes.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'internacoes/:id/delete',
        component: InternacoesDeletePopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'safhApp.internacoes.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    }
];
