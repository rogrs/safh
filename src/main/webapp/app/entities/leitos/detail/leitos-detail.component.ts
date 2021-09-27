import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { ILeitos } from '../leitos.model';

@Component({
  selector: 'jhi-leitos-detail',
  templateUrl: './leitos-detail.component.html',
})
export class LeitosDetailComponent implements OnInit {
  leitos: ILeitos | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ leitos }) => {
      this.leitos = leitos;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
