import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IDietas } from '../dietas.model';

@Component({
  selector: 'jhi-dietas-detail',
  templateUrl: './dietas-detail.component.html',
})
export class DietasDetailComponent implements OnInit {
  dietas: IDietas | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ dietas }) => {
      this.dietas = dietas;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
