import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Response } from '@angular/http';

import { Observable } from 'rxjs/Rx';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager, JhiAlertService } from 'ng-jhipster';

import { Dietas } from './dietas.model';
import { DietasPopupService } from './dietas-popup.service';
import { DietasService } from './dietas.service';

@Component({
    selector: 'jhi-dietas-dialog',
    templateUrl: './dietas-dialog.component.html'
})
export class DietasDialogComponent implements OnInit {

    dietas: Dietas;
    isSaving: boolean;

    constructor(
        public activeModal: NgbActiveModal,
        private jhiAlertService: JhiAlertService,
        private dietasService: DietasService,
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
        if (this.dietas.id !== undefined) {
            this.subscribeToSaveResponse(
                this.dietasService.update(this.dietas));
        } else {
            this.subscribeToSaveResponse(
                this.dietasService.create(this.dietas));
        }
    }

    private subscribeToSaveResponse(result: Observable<Dietas>) {
        result.subscribe((res: Dietas) =>
            this.onSaveSuccess(res), (res: Response) => this.onSaveError());
    }

    private onSaveSuccess(result: Dietas) {
        this.eventManager.broadcast({ name: 'dietasListModification', content: 'OK'});
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
    selector: 'jhi-dietas-popup',
    template: ''
})
export class DietasPopupComponent implements OnInit, OnDestroy {

    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private dietasPopupService: DietasPopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            if ( params['id'] ) {
                this.dietasPopupService
                    .open(DietasDialogComponent as Component, params['id']);
            } else {
                this.dietasPopupService
                    .open(DietasDialogComponent as Component);
            }
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
