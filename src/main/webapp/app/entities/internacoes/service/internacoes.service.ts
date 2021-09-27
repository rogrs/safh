import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import * as dayjs from 'dayjs';

import { isPresent } from 'app/core/util/operators';
import { DATE_FORMAT } from 'app/config/input.constants';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IInternacoes, getInternacoesIdentifier } from '../internacoes.model';

export type EntityResponseType = HttpResponse<IInternacoes>;
export type EntityArrayResponseType = HttpResponse<IInternacoes[]>;

@Injectable({ providedIn: 'root' })
export class InternacoesService {
  public resourceUrl = this.applicationConfigService.getEndpointFor('api/internacoes');

  constructor(protected http: HttpClient, private applicationConfigService: ApplicationConfigService) {}

  create(internacoes: IInternacoes): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(internacoes);
    return this.http
      .post<IInternacoes>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  update(internacoes: IInternacoes): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(internacoes);
    return this.http
      .put<IInternacoes>(`${this.resourceUrl}/${getInternacoesIdentifier(internacoes) as number}`, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  partialUpdate(internacoes: IInternacoes): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(internacoes);
    return this.http
      .patch<IInternacoes>(`${this.resourceUrl}/${getInternacoesIdentifier(internacoes) as number}`, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<IInternacoes>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<IInternacoes[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addInternacoesToCollectionIfMissing(
    internacoesCollection: IInternacoes[],
    ...internacoesToCheck: (IInternacoes | null | undefined)[]
  ): IInternacoes[] {
    const internacoes: IInternacoes[] = internacoesToCheck.filter(isPresent);
    if (internacoes.length > 0) {
      const internacoesCollectionIdentifiers = internacoesCollection.map(internacoesItem => getInternacoesIdentifier(internacoesItem)!);
      const internacoesToAdd = internacoes.filter(internacoesItem => {
        const internacoesIdentifier = getInternacoesIdentifier(internacoesItem);
        if (internacoesIdentifier == null || internacoesCollectionIdentifiers.includes(internacoesIdentifier)) {
          return false;
        }
        internacoesCollectionIdentifiers.push(internacoesIdentifier);
        return true;
      });
      return [...internacoesToAdd, ...internacoesCollection];
    }
    return internacoesCollection;
  }

  protected convertDateFromClient(internacoes: IInternacoes): IInternacoes {
    return Object.assign({}, internacoes, {
      dataInternacao: internacoes.dataInternacao?.isValid() ? internacoes.dataInternacao.format(DATE_FORMAT) : undefined,
    });
  }

  protected convertDateFromServer(res: EntityResponseType): EntityResponseType {
    if (res.body) {
      res.body.dataInternacao = res.body.dataInternacao ? dayjs(res.body.dataInternacao) : undefined;
    }
    return res;
  }

  protected convertDateArrayFromServer(res: EntityArrayResponseType): EntityArrayResponseType {
    if (res.body) {
      res.body.forEach((internacoes: IInternacoes) => {
        internacoes.dataInternacao = internacoes.dataInternacao ? dayjs(internacoes.dataInternacao) : undefined;
      });
    }
    return res;
  }
}
