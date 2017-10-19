import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Response } from '@angular/http';

import { Observable } from 'rxjs/Rx';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager, JhiAlertService } from 'ng-jhipster';

import { Medicos } from './medicos.model';
import { MedicosPopupService } from './medicos-popup.service';
import { MedicosService } from './medicos.service';
import { Especialidades, EspecialidadesService } from '../especialidades';
import { ResponseWrapper } from '../../shared';

@Component({
    selector: 'jhi-medicos-dialog',
    templateUrl: './medicos-dialog.component.html'
})
export class MedicosDialogComponent implements OnInit {

    medicos: Medicos;
    isSaving: boolean;

    especialidades: Especialidades[];

    constructor(
        public activeModal: NgbActiveModal,
        private jhiAlertService: JhiAlertService,
        private medicosService: MedicosService,
        private especialidadesService: EspecialidadesService,
        private eventManager: JhiEventManager
    ) {
    }

    ngOnInit() {
        this.isSaving = false;
        this.especialidadesService.query()
            .subscribe((res: ResponseWrapper) => { this.especialidades = res.json; }, (res: ResponseWrapper) => this.onError(res.json));
    }

    clear() {
        this.activeModal.dismiss('cancel');
    }

    save() {
        this.isSaving = true;
        if (this.medicos.id !== undefined) {
            this.subscribeToSaveResponse(
                this.medicosService.update(this.medicos));
        } else {
            this.subscribeToSaveResponse(
                this.medicosService.create(this.medicos));
        }
    }

    private subscribeToSaveResponse(result: Observable<Medicos>) {
        result.subscribe((res: Medicos) =>
            this.onSaveSuccess(res), (res: Response) => this.onSaveError());
    }

    private onSaveSuccess(result: Medicos) {
        this.eventManager.broadcast({ name: 'medicosListModification', content: 'OK'});
        this.isSaving = false;
        this.activeModal.dismiss(result);
    }

    private onSaveError() {
        this.isSaving = false;
    }

    private onError(error: any) {
        this.jhiAlertService.error(error.message, null, null);
    }

    trackEspecialidadesById(index: number, item: Especialidades) {
        return item.id;
    }
}

@Component({
    selector: 'jhi-medicos-popup',
    template: ''
})
export class MedicosPopupComponent implements OnInit, OnDestroy {

    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private medicosPopupService: MedicosPopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            if ( params['id'] ) {
                this.medicosPopupService
                    .open(MedicosDialogComponent as Component, params['id']);
            } else {
                this.medicosPopupService
                    .open(MedicosDialogComponent as Component);
            }
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
