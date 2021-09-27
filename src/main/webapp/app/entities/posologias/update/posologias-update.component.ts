import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import { IPosologias, Posologias } from '../posologias.model';
import { PosologiasService } from '../service/posologias.service';

@Component({
  selector: 'jhi-posologias-update',
  templateUrl: './posologias-update.component.html',
})
export class PosologiasUpdateComponent implements OnInit {
  isSaving = false;

  editForm = this.fb.group({
    id: [],
    posologia: [null, [Validators.required, Validators.maxLength(40)]],
  });

  constructor(protected posologiasService: PosologiasService, protected activatedRoute: ActivatedRoute, protected fb: FormBuilder) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ posologias }) => {
      this.updateForm(posologias);
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const posologias = this.createFromForm();
    if (posologias.id !== undefined) {
      this.subscribeToSaveResponse(this.posologiasService.update(posologias));
    } else {
      this.subscribeToSaveResponse(this.posologiasService.create(posologias));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IPosologias>>): void {
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

  protected updateForm(posologias: IPosologias): void {
    this.editForm.patchValue({
      id: posologias.id,
      posologia: posologias.posologia,
    });
  }

  protected createFromForm(): IPosologias {
    return {
      ...new Posologias(),
      id: this.editForm.get(['id'])!.value,
      posologia: this.editForm.get(['posologia'])!.value,
    };
  }
}
