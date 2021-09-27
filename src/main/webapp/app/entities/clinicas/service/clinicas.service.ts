import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IClinicas, getClinicasIdentifier } from '../clinicas.model';

export type EntityResponseType = HttpResponse<IClinicas>;
export type EntityArrayResponseType = HttpResponse<IClinicas[]>;

@Injectable({ providedIn: 'root' })
export class ClinicasService {
  public resourceUrl = this.applicationConfigService.getEndpointFor('api/clinicas');

  constructor(protected http: HttpClient, private applicationConfigService: ApplicationConfigService) {}

  create(clinicas: IClinicas): Observable<EntityResponseType> {
    return this.http.post<IClinicas>(this.resourceUrl, clinicas, { observe: 'response' });
  }

  update(clinicas: IClinicas): Observable<EntityResponseType> {
    return this.http.put<IClinicas>(`${this.resourceUrl}/${getClinicasIdentifier(clinicas) as number}`, clinicas, { observe: 'response' });
  }

  partialUpdate(clinicas: IClinicas): Observable<EntityResponseType> {
    return this.http.patch<IClinicas>(`${this.resourceUrl}/${getClinicasIdentifier(clinicas) as number}`, clinicas, {
      observe: 'response',
    });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IClinicas>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IClinicas[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addClinicasToCollectionIfMissing(clinicasCollection: IClinicas[], ...clinicasToCheck: (IClinicas | null | undefined)[]): IClinicas[] {
    const clinicas: IClinicas[] = clinicasToCheck.filter(isPresent);
    if (clinicas.length > 0) {
      const clinicasCollectionIdentifiers = clinicasCollection.map(clinicasItem => getClinicasIdentifier(clinicasItem)!);
      const clinicasToAdd = clinicas.filter(clinicasItem => {
        const clinicasIdentifier = getClinicasIdentifier(clinicasItem);
        if (clinicasIdentifier == null || clinicasCollectionIdentifiers.includes(clinicasIdentifier)) {
          return false;
        }
        clinicasCollectionIdentifiers.push(clinicasIdentifier);
        return true;
      });
      return [...clinicasToAdd, ...clinicasCollection];
    }
    return clinicasCollection;
  }
}
