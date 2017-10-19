import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Response } from '@angular/http';

import { Observable } from 'rxjs/Rx';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager, JhiAlertService } from 'ng-jhipster';

import { Posologias } from './posologias.model';
import { PosologiasPopupService } from './posologias-popup.service';
import { PosologiasService } from './posologias.service';

@Component({
    selector: 'jhi-posologias-dialog',
    templateUrl: './posologias-dialog.component.html'
})
export class PosologiasDialogComponent implements OnInit {

    posologias: Posologias;
    isSaving: boolean;

    constructor(
        public activeModal: NgbActiveModal,
        private jhiAlertService: JhiAlertService,
        private posologiasService: PosologiasService,
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
        if (this.posologias.id !== undefined) {
            this.subscribeToSaveResponse(
                this.posologiasService.update(this.posologias));
        } else {
            this.subscribeToSaveResponse(
                this.posologiasService.create(this.posologias));
        }
    }

    private subscribeToSaveResponse(result: Observable<Posologias>) {
        result.subscribe((res: Posologias) =>
            this.onSaveSuccess(res), (res: Response) => this.onSaveError());
    }

    private onSaveSuccess(result: Posologias) {
        this.eventManager.broadcast({ name: 'posologiasListModification', content: 'OK'});
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
    selector: 'jhi-posologias-popup',
    template: ''
})
export class PosologiasPopupComponent implements OnInit, OnDestroy {

    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private posologiasPopupService: PosologiasPopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            if ( params['id'] ) {
                this.posologiasPopupService
                    .open(PosologiasDialogComponent as Component, params['id']);
            } else {
                this.posologiasPopupService
                    .open(PosologiasDialogComponent as Component);
            }
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
