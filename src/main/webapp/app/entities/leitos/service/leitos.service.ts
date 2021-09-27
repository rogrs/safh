import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { ILeitos, getLeitosIdentifier } from '../leitos.model';

export type EntityResponseType = HttpResponse<ILeitos>;
export type EntityArrayResponseType = HttpResponse<ILeitos[]>;

@Injectable({ providedIn: 'root' })
export class LeitosService {
  public resourceUrl = this.applicationConfigService.getEndpointFor('api/leitos');

  constructor(protected http: HttpClient, private applicationConfigService: ApplicationConfigService) {}

  create(leitos: ILeitos): Observable<EntityResponseType> {
    return this.http.post<ILeitos>(this.resourceUrl, leitos, { observe: 'response' });
  }

  update(leitos: ILeitos): Observable<EntityResponseType> {
    return this.http.put<ILeitos>(`${this.resourceUrl}/${getLeitosIdentifier(leitos) as number}`, leitos, { observe: 'response' });
  }

  partialUpdate(leitos: ILeitos): Observable<EntityResponseType> {
    return this.http.patch<ILeitos>(`${this.resourceUrl}/${getLeitosIdentifier(leitos) as number}`, leitos, { observe: 'response' });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<ILeitos>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<ILeitos[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addLeitosToCollectionIfMissing(leitosCollection: ILeitos[], ...leitosToCheck: (ILeitos | null | undefined)[]): ILeitos[] {
    const leitos: ILeitos[] = leitosToCheck.filter(isPresent);
    if (leitos.length > 0) {
      const leitosCollectionIdentifiers = leitosCollection.map(leitosItem => getLeitosIdentifier(leitosItem)!);
      const leitosToAdd = leitos.filter(leitosItem => {
        const leitosIdentifier = getLeitosIdentifier(leitosItem);
        if (leitosIdentifier == null || leitosCollectionIdentifiers.includes(leitosIdentifier)) {
          return false;
        }
        leitosCollectionIdentifiers.push(leitosIdentifier);
        return true;
      });
      return [...leitosToAdd, ...leitosCollection];
    }
    return leitosCollection;
  }
}
