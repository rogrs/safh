import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { InternacoesDetalhes } from './internacoes-detalhes.model';
import { InternacoesDetalhesPopupService } from './internacoes-detalhes-popup.service';
import { InternacoesDetalhesService } from './internacoes-detalhes.service';

@Component({
    selector: 'jhi-internacoes-detalhes-delete-dialog',
    templateUrl: './internacoes-detalhes-delete-dialog.component.html'
})
export class InternacoesDetalhesDeleteDialogComponent {

    internacoesDetalhes: InternacoesDetalhes;

    constructor(
        private internacoesDetalhesService: InternacoesDetalhesService,
        public activeModal: NgbActiveModal,
        private eventManager: JhiEventManager
    ) {
    }

    clear() {
        this.activeModal.dismiss('cancel');
    }

    confirmDelete(id: number) {
        this.internacoesDetalhesService.delete(id).subscribe((response) => {
            this.eventManager.broadcast({
                name: 'internacoesDetalhesListModification',
                content: 'Deleted an internacoesDetalhes'
            });
            this.activeModal.dismiss(true);
        });
    }
}

@Component({
    selector: 'jhi-internacoes-detalhes-delete-popup',
    template: ''
})
export class InternacoesDetalhesDeletePopupComponent implements OnInit, OnDestroy {

    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private internacoesDetalhesPopupService: InternacoesDetalhesPopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            this.internacoesDetalhesPopupService
                .open(InternacoesDetalhesDeleteDialogComponent as Component, params['id']);
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
