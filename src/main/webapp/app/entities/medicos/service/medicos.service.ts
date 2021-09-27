import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IMedicos, getMedicosIdentifier } from '../medicos.model';

export type EntityResponseType = HttpResponse<IMedicos>;
export type EntityArrayResponseType = HttpResponse<IMedicos[]>;

@Injectable({ providedIn: 'root' })
export class MedicosService {
  public resourceUrl = this.applicationConfigService.getEndpointFor('api/medicos');

  constructor(protected http: HttpClient, private applicationConfigService: ApplicationConfigService) {}

  create(medicos: IMedicos): Observable<EntityResponseType> {
    return this.http.post<IMedicos>(this.resourceUrl, medicos, { observe: 'response' });
  }

  update(medicos: IMedicos): Observable<EntityResponseType> {
    return this.http.put<IMedicos>(`${this.resourceUrl}/${getMedicosIdentifier(medicos) as number}`, medicos, { observe: 'response' });
  }

  partialUpdate(medicos: IMedicos): Observable<EntityResponseType> {
    return this.http.patch<IMedicos>(`${this.resourceUrl}/${getMedicosIdentifier(medicos) as number}`, medicos, { observe: 'response' });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IMedicos>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IMedicos[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addMedicosToCollectionIfMissing(medicosCollection: IMedicos[], ...medicosToCheck: (IMedicos | null | undefined)[]): IMedicos[] {
    const medicos: IMedicos[] = medicosToCheck.filter(isPresent);
    if (medicos.length > 0) {
      const medicosCollectionIdentifiers = medicosCollection.map(medicosItem => getMedicosIdentifier(medicosItem)!);
      const medicosToAdd = medicos.filter(medicosItem => {
        const medicosIdentifier = getMedicosIdentifier(medicosItem);
        if (medicosIdentifier == null || medicosCollectionIdentifiers.includes(medicosIdentifier)) {
          return false;
        }
        medicosCollectionIdentifiers.push(medicosIdentifier);
        return true;
      });
      return [...medicosToAdd, ...medicosCollection];
    }
    return medicosCollection;
  }
}
