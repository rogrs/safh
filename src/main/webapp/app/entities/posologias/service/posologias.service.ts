import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IPosologias, getPosologiasIdentifier } from '../posologias.model';

export type EntityResponseType = HttpResponse<IPosologias>;
export type EntityArrayResponseType = HttpResponse<IPosologias[]>;

@Injectable({ providedIn: 'root' })
export class PosologiasService {
  public resourceUrl = this.applicationConfigService.getEndpointFor('api/posologias');

  constructor(protected http: HttpClient, private applicationConfigService: ApplicationConfigService) {}

  create(posologias: IPosologias): Observable<EntityResponseType> {
    return this.http.post<IPosologias>(this.resourceUrl, posologias, { observe: 'response' });
  }

  update(posologias: IPosologias): Observable<EntityResponseType> {
    return this.http.put<IPosologias>(`${this.resourceUrl}/${getPosologiasIdentifier(posologias) as number}`, posologias, {
      observe: 'response',
    });
  }

  partialUpdate(posologias: IPosologias): Observable<EntityResponseType> {
    return this.http.patch<IPosologias>(`${this.resourceUrl}/${getPosologiasIdentifier(posologias) as number}`, posologias, {
      observe: 'response',
    });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IPosologias>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IPosologias[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addPosologiasToCollectionIfMissing(
    posologiasCollection: IPosologias[],
    ...posologiasToCheck: (IPosologias | null | undefined)[]
  ): IPosologias[] {
    const posologias: IPosologias[] = posologiasToCheck.filter(isPresent);
    if (posologias.length > 0) {
      const posologiasCollectionIdentifiers = posologiasCollection.map(posologiasItem => getPosologiasIdentifier(posologiasItem)!);
      const posologiasToAdd = posologias.filter(posologiasItem => {
        const posologiasIdentifier = getPosologiasIdentifier(posologiasItem);
        if (posologiasIdentifier == null || posologiasCollectionIdentifiers.includes(posologiasIdentifier)) {
          return false;
        }
        posologiasCollectionIdentifiers.push(posologiasIdentifier);
        return true;
      });
      return [...posologiasToAdd, ...posologiasCollection];
    }
    return posologiasCollection;
  }
}
