import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import { IEnfermarias, Enfermarias } from '../enfermarias.model';
import { EnfermariasService } from '../service/enfermarias.service';

@Component({
  selector: 'jhi-enfermarias-update',
  templateUrl: './enfermarias-update.component.html',
})
export class EnfermariasUpdateComponent implements OnInit {
  isSaving = false;

  editForm = this.fb.group({
    id: [],
    enfermaria: [null, [Validators.required, Validators.maxLength(60)]],
  });

  constructor(protected enfermariasService: EnfermariasService, protected activatedRoute: ActivatedRoute, protected fb: FormBuilder) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ enfermarias }) => {
      this.updateForm(enfermarias);
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const enfermarias = this.createFromForm();
    if (enfermarias.id !== undefined) {
      this.subscribeToSaveResponse(this.enfermariasService.update(enfermarias));
    } else {
      this.subscribeToSaveResponse(this.enfermariasService.create(enfermarias));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IEnfermarias>>): void {
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

  protected updateForm(enfermarias: IEnfermarias): void {
    this.editForm.patchValue({
      id: enfermarias.id,
      enfermaria: enfermarias.enfermaria,
    });
  }

  protected createFromForm(): IEnfermarias {
    return {
      ...new Enfermarias(),
      id: this.editForm.get(['id'])!.value,
      enfermaria: this.editForm.get(['enfermaria'])!.value,
    };
  }
}
