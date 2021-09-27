import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import { IClinicas, Clinicas } from '../clinicas.model';
import { ClinicasService } from '../service/clinicas.service';

@Component({
  selector: 'jhi-clinicas-update',
  templateUrl: './clinicas-update.component.html',
})
export class ClinicasUpdateComponent implements OnInit {
  isSaving = false;

  editForm = this.fb.group({
    id: [],
    clinica: [null, [Validators.required, Validators.maxLength(80)]],
    descricao: [null, [Validators.maxLength(100)]],
  });

  constructor(protected clinicasService: ClinicasService, protected activatedRoute: ActivatedRoute, protected fb: FormBuilder) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ clinicas }) => {
      this.updateForm(clinicas);
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const clinicas = this.createFromForm();
    if (clinicas.id !== undefined) {
      this.subscribeToSaveResponse(this.clinicasService.update(clinicas));
    } else {
      this.subscribeToSaveResponse(this.clinicasService.create(clinicas));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IClinicas>>): void {
    result.pipe(finalize(() => this.onSaveFinalize())).subscribe(
      () => this.onSaveSuccess(),
      () => this.onSaveError()
    );
  }

  protected onSaveSuccess(): void {
    this.previousState();
  }

  protected onSaveError(): void {
    // Api for inheritance.
  }

  protected onSaveFinalize(): void {
    this.isSaving = false;
  }

  protected updateForm(clinicas: IClinicas): void {
    this.editForm.patchValue({
      id: clinicas.id,
      clinica: clinicas.clinica,
      descricao: clinicas.descricao,
    });
  }

  protected createFromForm(): IClinicas {
    return {
      ...new Clinicas(),
      id: this.editForm.get(['id'])!.value,
      clinica: this.editForm.get(['clinica'])!.value,
      descricao: this.editForm.get(['descricao'])!.value,
    };
  }
}
