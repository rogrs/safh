import { Injectable } from '@angular/core';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes } from '@angular/router';

import { UserRouteAccessService } from '../../shared';
import { JhiPaginationUtil } from 'ng-jhipster';

import { InternacoesDetalhesComponent } from './internacoes-detalhes.component';
import { InternacoesDetalhesDetailComponent } from './internacoes-detalhes-detail.component';
import { InternacoesDetalhesPopupComponent } from './internacoes-detalhes-dialog.component';
import { InternacoesDetalhesDeletePopupComponent } from './internacoes-detalhes-delete-dialog.component';

export const internacoesDetalhesRoute: Routes = [
    {
        path: 'internacoes-detalhes',
        component: InternacoesDetalhesComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'safhApp.internacoesDetalhes.home.title'
        },
        canActivate: [UserRouteAccessService]
    }, {
        path: 'internacoes-detalhes/:id',
        component: InternacoesDetalhesDetailComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'safhApp.internacoesDetalhes.home.title'
        },
        canActivate: [UserRouteAccessService]
    }
];

export const internacoesDetalhesPopupRoute: Routes = [
    {
        path: 'internacoes-detalhes-new',
        component: InternacoesDetalhesPopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'safhApp.internacoesDetalhes.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'internacoes-detalhes/:id/edit',
        component: InternacoesDetalhesPopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'safhApp.internacoesDetalhes.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'internacoes-detalhes/:id/delete',
        component: InternacoesDetalhesDeletePopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'safhApp.internacoesDetalhes.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    }
];
