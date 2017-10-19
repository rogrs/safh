import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Subscription } from 'rxjs/Rx';
import { JhiEventManager } from 'ng-jhipster';

import { InternacoesDetalhes } from './internacoes-detalhes.model';
import { InternacoesDetalhesService } from './internacoes-detalhes.service';

@Component({
    selector: 'jhi-internacoes-detalhes-detail',
    templateUrl: './internacoes-detalhes-detail.component.html'
})
export class InternacoesDetalhesDetailComponent implements OnInit, OnDestroy {

    internacoesDetalhes: InternacoesDetalhes;
    private subscription: Subscription;
    private eventSubscriber: Subscription;

    constructor(
        private eventManager: JhiEventManager,
        private internacoesDetalhesService: InternacoesDetalhesService,
        private route: ActivatedRoute
    ) {
    }

    ngOnInit() {
        this.subscription = this.route.params.subscribe((params) => {
            this.load(params['id']);
        });
        this.registerChangeInInternacoesDetalhes();
    }

    load(id) {
        this.internacoesDetalhesService.find(id).subscribe((internacoesDetalhes) => {
            this.internacoesDetalhes = internacoesDetalhes;
        });
    }
    previousState() {
        window.history.back();
    }

    ngOnDestroy() {
        this.subscription.unsubscribe();
        this.eventManager.destroy(this.eventSubscriber);
    }

    registerChangeInInternacoesDetalhes() {
        this.eventSubscriber = this.eventManager.subscribe(
            'internacoesDetalhesListModification',
            (response) => this.load(this.internacoesDetalhes.id)
        );
    }
}
