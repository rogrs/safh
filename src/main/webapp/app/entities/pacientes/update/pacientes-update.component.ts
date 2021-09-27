import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { IPacientes, Pacientes } from '../pacientes.model';
import { PacientesService } from '../service/pacientes.service';
import { IClinicas } from 'app/entities/clinicas/clinicas.model';
import { ClinicasService } from 'app/entities/clinicas/service/clinicas.service';
import { IEnfermarias } from 'app/entities/enfermarias/enfermarias.model';
import { EnfermariasService } from 'app/entities/enfermarias/service/enfermarias.service';
import { ILeitos } from 'app/entities/leitos/leitos.model';
import { LeitosService } from 'app/entities/leitos/service/leitos.service';

@Component({
  selector: 'jhi-pacientes-update',
  templateUrl: './pacientes-update.component.html',
})
export class PacientesUpdateComponent implements OnInit {
  isSaving = false;

  clinicasSharedCollection: IClinicas[] = [];
  enfermariasSharedCollection: IEnfermarias[] = [];
  leitosSharedCollection: ILeitos[] = [];

  editForm = this.fb.group({
    id: [],
    prontuario: [null, [Validators.required]],
    nome: [null, [Validators.required, Validators.maxLength(255)]],
    cpf: [null, [Validators.maxLength(11)]],
    email: [null, [Validators.maxLength(100)]],
    cep: [null, [Validators.maxLength(10)]],
    logradouro: [null, [Validators.maxLength(80)]],
    numero: [null, [Validators.maxLength(10)]],
    complemento: [null, [Validators.maxLength(60)]],
    bairro: [null, [Validators.maxLength(60)]],
    cidade: [null, [Validators.maxLength(60)]],
    uF: [],
    clinicas: [],
    enfermarias: [],
    leitos: [],
  });

  constructor(
    protected pacientesService: PacientesService,
    protected clinicasService: ClinicasService,
    protected enfermariasService: EnfermariasService,
    protected leitosService: LeitosService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ pacientes }) => {
      this.updateForm(pacientes);

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const pacientes = this.createFromForm();
    if (pacientes.id !== undefined) {
      this.subscribeToSaveResponse(this.pacientesService.update(pacientes));
    } else {
      this.subscribeToSaveResponse(this.pacientesService.create(pacientes));
    }
  }

  trackClinicasById(index: number, item: IClinicas): number {
    return item.id!;
  }

  trackEnfermariasById(index: number, item: IEnfermarias): number {
    return item.id!;
  }

  trackLeitosById(index: number, item: ILeitos): number {
    return item.id!;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IPacientes>>): void {
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

  protected updateForm(pacientes: IPacientes): void {
    this.editForm.patchValue({
      id: pacientes.id,
      prontuario: pacientes.prontuario,
      nome: pacientes.nome,
      cpf: pacientes.cpf,
      email: pacientes.email,
      cep: pacientes.cep,
      logradouro: pacientes.logradouro,
      numero: pacientes.numero,
      complemento: pacientes.complemento,
      bairro: pacientes.bairro,
      cidade: pacientes.cidade,
      uF: pacientes.uF,
      clinicas: pacientes.clinicas,
      enfermarias: pacientes.enfermarias,
      leitos: pacientes.leitos,
    });

    this.clinicasSharedCollection = this.clinicasService.addClinicasToCollectionIfMissing(
      this.clinicasSharedCollection,
      pacientes.clinicas
    );
    this.enfermariasSharedCollection = this.enfermariasService.addEnfermariasToCollectionIfMissing(
      this.enfermariasSharedCollection,
      pacientes.enfermarias
    );
    this.leitosSharedCollection = this.leitosService.addLeitosToCollectionIfMissing(this.leitosSharedCollection, pacientes.leitos);
  }

  protected loadRelationshipsOptions(): void {
    this.clinicasService
      .query()
      .pipe(map((res: HttpResponse<IClinicas[]>) => res.body ?? []))
      .pipe(
        map((clinicas: IClinicas[]) =>
          this.clinicasService.addClinicasToCollectionIfMissing(clinicas, this.editForm.get('clinicas')!.value)
        )
      )
      .subscribe((clinicas: IClinicas[]) => (this.clinicasSharedCollection = clinicas));

    this.enfermariasService
      .query()
      .pipe(map((res: HttpResponse<IEnfermarias[]>) => res.body ?? []))
      .pipe(
        map((enfermarias: IEnfermarias[]) =>
          this.enfermariasService.addEnfermariasToCollectionIfMissing(enfermarias, this.editForm.get('enfermarias')!.value)
        )
      )
      .subscribe((enfermarias: IEnfermarias[]) => (this.enfermariasSharedCollection = enfermarias));

    this.leitosService
      .query()
      .pipe(map((res: HttpResponse<ILeitos[]>) => res.body ?? []))
      .pipe(map((leitos: ILeitos[]) => this.leitosService.addLeitosToCollectionIfMissing(leitos, this.editForm.get('leitos')!.value)))
      .subscribe((leitos: ILeitos[]) => (this.leitosSharedCollection = leitos));
  }

  protected createFromForm(): IPacientes {
    return {
      ...new Pacientes(),
      id: this.editForm.get(['id'])!.value,
      prontuario: this.editForm.get(['prontuario'])!.value,
      nome: this.editForm.get(['nome'])!.value,
      cpf: this.editForm.get(['cpf'])!.value,
      email: this.editForm.get(['email'])!.value,
      cep: this.editForm.get(['cep'])!.value,
      logradouro: this.editForm.get(['logradouro'])!.value,
      numero: this.editForm.get(['numero'])!.value,
      complemento: this.editForm.get(['complemento'])!.value,
      bairro: this.editForm.get(['bairro'])!.value,
      cidade: this.editForm.get(['cidade'])!.value,
      uF: this.editForm.get(['uF'])!.value,
      clinicas: this.editForm.get(['clinicas'])!.value,
      enfermarias: this.editForm.get(['enfermarias'])!.value,
      leitos: this.editForm.get(['leitos'])!.value,
    };
  }
}
