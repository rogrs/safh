import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import * as dayjs from 'dayjs';

import { isPresent } from 'app/core/util/operators';
import { DATE_FORMAT } from 'app/config/input.constants';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IInternacoesDetalhes, getInternacoesDetalhesIdentifier } from '../internacoes-detalhes.model';

export type EntityResponseType = HttpResponse<IInternacoesDetalhes>;
export type EntityArrayResponseType = HttpResponse<IInternacoesDetalhes[]>;

@Injectable({ providedIn: 'root' })
export class InternacoesDetalhesService {
  public resourceUrl = this.applicationConfigService.getEndpointFor('api/internacoes-detalhes');

  constructor(protected http: HttpClient, private applicationConfigService: ApplicationConfigService) {}

  create(internacoesDetalhes: IInternacoesDetalhes): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(internacoesDetalhes);
    return this.http
      .post<IInternacoesDetalhes>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  update(internacoesDetalhes: IInternacoesDetalhes): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(internacoesDetalhes);
    return this.http
      .put<IInternacoesDetalhes>(`${this.resourceUrl}/${getInternacoesDetalhesIdentifier(internacoesDetalhes) as number}`, copy, {
        observe: 'response',
      })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  partialUpdate(internacoesDetalhes: IInternacoesDetalhes): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(internacoesDetalhes);
    return this.http
      .patch<IInternacoesDetalhes>(`${this.resourceUrl}/${getInternacoesDetalhesIdentifier(internacoesDetalhes) as number}`, copy, {
        observe: 'response',
      })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<IInternacoesDetalhes>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<IInternacoesDetalhes[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addInternacoesDetalhesToCollectionIfMissing(
    internacoesDetalhesCollection: IInternacoesDetalhes[],
    ...internacoesDetalhesToCheck: (IInternacoesDetalhes | null | undefined)[]
  ): IInternacoesDetalhes[] {
    const internacoesDetalhes: IInternacoesDetalhes[] = internacoesDetalhesToCheck.filter(isPresent);
    if (internacoesDetalhes.length > 0) {
      const internacoesDetalhesCollectionIdentifiers = internacoesDetalhesCollection.map(
        internacoesDetalhesItem => getInternacoesDetalhesIdentifier(internacoesDetalhesItem)!
      );
      const internacoesDetalhesToAdd = internacoesDetalhes.filter(internacoesDetalhesItem => {
        const internacoesDetalhesIdentifier = getInternacoesDetalhesIdentifier(internacoesDetalhesItem);
        if (internacoesDetalhesIdentifier == null || internacoesDetalhesCollectionIdentifiers.includes(internacoesDetalhesIdentifier)) {
          return false;
        }
        internacoesDetalhesCollectionIdentifiers.push(internacoesDetalhesIdentifier);
        return true;
      });
      return [...internacoesDetalhesToAdd, ...internacoesDetalhesCollection];
    }
    return internacoesDetalhesCollection;
  }

  protected convertDateFromClient(internacoesDetalhes: IInternacoesDetalhes): IInternacoesDetalhes {
    return Object.assign({}, internacoesDetalhes, {
      dataDetalhe: internacoesDetalhes.dataDetalhe?.isValid() ? internacoesDetalhes.dataDetalhe.format(DATE_FORMAT) : undefined,
      horario: internacoesDetalhes.horario?.isValid() ? internacoesDetalhes.horario.format(DATE_FORMAT) : undefined,
    });
  }

  protected convertDateFromServer(res: EntityResponseType): EntityResponseType {
    if (res.body) {
      res.body.dataDetalhe = res.body.dataDetalhe ? dayjs(res.body.dataDetalhe) : undefined;
      res.body.horario = res.body.horario ? dayjs(res.body.horario) : undefined;
    }
    return res;
  }

  protected convertDateArrayFromServer(res: EntityArrayResponseType): EntityArrayResponseType {
    if (res.body) {
      res.body.forEach((internacoesDetalhes: IInternacoesDetalhes) => {
        internacoesDetalhes.dataDetalhe = internacoesDetalhes.dataDetalhe ? dayjs(internacoesDetalhes.dataDetalhe) : undefined;
        internacoesDetalhes.horario = internacoesDetalhes.horario ? dayjs(internacoesDetalhes.horario) : undefined;
      });
    }
    return res;
  }
}
