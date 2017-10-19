import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Response } from '@angular/http';

import { Observable } from 'rxjs/Rx';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager, JhiAlertService } from 'ng-jhipster';

import { Fabricantes } from './fabricantes.model';
import { FabricantesPopupService } from './fabricantes-popup.service';
import { FabricantesService } from './fabricantes.service';

@Component({
    selector: 'jhi-fabricantes-dialog',
    templateUrl: './fabricantes-dialog.component.html'
})
export class FabricantesDialogComponent implements OnInit {

    fabricantes: Fabricantes;
    isSaving: boolean;

    constructor(
        public activeModal: NgbActiveModal,
        private jhiAlertService: JhiAlertService,
        private fabricantesService: FabricantesService,
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
        if (this.fabricantes.id !== undefined) {
            this.subscribeToSaveResponse(
                this.fabricantesService.update(this.fabricantes));
        } else {
            this.subscribeToSaveResponse(
                this.fabricantesService.create(this.fabricantes));
        }
    }

    private subscribeToSaveResponse(result: Observable<Fabricantes>) {
        result.subscribe((res: Fabricantes) =>
            this.onSaveSuccess(res), (res: Response) => this.onSaveError());
    }

    private onSaveSuccess(result: Fabricantes) {
        this.eventManager.broadcast({ name: 'fabricantesListModification', content: 'OK'});
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
    selector: 'jhi-fabricantes-popup',
    template: ''
})
export class FabricantesPopupComponent implements OnInit, OnDestroy {

    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private fabricantesPopupService: FabricantesPopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            if ( params['id'] ) {
                this.fabricantesPopupService
                    .open(FabricantesDialogComponent as Component, params['id']);
            } else {
                this.fabricantesPopupService
                    .open(FabricantesDialogComponent as Component);
            }
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
