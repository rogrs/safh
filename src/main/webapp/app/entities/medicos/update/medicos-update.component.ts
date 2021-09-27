import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { IMedicos, Medicos } from '../medicos.model';
import { MedicosService } from '../service/medicos.service';
import { IEspecialidades } from 'app/entities/especialidades/especialidades.model';
import { EspecialidadesService } from 'app/entities/especialidades/service/especialidades.service';

@Component({
  selector: 'jhi-medicos-update',
  templateUrl: './medicos-update.component.html',
})
export class MedicosUpdateComponent implements OnInit {
  isSaving = false;

  especialidadesSharedCollection: IEspecialidades[] = [];

  editForm = this.fb.group({
    id: [],
    nome: [null, [Validators.required, Validators.maxLength(255)]],
    crm: [null, [Validators.required, Validators.maxLength(40)]],
    cpf: [null, [Validators.maxLength(11)]],
    email: [null, [Validators.maxLength(100)]],
    cep: [null, [Validators.maxLength(10)]],
    logradouro: [null, [Validators.maxLength(80)]],
    numero: [null, [Validators.maxLength(10)]],
    complemento: [null, [Validators.maxLength(60)]],
    bairro: [null, [Validators.maxLength(60)]],
    cidade: [null, [Validators.maxLength(60)]],
    uF: [],
    especialidades: [],
  });

  constructor(
    protected medicosService: MedicosService,
    protected especialidadesService: EspecialidadesService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ medicos }) => {
      this.updateForm(medicos);

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const medicos = this.createFromForm();
    if (medicos.id !== undefined) {
      this.subscribeToSaveResponse(this.medicosService.update(medicos));
    } else {
      this.subscribeToSaveResponse(this.medicosService.create(medicos));
    }
  }

  trackEspecialidadesById(index: number, item: IEspecialidades): number {
    return item.id!;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IMedicos>>): void {
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

  protected updateForm(medicos: IMedicos): void {
    this.editForm.patchValue({
      id: medicos.id,
      nome: medicos.nome,
      crm: medicos.crm,
      cpf: medicos.cpf,
      email: medicos.email,
      cep: medicos.cep,
      logradouro: medicos.logradouro,
      numero: medicos.numero,
      complemento: medicos.complemento,
      bairro: medicos.bairro,
      cidade: medicos.cidade,
      uF: medicos.uF,
      especialidades: medicos.especialidades,
    });

    this.especialidadesSharedCollection = this.especialidadesService.addEspecialidadesToCollectionIfMissing(
      this.especialidadesSharedCollection,
      medicos.especialidades
    );
  }

  protected loadRelationshipsOptions(): void {
    this.especialidadesService
      .query()
      .pipe(map((res: HttpResponse<IEspecialidades[]>) => res.body ?? []))
      .pipe(
        map((especialidades: IEspecialidades[]) =>
          this.especialidadesService.addEspecialidadesToCollectionIfMissing(especialidades, this.editForm.get('especialidades')!.value)
        )
      )
      .subscribe((especialidades: IEspecialidades[]) => (this.especialidadesSharedCollection = especialidades));
  }

  protected createFromForm(): IMedicos {
    return {
      ...new Medicos(),
      id: this.editForm.get(['id'])!.value,
      nome: this.editForm.get(['nome'])!.value,
      crm: this.editForm.get(['crm'])!.value,
      cpf: this.editForm.get(['cpf'])!.value,
      email: this.editForm.get(['email'])!.value,
      cep: this.editForm.get(['cep'])!.value,
      logradouro: this.editForm.get(['logradouro'])!.value,
      numero: this.editForm.get(['numero'])!.value,
      complemento: this.editForm.get(['complemento'])!.value,
      bairro: this.editForm.get(['bairro'])!.value,
      cidade: this.editForm.get(['cidade'])!.value,
      uF: this.editForm.get(['uF'])!.value,
      especialidades: this.editForm.get(['especialidades'])!.value,
    };
  }
}
