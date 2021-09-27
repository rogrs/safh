import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IEnfermarias, getEnfermariasIdentifier } from '../enfermarias.model';

export type EntityResponseType = HttpResponse<IEnfermarias>;
export type EntityArrayResponseType = HttpResponse<IEnfermarias[]>;

@Injectable({ providedIn: 'root' })
export class EnfermariasService {
  public resourceUrl = this.applicationConfigService.getEndpointFor('api/enfermarias');

  constructor(protected http: HttpClient, private applicationConfigService: ApplicationConfigService) {}

  create(enfermarias: IEnfermarias): Observable<EntityResponseType> {
    return this.http.post<IEnfermarias>(this.resourceUrl, enfermarias, { observe: 'response' });
  }

  update(enfermarias: IEnfermarias): Observable<EntityResponseType> {
    return this.http.put<IEnfermarias>(`${this.resourceUrl}/${getEnfermariasIdentifier(enfermarias) as number}`, enfermarias, {
      observe: 'response',
    });
  }

  partialUpdate(enfermarias: IEnfermarias): Observable<EntityResponseType> {
    return this.http.patch<IEnfermarias>(`${this.resourceUrl}/${getEnfermariasIdentifier(enfermarias) as number}`, enfermarias, {
      observe: 'response',
    });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IEnfermarias>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IEnfermarias[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addEnfermariasToCollectionIfMissing(
    enfermariasCollection: IEnfermarias[],
    ...enfermariasToCheck: (IEnfermarias | null | undefined)[]
  ): IEnfermarias[] {
    const enfermarias: IEnfermarias[] = enfermariasToCheck.filter(isPresent);
    if (enfermarias.length > 0) {
      const enfermariasCollectionIdentifiers = enfermariasCollection.map(enfermariasItem => getEnfermariasIdentifier(enfermariasItem)!);
      const enfermariasToAdd = enfermarias.filter(enfermariasItem => {
        const enfermariasIdentifier = getEnfermariasIdentifier(enfermariasItem);
        if (enfermariasIdentifier == null || enfermariasCollectionIdentifiers.includes(enfermariasIdentifier)) {
          return false;
        }
        enfermariasCollectionIdentifiers.push(enfermariasIdentifier);
        return true;
      });
      return [...enfermariasToAdd, ...enfermariasCollection];
    }
    return enfermariasCollection;
  }
}
