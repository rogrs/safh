import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Response } from '@angular/http';

import { Observable } from 'rxjs/Rx';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager, JhiAlertService } from 'ng-jhipster';

import { Enfermarias } from './enfermarias.model';
import { EnfermariasPopupService } from './enfermarias-popup.service';
import { EnfermariasService } from './enfermarias.service';

@Component({
    selector: 'jhi-enfermarias-dialog',
    templateUrl: './enfermarias-dialog.component.html'
})
export class EnfermariasDialogComponent implements OnInit {

    enfermarias: Enfermarias;
    isSaving: boolean;

    constructor(
        public activeModal: NgbActiveModal,
        private jhiAlertService: JhiAlertService,
        private enfermariasService: EnfermariasService,
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
        if (this.enfermarias.id !== undefined) {
            this.subscribeToSaveResponse(
                this.enfermariasService.update(this.enfermarias));
        } else {
            this.subscribeToSaveResponse(
                this.enfermariasService.create(this.enfermarias));
        }
    }

    private subscribeToSaveResponse(result: Observable<Enfermarias>) {
        result.subscribe((res: Enfermarias) =>
            this.onSaveSuccess(res), (res: Response) => this.onSaveError());
    }

    private onSaveSuccess(result: Enfermarias) {
        this.eventManager.broadcast({ name: 'enfermariasListModification', content: 'OK'});
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
    selector: 'jhi-enfermarias-popup',
    template: ''
})
export class EnfermariasPopupComponent implements OnInit, OnDestroy {

    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private enfermariasPopupService: EnfermariasPopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            if ( params['id'] ) {
                this.enfermariasPopupService
                    .open(EnfermariasDialogComponent as Component, params['id']);
            } else {
                this.enfermariasPopupService
                    .open(EnfermariasDialogComponent as Component);
            }
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
