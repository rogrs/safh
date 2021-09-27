import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IFabricantes } from '../fabricantes.model';

@Component({
  selector: 'jhi-fabricantes-detail',
  templateUrl: './fabricantes-detail.component.html',
})
export class FabricantesDetailComponent implements OnInit {
  fabricantes: IFabricantes | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ fabricantes }) => {
      this.fabricantes = fabricantes;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
