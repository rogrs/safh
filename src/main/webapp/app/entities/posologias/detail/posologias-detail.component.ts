import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IPosologias } from '../posologias.model';

@Component({
  selector: 'jhi-posologias-detail',
  templateUrl: './posologias-detail.component.html',
})
export class PosologiasDetailComponent implements OnInit {
  posologias: IPosologias | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ posologias }) => {
      this.posologias = posologias;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
