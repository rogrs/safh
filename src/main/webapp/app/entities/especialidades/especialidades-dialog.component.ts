import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Response } from '@angular/http';

import { Observable } from 'rxjs/Rx';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager, JhiAlertService } from 'ng-jhipster';

import { Especialidades } from './especialidades.model';
import { EspecialidadesPopupService } from './especialidades-popup.service';
import { EspecialidadesService } from './especialidades.service';

@Component({
    selector: 'jhi-especialidades-dialog',
    templateUrl: './especialidades-dialog.component.html'
})
export class EspecialidadesDialogComponent implements OnInit {

    especialidades: Especialidades;
    isSaving: boolean;

    constructor(
        public activeModal: NgbActiveModal,
        private jhiAlertService: JhiAlertService,
        private especialidadesService: EspecialidadesService,
        private eventManager: JhiEventManager
    ) {
    }

    ngOnInit() {
        this.isSaving = false;
    }

    clear() {
        this.activeModal.dismiss('cancel');
    }

    save() {
        this.isSaving = true;
        if (this.especialidades.id !== undefined) {
            this.subscribeToSaveResponse(
                this.especialidadesService.update(this.especialidades));
        } else {
            this.subscribeToSaveResponse(
                this.especialidadesService.create(this.especialidades));
        }
    }

    private subscribeToSaveResponse(result: Observable<Especialidades>) {
        result.subscribe((res: Especialidades) =>
            this.onSaveSuccess(res), (res: Response) => this.onSaveError());
    }

    private onSaveSuccess(result: Especialidades) {
        this.eventManager.broadcast({ name: 'especialidadesListModification', content: 'OK'});
        this.isSaving = false;
        this.activeModal.dismiss(result);
    }

    private onSaveError() {
        this.isSaving = false;
    }

    private onError(error: any) {
        this.jhiAlertService.error(error.message, null, null);
    }
}

@Component({
    selector: 'jhi-especialidades-popup',
    template: ''
})
export class EspecialidadesPopupComponent implements OnInit, OnDestroy {

    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private especialidadesPopupService: EspecialidadesPopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            if ( params['id'] ) {
                this.especialidadesPopupService
                    .open(EspecialidadesDialogComponent as Component, params['id']);
            } else {
                this.especialidadesPopupService
                    .open(EspecialidadesDialogComponent as Component);
            }
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
