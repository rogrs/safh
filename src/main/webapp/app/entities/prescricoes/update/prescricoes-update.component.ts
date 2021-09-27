import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import { IPrescricoes, Prescricoes } from '../prescricoes.model';
import { PrescricoesService } from '../service/prescricoes.service';

@Component({
  selector: 'jhi-prescricoes-update',
  templateUrl: './prescricoes-update.component.html',
})
export class PrescricoesUpdateComponent implements OnInit {
  isSaving = false;

  editForm = this.fb.group({
    id: [],
    prescricao: [null, [Validators.required, Validators.maxLength(100)]],
  });

  constructor(protected prescricoesService: PrescricoesService, protected activatedRoute: ActivatedRoute, protected fb: FormBuilder) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ prescricoes }) => {
      this.updateForm(prescricoes);
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const prescricoes = this.createFromForm();
    if (prescricoes.id !== undefined) {
      this.subscribeToSaveResponse(this.prescricoesService.update(prescricoes));
    } else {
      this.subscribeToSaveResponse(this.prescricoesService.create(prescricoes));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IPrescricoes>>): void {
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

  protected updateForm(prescricoes: IPrescricoes): void {
    this.editForm.patchValue({
      id: prescricoes.id,
      prescricao: prescricoes.prescricao,
    });
  }

  protected createFromForm(): IPrescricoes {
    return {
      ...new Prescricoes(),
      id: this.editForm.get(['id'])!.value,
      prescricao: this.editForm.get(['prescricao'])!.value,
    };
  }
}
