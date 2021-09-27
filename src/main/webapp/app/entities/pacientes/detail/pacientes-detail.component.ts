import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IPacientes } from '../pacientes.model';

@Component({
  selector: 'jhi-pacientes-detail',
  templateUrl: './pacientes-detail.component.html',
})
export class PacientesDetailComponent implements OnInit {
  pacientes: IPacientes | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ pacientes }) => {
      this.pacientes = pacientes;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
