import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Response } from '@angular/http';

import { Observable } from 'rxjs/Rx';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager, JhiAlertService } from 'ng-jhipster';

import { InternacoesDetalhes } from './internacoes-detalhes.model';
import { InternacoesDetalhesPopupService } from './internacoes-detalhes-popup.service';
import { InternacoesDetalhesService } from './internacoes-detalhes.service';
import { Internacoes, InternacoesService } from '../internacoes';
import { Dietas, DietasService } from '../dietas';
import { Prescricoes, PrescricoesService } from '../prescricoes';
import { Posologias, PosologiasService } from '../posologias';
import { ResponseWrapper } from '../../shared';

@Component({
    selector: 'jhi-internacoes-detalhes-dialog',
    templateUrl: './internacoes-detalhes-dialog.component.html'
})
export class InternacoesDetalhesDialogComponent implements OnInit {

    internacoesDetalhes: InternacoesDetalhes;
    isSaving: boolean;

    internacoes: Internacoes[];

    dietas: Dietas[];

    prescricoes: Prescricoes[];

    posologias: Posologias[];
    dataDetalheDp: any;
    horarioDp: any;

    constructor(
        public activeModal: NgbActiveModal,
        private jhiAlertService: JhiAlertService,
        private internacoesDetalhesService: InternacoesDetalhesService,
        private internacoesService: InternacoesService,
        private dietasService: DietasService,
        private prescricoesService: PrescricoesService,
        private posologiasService: PosologiasService,
        private eventManager: JhiEventManager
    ) {
    }

    ngOnInit() {
        this.isSaving = false;
        this.internacoesService.query()
            .subscribe((res: ResponseWrapper) => { this.internacoes = res.json; }, (res: ResponseWrapper) => this.onError(res.json));
        this.dietasService.query()
            .subscribe((res: ResponseWrapper) => { this.dietas = res.json; }, (res: ResponseWrapper) => this.onError(res.json));
        this.prescricoesService.query()
            .subscribe((res: ResponseWrapper) => { this.prescricoes = res.json; }, (res: ResponseWrapper) => this.onError(res.json));
        this.posologiasService.query()
            .subscribe((res: ResponseWrapper) => { this.posologias = res.json; }, (res: ResponseWrapper) => this.onError(res.json));
    }

    clear() {
        this.activeModal.dismiss('cancel');
    }

    save() {
        this.isSaving = true;
        if (this.internacoesDetalhes.id !== undefined) {
            this.subscribeToSaveResponse(
                this.internacoesDetalhesService.update(this.internacoesDetalhes));
        } else {
            this.subscribeToSaveResponse(
                this.internacoesDetalhesService.create(this.internacoesDetalhes));
        }
    }

    private subscribeToSaveResponse(result: Observable<InternacoesDetalhes>) {
        result.subscribe((res: InternacoesDetalhes) =>
            this.onSaveSuccess(res), (res: Response) => this.onSaveError());
    }

    private onSaveSuccess(result: InternacoesDetalhes) {
        this.eventManager.broadcast({ name: 'internacoesDetalhesListModification', content: 'OK'});
        this.isSaving = false;
        this.activeModal.dismiss(result);
    }

    private onSaveError() {
        this.isSaving = false;
    }

    private onError(error: any) {
        this.jhiAlertService.error(error.message, null, null);
    }

    trackInternacoesById(index: number, item: Internacoes) {
        return item.id;
    }

    trackDietasById(index: number, item: Dietas) {
        return item.id;
    }

    trackPrescricoesById(index: number, item: Prescricoes) {
        return item.id;
    }

    trackPosologiasById(index: number, item: Posologias) {
        return item.id;
    }
}

@Component({
    selector: 'jhi-internacoes-detalhes-popup',
    template: ''
})
export class InternacoesDetalhesPopupComponent implements OnInit, OnDestroy {

    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private internacoesDetalhesPopupService: InternacoesDetalhesPopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            if ( params['id'] ) {
                this.internacoesDetalhesPopupService
                    .open(InternacoesDetalhesDialogComponent as Component, params['id']);
            } else {
                this.internacoesDetalhesPopupService
                    .open(InternacoesDetalhesDialogComponent as Component);
            }
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
