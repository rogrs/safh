import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import { IDietas, Dietas } from '../dietas.model';
import { DietasService } from '../service/dietas.service';

@Component({
  selector: 'jhi-dietas-update',
  templateUrl: './dietas-update.component.html',
})
export class DietasUpdateComponent implements OnInit {
  isSaving = false;

  editForm = this.fb.group({
    id: [],
    dieta: [null, [Validators.required, Validators.maxLength(40)]],
    descricao: [null, [Validators.maxLength(255)]],
  });

  constructor(protected dietasService: DietasService, protected activatedRoute: ActivatedRoute, protected fb: FormBuilder) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ dietas }) => {
      this.updateForm(dietas);
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const dietas = this.createFromForm();
    if (dietas.id !== undefined) {
      this.subscribeToSaveResponse(this.dietasService.update(dietas));
    } else {
      this.subscribeToSaveResponse(this.dietasService.create(dietas));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IDietas>>): void {
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

  protected updateForm(dietas: IDietas): void {
    this.editForm.patchValue({
      id: dietas.id,
      dieta: dietas.dieta,
      descricao: dietas.descricao,
    });
  }

  protected createFromForm(): IDietas {
    return {
      ...new Dietas(),
      id: this.editForm.get(['id'])!.value,
      dieta: this.editForm.get(['dieta'])!.value,
      descricao: this.editForm.get(['descricao'])!.value,
    };
  }
}
