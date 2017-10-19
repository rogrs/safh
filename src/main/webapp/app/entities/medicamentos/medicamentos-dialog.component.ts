import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Response } from '@angular/http';

import { Observable } from 'rxjs/Rx';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager, JhiAlertService } from 'ng-jhipster';

import { Medicamentos } from './medicamentos.model';
import { MedicamentosPopupService } from './medicamentos-popup.service';
import { MedicamentosService } from './medicamentos.service';
import { Posologias, PosologiasService } from '../posologias';
import { Fabricantes, FabricantesService } from '../fabricantes';
import { ResponseWrapper } from '../../shared';

@Component({
    selector: 'jhi-medicamentos-dialog',
    templateUrl: './medicamentos-dialog.component.html'
})
export class MedicamentosDialogComponent implements OnInit {

    medicamentos: Medicamentos;
    isSaving: boolean;

    posologias: Posologias[];

    fabricantes: Fabricantes[];

    constructor(
        public activeModal: NgbActiveModal,
        private jhiAlertService: JhiAlertService,
        private medicamentosService: MedicamentosService,
        private posologiasService: PosologiasService,
        private fabricantesService: FabricantesService,
        private eventManager: JhiEventManager
    ) {
    }

    ngOnInit() {
        this.isSaving = false;
        this.posologiasService.query()
            .subscribe((res: ResponseWrapper) => { this.posologias = res.json; }, (res: ResponseWrapper) => this.onError(res.json));
        this.fabricantesService.query()
            .subscribe((res: ResponseWrapper) => { this.fabricantes = res.json; }, (res: ResponseWrapper) => this.onError(res.json));
    }

    clear() {
        this.activeModal.dismiss('cancel');
    }

    save() {
        this.isSaving = true;
        if (this.medicamentos.id !== undefined) {
            this.subscribeToSaveResponse(
                this.medicamentosService.update(this.medicamentos));
        } else {
            this.subscribeToSaveResponse(
                this.medicamentosService.create(this.medicamentos));
        }
    }

    private subscribeToSaveResponse(result: Observable<Medicamentos>) {
        result.subscribe((res: Medicamentos) =>
            this.onSaveSuccess(res), (res: Response) => this.onSaveError());
    }

    private onSaveSuccess(result: Medicamentos) {
        this.eventManager.broadcast({ name: 'medicamentosListModification', content: 'OK'});
        this.isSaving = false;
        this.activeModal.dismiss(result);
    }

    private onSaveError() {
        this.isSaving = false;
    }

    private onError(error: any) {
        this.jhiAlertService.error(error.message, null, null);
    }

    trackPosologiasById(index: number, item: Posologias) {
        return item.id;
    }

    trackFabricantesById(index: number, item: Fabricantes) {
        return item.id;
    }
}

@Component({
    selector: 'jhi-medicamentos-popup',
    template: ''
})
export class MedicamentosPopupComponent implements OnInit, OnDestroy {

    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private medicamentosPopupService: MedicamentosPopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            if ( params['id'] ) {
                this.medicamentosPopupService
                    .open(MedicamentosDialogComponent as Component, params['id']);
            } else {
                this.medicamentosPopupService
                    .open(MedicamentosDialogComponent as Component);
            }
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
