import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IPrescricoes, getPrescricoesIdentifier } from '../prescricoes.model';

export type EntityResponseType = HttpResponse<IPrescricoes>;
export type EntityArrayResponseType = HttpResponse<IPrescricoes[]>;

@Injectable({ providedIn: 'root' })
export class PrescricoesService {
  public resourceUrl = this.applicationConfigService.getEndpointFor('api/prescricoes');

  constructor(protected http: HttpClient, private applicationConfigService: ApplicationConfigService) {}

  create(prescricoes: IPrescricoes): Observable<EntityResponseType> {
    return this.http.post<IPrescricoes>(this.resourceUrl, prescricoes, { observe: 'response' });
  }

  update(prescricoes: IPrescricoes): Observable<EntityResponseType> {
    return this.http.put<IPrescricoes>(`${this.resourceUrl}/${getPrescricoesIdentifier(prescricoes) as number}`, prescricoes, {
      observe: 'response',
    });
  }

  partialUpdate(prescricoes: IPrescricoes): Observable<EntityResponseType> {
    return this.http.patch<IPrescricoes>(`${this.resourceUrl}/${getPrescricoesIdentifier(prescricoes) as number}`, prescricoes, {
      observe: 'response',
    });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IPrescricoes>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IPrescricoes[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addPrescricoesToCollectionIfMissing(
    prescricoesCollection: IPrescricoes[],
    ...prescricoesToCheck: (IPrescricoes | null | undefined)[]
  ): IPrescricoes[] {
    const prescricoes: IPrescricoes[] = prescricoesToCheck.filter(isPresent);
    if (prescricoes.length > 0) {
      const prescricoesCollectionIdentifiers = prescricoesCollection.map(prescricoesItem => getPrescricoesIdentifier(prescricoesItem)!);
      const prescricoesToAdd = prescricoes.filter(prescricoesItem => {
        const prescricoesIdentifier = getPrescricoesIdentifier(prescricoesItem);
        if (prescricoesIdentifier == null || prescricoesCollectionIdentifiers.includes(prescricoesIdentifier)) {
          return false;
        }
        prescricoesCollectionIdentifiers.push(prescricoesIdentifier);
        return true;
      });
      return [...prescricoesToAdd, ...prescricoesCollection];
    }
    return prescricoesCollection;
  }
}
