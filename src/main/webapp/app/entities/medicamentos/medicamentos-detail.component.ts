import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Subscription } from 'rxjs/Rx';
import { JhiEventManager } from 'ng-jhipster';

import { Medicamentos } from './medicamentos.model';
import { MedicamentosService } from './medicamentos.service';

@Component({
    selector: 'jhi-medicamentos-detail',
    templateUrl: './medicamentos-detail.component.html'
})
export class MedicamentosDetailComponent implements OnInit, OnDestroy {

    medicamentos: Medicamentos;
    private subscription: Subscription;
    private eventSubscriber: Subscription;

    constructor(
        private eventManager: JhiEventManager,
        private medicamentosService: MedicamentosService,
        private route: ActivatedRoute
    ) {
    }

    ngOnInit() {
        this.subscription = this.route.params.subscribe((params) => {
            this.load(params['id']);
        });
        this.registerChangeInMedicamentos();
    }

    load(id) {
        this.medicamentosService.find(id).subscribe((medicamentos) => {
            this.medicamentos = medicamentos;
        });
    }
    previousState() {
        window.history.back();
    }

    ngOnDestroy() {
        this.subscription.unsubscribe();
        this.eventManager.destroy(this.eventSubscriber);
    }

    registerChangeInMedicamentos() {
        this.eventSubscriber = this.eventManager.subscribe(
            'medicamentosListModification',
            (response) => this.load(this.medicamentos.id)
        );
    }
}
