import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IMedicos } from '../medicos.model';

@Component({
  selector: 'jhi-medicos-detail',
  templateUrl: './medicos-detail.component.html',
})
export class MedicosDetailComponent implements OnInit {
  medicos: IMedicos | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ medicos }) => {
      this.medicos = medicos;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
