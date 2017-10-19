import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Response } from '@angular/http';

import { Observable } from 'rxjs/Rx';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager, JhiAlertService } from 'ng-jhipster';

import { Prescricoes } from './prescricoes.model';
import { PrescricoesPopupService } from './prescricoes-popup.service';
import { PrescricoesService } from './prescricoes.service';

@Component({
    selector: 'jhi-prescricoes-dialog',
    templateUrl: './prescricoes-dialog.component.html'
})
export class PrescricoesDialogComponent implements OnInit {

    prescricoes: Prescricoes;
    isSaving: boolean;

    constructor(
        public activeModal: NgbActiveModal,
        private jhiAlertService: JhiAlertService,
        private prescricoesService: PrescricoesService,
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
        if (this.prescricoes.id !== undefined) {
            this.subscribeToSaveResponse(
                this.prescricoesService.update(this.prescricoes));
        } else {
            this.subscribeToSaveResponse(
                this.prescricoesService.create(this.prescricoes));
        }
    }

    private subscribeToSaveResponse(result: Observable<Prescricoes>) {
        result.subscribe((res: Prescricoes) =>
            this.onSaveSuccess(res), (res: Response) => this.onSaveError());
    }

    private onSaveSuccess(result: Prescricoes) {
        this.eventManager.broadcast({ name: 'prescricoesListModification', content: 'OK'});
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
    selector: 'jhi-prescricoes-popup',
    template: ''
})
export class PrescricoesPopupComponent implements OnInit, OnDestroy {

    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private prescricoesPopupService: PrescricoesPopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            if ( params['id'] ) {
                this.prescricoesPopupService
                    .open(PrescricoesDialogComponent as Component, params['id']);
            } else {
                this.prescricoesPopupService
                    .open(PrescricoesDialogComponent as Component);
            }
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
