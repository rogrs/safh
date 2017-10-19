import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Response } from '@angular/http';

import { Observable } from 'rxjs/Rx';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager, JhiAlertService } from 'ng-jhipster';

import { Internacoes } from './internacoes.model';
import { InternacoesPopupService } from './internacoes-popup.service';
import { InternacoesService } from './internacoes.service';
import { Pacientes, PacientesService } from '../pacientes';
import { Clinicas, ClinicasService } from '../clinicas';
import { Medicos, MedicosService } from '../medicos';
import { ResponseWrapper } from '../../shared';

@Component({
    selector: 'jhi-internacoes-dialog',
    templateUrl: './internacoes-dialog.component.html'
})
export class InternacoesDialogComponent implements OnInit {

    internacoes: Internacoes;
    isSaving: boolean;

    pacientes: Pacientes[];

    clinicas: Clinicas[];

    medicos: Medicos[];
    dataInternacaoDp: any;

    constructor(
        public activeModal: NgbActiveModal,
        private jhiAlertService: JhiAlertService,
        private internacoesService: InternacoesService,
        private pacientesService: PacientesService,
        private clinicasService: ClinicasService,
        private medicosService: MedicosService,
        private eventManager: JhiEventManager
    ) {
    }

    ngOnInit() {
        this.isSaving = false;
        this.pacientesService.query()
            .subscribe((res: ResponseWrapper) => { this.pacientes = res.json; }, (res: ResponseWrapper) => this.onError(res.json));
        this.clinicasService.query()
            .subscribe((res: ResponseWrapper) => { this.clinicas = res.json; }, (res: ResponseWrapper) => this.onError(res.json));
        this.medicosService.query()
            .subscribe((res: ResponseWrapper) => { this.medicos = res.json; }, (res: ResponseWrapper) => this.onError(res.json));
    }

    clear() {
        this.activeModal.dismiss('cancel');
    }

    save() {
        this.isSaving = true;
        if (this.internacoes.id !== undefined) {
            this.subscribeToSaveResponse(
                this.internacoesService.update(this.internacoes));
        } else {
            this.subscribeToSaveResponse(
                this.internacoesService.create(this.internacoes));
        }
    }

    private subscribeToSaveResponse(result: Observable<Internacoes>) {
        result.subscribe((res: Internacoes) =>
            this.onSaveSuccess(res), (res: Response) => this.onSaveError());
    }

    private onSaveSuccess(result: Internacoes) {
        this.eventManager.broadcast({ name: 'internacoesListModification', content: 'OK'});
        this.isSaving = false;
        this.activeModal.dismiss(result);
    }

    private onSaveError() {
        this.isSaving = false;
    }

    private onError(error: any) {
        this.jhiAlertService.error(error.message, null, null);
    }

    trackPacientesById(index: number, item: Pacientes) {
        return item.id;
    }

    trackClinicasById(index: number, item: Clinicas) {
        return item.id;
    }

    trackMedicosById(index: number, item: Medicos) {
        return item.id;
    }
}

@Component({
    selector: 'jhi-internacoes-popup',
    template: ''
})
export class InternacoesPopupComponent implements OnInit, OnDestroy {

    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private internacoesPopupService: InternacoesPopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            if ( params['id'] ) {
                this.internacoesPopupService
                    .open(InternacoesDialogComponent as Component, params['id']);
            } else {
                this.internacoesPopupService
                    .open(InternacoesDialogComponent as Component);
            }
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
