import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Response } from '@angular/http';

import { Observable } from 'rxjs/Rx';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager, JhiAlertService } from 'ng-jhipster';

import { Leitos } from './leitos.model';
import { LeitosPopupService } from './leitos-popup.service';
import { LeitosService } from './leitos.service';

@Component({
    selector: 'jhi-leitos-dialog',
    templateUrl: './leitos-dialog.component.html'
})
export class LeitosDialogComponent implements OnInit {

    leitos: Leitos;
    isSaving: boolean;

    constructor(
        public activeModal: NgbActiveModal,
        private jhiAlertService: JhiAlertService,
        private leitosService: LeitosService,
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
        if (this.leitos.id !== undefined) {
            this.subscribeToSaveResponse(
                this.leitosService.update(this.leitos));
        } else {
            this.subscribeToSaveResponse(
                this.leitosService.create(this.leitos));
        }
    }

    private subscribeToSaveResponse(result: Observable<Leitos>) {
        result.subscribe((res: Leitos) =>
            this.onSaveSuccess(res), (res: Response) => this.onSaveError());
    }

    private onSaveSuccess(result: Leitos) {
        this.eventManager.broadcast({ name: 'leitosListModification', content: 'OK'});
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
    selector: 'jhi-leitos-popup',
    template: ''
})
export class LeitosPopupComponent implements OnInit, OnDestroy {

    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private leitosPopupService: LeitosPopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            if ( params['id'] ) {
                this.leitosPopupService
                    .open(LeitosDialogComponent as Component, params['id']);
            } else {
                this.leitosPopupService
                    .open(LeitosDialogComponent as Component);
            }
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
