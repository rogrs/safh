import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Response } from '@angular/http';

import { Observable } from 'rxjs/Rx';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager, JhiAlertService } from 'ng-jhipster';

import { Clinicas } from './clinicas.model';
import { ClinicasPopupService } from './clinicas-popup.service';
import { ClinicasService } from './clinicas.service';

@Component({
    selector: 'jhi-clinicas-dialog',
    templateUrl: './clinicas-dialog.component.html'
})
export class ClinicasDialogComponent implements OnInit {

    clinicas: Clinicas;
    isSaving: boolean;

    constructor(
        public activeModal: NgbActiveModal,
        private jhiAlertService: JhiAlertService,
        private clinicasService: ClinicasService,
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
        if (this.clinicas.id !== undefined) {
            this.subscribeToSaveResponse(
                this.clinicasService.update(this.clinicas));
        } else {
            this.subscribeToSaveResponse(
                this.clinicasService.create(this.clinicas));
        }
    }

    private subscribeToSaveResponse(result: Observable<Clinicas>) {
        result.subscribe((res: Clinicas) =>
            this.onSaveSuccess(res), (res: Response) => this.onSaveError());
    }

    private onSaveSuccess(result: Clinicas) {
        this.eventManager.broadcast({ name: 'clinicasListModification', content: 'OK'});
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
    selector: 'jhi-clinicas-popup',
    template: ''
})
export class ClinicasPopupComponent implements OnInit, OnDestroy {

    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private clinicasPopupService: ClinicasPopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            if ( params['id'] ) {
                this.clinicasPopupService
                    .open(ClinicasDialogComponent as Component, params['id']);
            } else {
                this.clinicasPopupService
                    .open(ClinicasDialogComponent as Component);
            }
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
