import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IFabricantes, getFabricantesIdentifier } from '../fabricantes.model';

export type EntityResponseType = HttpResponse<IFabricantes>;
export type EntityArrayResponseType = HttpResponse<IFabricantes[]>;

@Injectable({ providedIn: 'root' })
export class FabricantesService {
  public resourceUrl = this.applicationConfigService.getEndpointFor('api/fabricantes');

  constructor(protected http: HttpClient, private applicationConfigService: ApplicationConfigService) {}

  create(fabricantes: IFabricantes): Observable<EntityResponseType> {
    return this.http.post<IFabricantes>(this.resourceUrl, fabricantes, { observe: 'response' });
  }

  update(fabricantes: IFabricantes): Observable<EntityResponseType> {
    return this.http.put<IFabricantes>(`${this.resourceUrl}/${getFabricantesIdentifier(fabricantes) as number}`, fabricantes, {
      observe: 'response',
    });
  }

  partialUpdate(fabricantes: IFabricantes): Observable<EntityResponseType> {
    return this.http.patch<IFabricantes>(`${this.resourceUrl}/${getFabricantesIdentifier(fabricantes) as number}`, fabricantes, {
      observe: 'response',
    });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IFabricantes>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IFabricantes[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addFabricantesToCollectionIfMissing(
    fabricantesCollection: IFabricantes[],
    ...fabricantesToCheck: (IFabricantes | null | undefined)[]
  ): IFabricantes[] {
    const fabricantes: IFabricantes[] = fabricantesToCheck.filter(isPresent);
    if (fabricantes.length > 0) {
      const fabricantesCollectionIdentifiers = fabricantesCollection.map(fabricantesItem => getFabricantesIdentifier(fabricantesItem)!);
      const fabricantesToAdd = fabricantes.filter(fabricantesItem => {
        const fabricantesIdentifier = getFabricantesIdentifier(fabricantesItem);
        if (fabricantesIdentifier == null || fabricantesCollectionIdentifiers.includes(fabricantesIdentifier)) {
          return false;
        }
        fabricantesCollectionIdentifiers.push(fabricantesIdentifier);
        return true;
      });
      return [...fabricantesToAdd, ...fabricantesCollection];
    }
    return fabricantesCollection;
  }
}
