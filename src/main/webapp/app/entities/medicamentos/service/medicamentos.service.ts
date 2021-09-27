import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IMedicamentos, getMedicamentosIdentifier } from '../medicamentos.model';

export type EntityResponseType = HttpResponse<IMedicamentos>;
export type EntityArrayResponseType = HttpResponse<IMedicamentos[]>;

@Injectable({ providedIn: 'root' })
export class MedicamentosService {
  public resourceUrl = this.applicationConfigService.getEndpointFor('api/medicamentos');

  constructor(protected http: HttpClient, private applicationConfigService: ApplicationConfigService) {}

  create(medicamentos: IMedicamentos): Observable<EntityResponseType> {
    return this.http.post<IMedicamentos>(this.resourceUrl, medicamentos, { observe: 'response' });
  }

  update(medicamentos: IMedicamentos): Observable<EntityResponseType> {
    return this.http.put<IMedicamentos>(`${this.resourceUrl}/${getMedicamentosIdentifier(medicamentos) as number}`, medicamentos, {
      observe: 'response',
    });
  }

  partialUpdate(medicamentos: IMedicamentos): Observable<EntityResponseType> {
    return this.http.patch<IMedicamentos>(`${this.resourceUrl}/${getMedicamentosIdentifier(medicamentos) as number}`, medicamentos, {
      observe: 'response',
    });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IMedicamentos>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IMedicamentos[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addMedicamentosToCollectionIfMissing(
    medicamentosCollection: IMedicamentos[],
    ...medicamentosToCheck: (IMedicamentos | null | undefined)[]
  ): IMedicamentos[] {
    const medicamentos: IMedicamentos[] = medicamentosToCheck.filter(isPresent);
    if (medicamentos.length > 0) {
      const medicamentosCollectionIdentifiers = medicamentosCollection.map(
        medicamentosItem => getMedicamentosIdentifier(medicamentosItem)!
      );
      const medicamentosToAdd = medicamentos.filter(medicamentosItem => {
        const medicamentosIdentifier = getMedicamentosIdentifier(medicamentosItem);
        if (medicamentosIdentifier == null || medicamentosCollectionIdentifiers.includes(medicamentosIdentifier)) {
          return false;
        }
        medicamentosCollectionIdentifiers.push(medicamentosIdentifier);
        return true;
      });
      return [...medicamentosToAdd, ...medicamentosCollection];
    }
    return medicamentosCollection;
  }
}
