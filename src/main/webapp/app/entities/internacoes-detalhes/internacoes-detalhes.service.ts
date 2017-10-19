import { Injectable } from '@angular/core';
import { Http, Response } from '@angular/http';
import { Observable } from 'rxjs/Rx';
import { SERVER_API_URL } from '../../app.constants';

import { JhiDateUtils } from 'ng-jhipster';

import { InternacoesDetalhes } from './internacoes-detalhes.model';
import { ResponseWrapper, createRequestOption } from '../../shared';

@Injectable()
export class InternacoesDetalhesService {

    private resourceUrl = SERVER_API_URL + 'api/internacoes-detalhes';
    private resourceSearchUrl = SERVER_API_URL + 'api/_search/internacoes-detalhes';

    constructor(private http: Http, private dateUtils: JhiDateUtils) { }

    create(internacoesDetalhes: InternacoesDetalhes): Observable<InternacoesDetalhes> {
        const copy = this.convert(internacoesDetalhes);
        return this.http.post(this.resourceUrl, copy).map((res: Response) => {
            const jsonResponse = res.json();
            return this.convertItemFromServer(jsonResponse);
        });
    }

    update(internacoesDetalhes: InternacoesDetalhes): Observable<InternacoesDetalhes> {
        const copy = this.convert(internacoesDetalhes);
        return this.http.put(this.resourceUrl, copy).map((res: Response) => {
            const jsonResponse = res.json();
            return this.convertItemFromServer(jsonResponse);
        });
    }

    find(id: number): Observable<InternacoesDetalhes> {
        return this.http.get(`${this.resourceUrl}/${id}`).map((res: Response) => {
            const jsonResponse = res.json();
            return this.convertItemFromServer(jsonResponse);
        });
    }

    query(req?: any): Observable<ResponseWrapper> {
        const options = createRequestOption(req);
        return this.http.get(this.resourceUrl, options)
            .map((res: Response) => this.convertResponse(res));
    }

    delete(id: number): Observable<Response> {
        return this.http.delete(`${this.resourceUrl}/${id}`);
    }

    search(req?: any): Observable<ResponseWrapper> {
        const options = createRequestOption(req);
        return this.http.get(this.resourceSearchUrl, options)
            .map((res: any) => this.convertResponse(res));
    }

    private convertResponse(res: Response): ResponseWrapper {
        const jsonResponse = res.json();
        const result = [];
        for (let i = 0; i < jsonResponse.length; i++) {
            result.push(this.convertItemFromServer(jsonResponse[i]));
        }
        return new ResponseWrapper(res.headers, result, res.status);
    }

    /**
     * Convert a returned JSON object to InternacoesDetalhes.
     */
    private convertItemFromServer(json: any): InternacoesDetalhes {
        const entity: InternacoesDetalhes = Object.assign(new InternacoesDetalhes(), json);
        entity.dataDetalhe = this.dateUtils
            .convertLocalDateFromServer(json.dataDetalhe);
        entity.horario = this.dateUtils
            .convertLocalDateFromServer(json.horario);
        return entity;
    }

    /**
     * Convert a InternacoesDetalhes to a JSON which can be sent to the server.
     */
    private convert(internacoesDetalhes: InternacoesDetalhes): InternacoesDetalhes {
        const copy: InternacoesDetalhes = Object.assign({}, internacoesDetalhes);
        copy.dataDetalhe = this.dateUtils
            .convertLocalDateToServer(internacoesDetalhes.dataDetalhe);
        copy.horario = this.dateUtils
            .convertLocalDateToServer(internacoesDetalhes.horario);
        return copy;
    }
}
