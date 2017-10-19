import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Subscription } from 'rxjs/Rx';
import { JhiEventManager } from 'ng-jhipster';

import { Medicos } from './medicos.model';
import { MedicosService } from './medicos.service';

@Component({
    selector: 'jhi-medicos-detail',
    templateUrl: './medicos-detail.component.html'
})
export class MedicosDetailComponent implements OnInit, OnDestroy {

    medicos: Medicos;
    private subscription: Subscription;
    private eventSubscriber: Subscription;

    constructor(
        private eventManager: JhiEventManager,
        private medicosService: MedicosService,
        private route: ActivatedRoute
    ) {
    }

    ngOnInit() {
        this.subscription = this.route.params.subscribe((params) => {
            this.load(params['id']);
        });
        this.registerChangeInMedicos();
    }

    load(id) {
        this.medicosService.find(id).subscribe((medicos) => {
            this.medicos = medicos;
        });
    }
    previousState() {
        window.history.back();
    }

    ngOnDestroy() {
        this.subscription.unsubscribe();
        this.eventManager.destroy(this.eventSubscriber);
    }

    registerChangeInMedicos() {
        this.eventSubscriber = this.eventManager.subscribe(
            'medicosListModification',
            (response) => this.load(this.medicos.id)
        );
    }
}
