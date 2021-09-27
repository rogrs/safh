import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import { IEspecialidades, Especialidades } from '../especialidades.model';
import { EspecialidadesService } from '../service/especialidades.service';

@Component({
  selector: 'jhi-especialidades-update',
  templateUrl: './especialidades-update.component.html',
})
export class EspecialidadesUpdateComponent implements OnInit {
  isSaving = false;

  editForm = this.fb.group({
    id: [],
    especialidade: [null, [Validators.required, Validators.maxLength(60)]],
  });

  constructor(
    protected especialidadesService: EspecialidadesService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ especialidades }) => {
      this.updateForm(especialidades);
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const especialidades = this.createFromForm();
    if (especialidades.id !== undefined) {
      this.subscribeToSaveResponse(this.especialidadesService.update(especialidades));
    } else {
      this.subscribeToSaveResponse(this.especialidadesService.create(especialidades));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IEspecialidades>>): void {
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

  protected updateForm(especialidades: IEspecialidades): void {
    this.editForm.patchValue({
      id: especialidades.id,
      especialidade: especialidades.especialidade,
    });
  }

  protected createFromForm(): IEspecialidades {
    return {
      ...new Especialidades(),
      id: this.editForm.get(['id'])!.value,
      especialidade: this.editForm.get(['especialidade'])!.value,
    };
  }
}
