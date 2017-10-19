import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Subscription } from 'rxjs/Rx';
import { JhiEventManager } from 'ng-jhipster';

import { Dietas } from './dietas.model';
import { DietasService } from './dietas.service';

@Component({
    selector: 'jhi-dietas-detail',
    templateUrl: './dietas-detail.component.html'
})
export class DietasDetailComponent implements OnInit, OnDestroy {

    dietas: Dietas;
    private subscription: Subscription;
    private eventSubscriber: Subscription;

    constructor(
        private eventManager: JhiEventManager,
        private dietasService: DietasService,
        private route: ActivatedRoute
    ) {
    }

    ngOnInit() {
        this.subscription = this.route.params.subscribe((params) => {
            this.load(params['id']);
        });
        this.registerChangeInDietas();
    }

    load(id) {
        this.dietasService.find(id).subscribe((dietas) => {
            this.dietas = dietas;
        });
    }
    previousState() {
        window.history.back();
    }

    ngOnDestroy() {
        this.subscription.unsubscribe();
        this.eventManager.destroy(this.eventSubscriber);
    }

    registerChangeInDietas() {
        this.eventSubscriber = this.eventManager.subscribe(
            'dietasListModification',
            (response) => this.load(this.dietas.id)
        );
    }
}
