import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Subscription } from 'rxjs/Rx';
import { JhiEventManager } from 'ng-jhipster';

import { Internacoes } from './internacoes.model';
import { InternacoesService } from './internacoes.service';

@Component({
    selector: 'jhi-internacoes-detail',
    templateUrl: './internacoes-detail.component.html'
})
export class InternacoesDetailComponent implements OnInit, OnDestroy {

    internacoes: Internacoes;
    private subscription: Subscription;
    private eventSubscriber: Subscription;

    constructor(
        private eventManager: JhiEventManager,
        private internacoesService: InternacoesService,
        private route: ActivatedRoute
    ) {
    }

    ngOnInit() {
        this.subscription = this.route.params.subscribe((params) => {
            this.load(params['id']);
        });
        this.registerChangeInInternacoes();
    }

    load(id) {
        this.internacoesService.find(id).subscribe((internacoes) => {
            this.internacoes = internacoes;
        });
    }
    previousState() {
        window.history.back();
    }

    ngOnDestroy() {
        this.subscription.unsubscribe();
        this.eventManager.destroy(this.eventSubscriber);
    }

    registerChangeInInternacoes() {
        this.eventSubscriber = this.eventManager.subscribe(
            'internacoesListModification',
            (response) => this.load(this.internacoes.id)
        );
    }
}
