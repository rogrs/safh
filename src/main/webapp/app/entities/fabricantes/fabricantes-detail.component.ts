import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Subscription } from 'rxjs/Rx';
import { JhiEventManager } from 'ng-jhipster';

import { Fabricantes } from './fabricantes.model';
import { FabricantesService } from './fabricantes.service';

@Component({
    selector: 'jhi-fabricantes-detail',
    templateUrl: './fabricantes-detail.component.html'
})
export class FabricantesDetailComponent implements OnInit, OnDestroy {

    fabricantes: Fabricantes;
    private subscription: Subscription;
    private eventSubscriber: Subscription;

    constructor(
        private eventManager: JhiEventManager,
        private fabricantesService: FabricantesService,
        private route: ActivatedRoute
    ) {
    }

    ngOnInit() {
        this.subscription = this.route.params.subscribe((params) => {
            this.load(params['id']);
        });
        this.registerChangeInFabricantes();
    }

    load(id) {
        this.fabricantesService.find(id).subscribe((fabricantes) => {
            this.fabricantes = fabricantes;
        });
    }
    previousState() {
        window.history.back();
    }

    ngOnDestroy() {
        this.subscription.unsubscribe();
        this.eventManager.destroy(this.eventSubscriber);
    }

    registerChangeInFabricantes() {
        this.eventSubscriber = this.eventManager.subscribe(
            'fabricantesListModification',
            (response) => this.load(this.fabricantes.id)
        );
    }
}
