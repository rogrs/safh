import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IEspecialidades } from '../especialidades.model';

@Component({
  selector: 'jhi-especialidades-detail',
  templateUrl: './especialidades-detail.component.html',
})
export class EspecialidadesDetailComponent implements OnInit {
  especialidades: IEspecialidades | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ especialidades }) => {
      this.especialidades = especialidades;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
