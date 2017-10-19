import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Response } from '@angular/http';

import { Observable } from 'rxjs/Rx';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager, JhiAlertService } from 'ng-jhipster';

import { Pacientes } from './pacientes.model';
import { PacientesPopupService } from './pacientes-popup.service';
import { PacientesService } from './pacientes.service';
import { Clinicas, ClinicasService } from '../clinicas';
import { Enfermarias, EnfermariasService } from '../enfermarias';
import { Leitos, LeitosService } from '../leitos';
import { ResponseWrapper } from '../../shared';

@Component({
    selector: 'jhi-pacientes-dialog',
    templateUrl: './pacientes-dialog.component.html'
})
export class PacientesDialogComponent implements OnInit {

    pacientes: Pacientes;
    isSaving: boolean;

    clinicas: Clinicas[];

    enfermarias: Enfermarias[];

    leitos: Leitos[];

    constructor(
        public activeModal: NgbActiveModal,
        private jhiAlertService: JhiAlertService,
        private pacientesService: PacientesService,
        private clinicasService: ClinicasService,
        private enfermariasService: EnfermariasService,
        private leitosService: LeitosService,
        private eventManager: JhiEventManager
    ) {
    }

    ngOnInit() {
        this.isSaving = false;
        this.clinicasService.query()
            .subscribe((res: ResponseWrapper) => { this.clinicas = res.json; }, (res: ResponseWrapper) => this.onError(res.json));
        this.enfermariasService.query()
            .subscribe((res: ResponseWrapper) => { this.enfermarias = res.json; }, (res: ResponseWrapper) => this.onError(res.json));
        this.leitosService.query()
            .subscribe((res: ResponseWrapper) => { this.leitos = res.json; }, (res: ResponseWrapper) => this.onError(res.json));
    }

    clear() {
        this.activeModal.dismiss('cancel');
    }

    save() {
        this.isSaving = true;
        if (this.pacientes.id !== undefined) {
            this.subscribeToSaveResponse(
                this.pacientesService.update(this.pacientes));
        } else {
            this.subscribeToSaveResponse(
                this.pacientesService.create(this.pacientes));
        }
    }

    private subscribeToSaveResponse(result: Observable<Pacientes>) {
        result.subscribe((res: Pacientes) =>
            this.onSaveSuccess(res), (res: Response) => this.onSaveError());
    }

    private onSaveSuccess(result: Pacientes) {
        this.eventManager.broadcast({ name: 'pacientesListModification', content: 'OK'});
        this.isSaving = false;
        this.activeModal.dismiss(result);
    }

    private onSaveError() {
        this.isSaving = false;
    }

    private onError(error: any) {
        this.jhiAlertService.error(error.message, null, null);
    }

    trackClinicasById(index: number, item: Clinicas) {
        return item.id;
    }

    trackEnfermariasById(index: number, item: Enfermarias) {
        return item.id;
    }

    trackLeitosById(index: number, item: Leitos) {
        return item.id;
    }
}

@Component({
    selector: 'jhi-pacientes-popup',
    template: ''
})
export class PacientesPopupComponent implements OnInit, OnDestroy {

    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private pacientesPopupService: PacientesPopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            if ( params['id'] ) {
                this.pacientesPopupService
                    .open(PacientesDialogComponent as Component, params['id']);
            } else {
                this.pacientesPopupService
                    .open(PacientesDialogComponent as Component);
            }
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
