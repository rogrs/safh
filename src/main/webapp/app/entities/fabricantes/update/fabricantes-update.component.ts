import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import { IFabricantes, Fabricantes } from '../fabricantes.model';
import { FabricantesService } from '../service/fabricantes.service';

@Component({
  selector: 'jhi-fabricantes-update',
  templateUrl: './fabricantes-update.component.html',
})
export class FabricantesUpdateComponent implements OnInit {
  isSaving = false;

  editForm = this.fb.group({
    id: [],
    fabricante: [null, [Validators.required, Validators.maxLength(60)]],
  });

  constructor(protected fabricantesService: FabricantesService, protected activatedRoute: ActivatedRoute, protected fb: FormBuilder) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ fabricantes }) => {
      this.updateForm(fabricantes);
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const fabricantes = this.createFromForm();
    if (fabricantes.id !== undefined) {
      this.subscribeToSaveResponse(this.fabricantesService.update(fabricantes));
    } else {
      this.subscribeToSaveResponse(this.fabricantesService.create(fabricantes));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IFabricantes>>): void {
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

  protected updateForm(fabricantes: IFabricantes): void {
    this.editForm.patchValue({
      id: fabricantes.id,
      fabricante: fabricantes.fabricante,
    });
  }

  protected createFromForm(): IFabricantes {
    return {
      ...new Fabricantes(),
      id: this.editForm.get(['id'])!.value,
      fabricante: this.editForm.get(['fabricante'])!.value,
    };
  }
}
