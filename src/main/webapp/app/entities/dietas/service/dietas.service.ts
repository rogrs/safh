import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IDietas, getDietasIdentifier } from '../dietas.model';

export type EntityResponseType = HttpResponse<IDietas>;
export type EntityArrayResponseType = HttpResponse<IDietas[]>;

@Injectable({ providedIn: 'root' })
export class DietasService {
  public resourceUrl = this.applicationConfigService.getEndpointFor('api/dietas');

  constructor(protected http: HttpClient, private applicationConfigService: ApplicationConfigService) {}

  create(dietas: IDietas): Observable<EntityResponseType> {
    return this.http.post<IDietas>(this.resourceUrl, dietas, { observe: 'response' });
  }

  update(dietas: IDietas): Observable<EntityResponseType> {
    return this.http.put<IDietas>(`${this.resourceUrl}/${getDietasIdentifier(dietas) as number}`, dietas, { observe: 'response' });
  }

  partialUpdate(dietas: IDietas): Observable<EntityResponseType> {
    return this.http.patch<IDietas>(`${this.resourceUrl}/${getDietasIdentifier(dietas) as number}`, dietas, { observe: 'response' });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IDietas>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IDietas[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addDietasToCollectionIfMissing(dietasCollection: IDietas[], ...dietasToCheck: (IDietas | null | undefined)[]): IDietas[] {
    const dietas: IDietas[] = dietasToCheck.filter(isPresent);
    if (dietas.length > 0) {
      const dietasCollectionIdentifiers = dietasCollection.map(dietasItem => getDietasIdentifier(dietasItem)!);
      const dietasToAdd = dietas.filter(dietasItem => {
        const dietasIdentifier = getDietasIdentifier(dietasItem);
        if (dietasIdentifier == null || dietasCollectionIdentifiers.includes(dietasIdentifier)) {
          return false;
        }
        dietasCollectionIdentifiers.push(dietasIdentifier);
        return true;
      });
      return [...dietasToAdd, ...dietasCollection];
    }
    return dietasCollection;
  }
}
