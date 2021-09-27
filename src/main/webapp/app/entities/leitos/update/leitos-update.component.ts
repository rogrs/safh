import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import { ILeitos, Leitos } from '../leitos.model';
import { LeitosService } from '../service/leitos.service';

@Component({
  selector: 'jhi-leitos-update',
  templateUrl: './leitos-update.component.html',
})
export class LeitosUpdateComponent implements OnInit {
  isSaving = false;

  editForm = this.fb.group({
    id: [],
    leito: [null, [Validators.required, Validators.maxLength(60)]],
    tipo: [null, [Validators.maxLength(40)]],
  });

  constructor(protected leitosService: LeitosService, protected activatedRoute: ActivatedRoute, protected fb: FormBuilder) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ leitos }) => {
      this.updateForm(leitos);
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const leitos = this.createFromForm();
    if (leitos.id !== undefined) {
      this.subscribeToSaveResponse(this.leitosService.update(leitos));
    } else {
      this.subscribeToSaveResponse(this.leitosService.create(leitos));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ILeitos>>): void {
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

  protected updateForm(leitos: ILeitos): void {
    this.editForm.patchValue({
      id: leitos.id,
      leito: leitos.leito,
      tipo: leitos.tipo,
    });
  }

  protected createFromForm(): ILeitos {
    return {
      ...new Leitos(),
      id: this.editForm.get(['id'])!.value,
      leito: this.editForm.get(['leito'])!.value,
      tipo: this.editForm.get(['tipo'])!.value,
    };
  }
}
