import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { IInternacoes, Internacoes } from '../internacoes.model';
import { InternacoesService } from '../service/internacoes.service';
import { IPacientes } from 'app/entities/pacientes/pacientes.model';
import { PacientesService } from 'app/entities/pacientes/service/pacientes.service';
import { IClinicas } from 'app/entities/clinicas/clinicas.model';
import { ClinicasService } from 'app/entities/clinicas/service/clinicas.service';
import { IMedicos } from 'app/entities/medicos/medicos.model';
import { MedicosService } from 'app/entities/medicos/service/medicos.service';

@Component({
  selector: 'jhi-internacoes-update',
  templateUrl: './internacoes-update.component.html',
})
export class InternacoesUpdateComponent implements OnInit {
  isSaving = false;

  pacientesSharedCollection: IPacientes[] = [];
  clinicasSharedCollection: IClinicas[] = [];
  medicosSharedCollection: IMedicos[] = [];

  editForm = this.fb.group({
    id: [],
    dataInternacao: [null, [Validators.required]],
    descricao: [null, [Validators.required, Validators.maxLength(200)]],
    pacientes: [],
    clinicas: [],
    medicos: [],
  });

  constructor(
    protected internacoesService: InternacoesService,
    protected pacientesService: PacientesService,
    protected clinicasService: ClinicasService,
    protected medicosService: MedicosService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ internacoes }) => {
      this.updateForm(internacoes);

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const internacoes = this.createFromForm();
    if (internacoes.id !== undefined) {
      this.subscribeToSaveResponse(this.internacoesService.update(internacoes));
    } else {
      this.subscribeToSaveResponse(this.internacoesService.create(internacoes));
    }
  }

  trackPacientesById(index: number, item: IPacientes): number {
    return item.id!;
  }

  trackClinicasById(index: number, item: IClinicas): number {
    return item.id!;
  }

  trackMedicosById(index: number, item: IMedicos): number {
    return item.id!;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IInternacoes>>): void {
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

  protected updateForm(internacoes: IInternacoes): void {
    this.editForm.patchValue({
      id: internacoes.id,
      dataInternacao: internacoes.dataInternacao,
      descricao: internacoes.descricao,
      pacientes: internacoes.pacientes,
      clinicas: internacoes.clinicas,
      medicos: internacoes.medicos,
    });

    this.pacientesSharedCollection = this.pacientesService.addPacientesToCollectionIfMissing(
      this.pacientesSharedCollection,
      internacoes.pacientes
    );
    this.clinicasSharedCollection = this.clinicasService.addClinicasToCollectionIfMissing(
      this.clinicasSharedCollection,
      internacoes.clinicas
    );
    this.medicosSharedCollection = this.medicosService.addMedicosToCollectionIfMissing(this.medicosSharedCollection, internacoes.medicos);
  }

  protected loadRelationshipsOptions(): void {
    this.pacientesService
      .query()
      .pipe(map((res: HttpResponse<IPacientes[]>) => res.body ?? []))
      .pipe(
        map((pacientes: IPacientes[]) =>
          this.pacientesService.addPacientesToCollectionIfMissing(pacientes, this.editForm.get('pacientes')!.value)
        )
      )
      .subscribe((pacientes: IPacientes[]) => (this.pacientesSharedCollection = pacientes));

    this.clinicasService
      .query()
      .pipe(map((res: HttpResponse<IClinicas[]>) => res.body ?? []))
      .pipe(
        map((clinicas: IClinicas[]) =>
          this.clinicasService.addClinicasToCollectionIfMissing(clinicas, this.editForm.get('clinicas')!.value)
        )
      )
      .subscribe((clinicas: IClinicas[]) => (this.clinicasSharedCollection = clinicas));

    this.medicosService
      .query()
      .pipe(map((res: HttpResponse<IMedicos[]>) => res.body ?? []))
      .pipe(map((medicos: IMedicos[]) => this.medicosService.addMedicosToCollectionIfMissing(medicos, this.editForm.get('medicos')!.value)))
      .subscribe((medicos: IMedicos[]) => (this.medicosSharedCollection = medicos));
  }

  protected createFromForm(): IInternacoes {
    return {
      ...new Internacoes(),
      id: this.editForm.get(['id'])!.value,
      dataInternacao: this.editForm.get(['dataInternacao'])!.value,
      descricao: this.editForm.get(['descricao'])!.value,
      pacientes: this.editForm.get(['pacientes'])!.value,
      clinicas: this.editForm.get(['clinicas'])!.value,
      medicos: this.editForm.get(['medicos'])!.value,
    };
  }
}
