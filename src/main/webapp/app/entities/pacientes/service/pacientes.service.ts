import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IPacientes, getPacientesIdentifier } from '../pacientes.model';

export type EntityResponseType = HttpResponse<IPacientes>;
export type EntityArrayResponseType = HttpResponse<IPacientes[]>;

@Injectable({ providedIn: 'root' })
export class PacientesService {
  public resourceUrl = this.applicationConfigService.getEndpointFor('api/pacientes');

  constructor(protected http: HttpClient, private applicationConfigService: ApplicationConfigService) {}

  create(pacientes: IPacientes): Observable<EntityResponseType> {
    return this.http.post<IPacientes>(this.resourceUrl, pacientes, { observe: 'response' });
  }

  update(pacientes: IPacientes): Observable<EntityResponseType> {
    return this.http.put<IPacientes>(`${this.resourceUrl}/${getPacientesIdentifier(pacientes) as number}`, pacientes, {
      observe: 'response',
    });
  }

  partialUpdate(pacientes: IPacientes): Observable<EntityResponseType> {
    return this.http.patch<IPacientes>(`${this.resourceUrl}/${getPacientesIdentifier(pacientes) as number}`, pacientes, {
      observe: 'response',
    });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IPacientes>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IPacientes[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addPacientesToCollectionIfMissing(
    pacientesCollection: IPacientes[],
    ...pacientesToCheck: (IPacientes | null | undefined)[]
  ): IPacientes[] {
    const pacientes: IPacientes[] = pacientesToCheck.filter(isPresent);
    if (pacientes.length > 0) {
      const pacientesCollectionIdentifiers = pacientesCollection.map(pacientesItem => getPacientesIdentifier(pacientesItem)!);
      const pacientesToAdd = pacientes.filter(pacientesItem => {
        const pacientesIdentifier = getPacientesIdentifier(pacientesItem);
        if (pacientesIdentifier == null || pacientesCollectionIdentifiers.includes(pacientesIdentifier)) {
          return false;
        }
        pacientesCollectionIdentifiers.push(pacientesIdentifier);
        return true;
      });
      return [...pacientesToAdd, ...pacientesCollection];
    }
    return pacientesCollection;
  }
}
