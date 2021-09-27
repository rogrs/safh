import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IMedicamentos } from '../medicamentos.model';

@Component({
  selector: 'jhi-medicamentos-detail',
  templateUrl: './medicamentos-detail.component.html',
})
export class MedicamentosDetailComponent implements OnInit {
  medicamentos: IMedicamentos | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ medicamentos }) => {
      this.medicamentos = medicamentos;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
