import { Injectable } from '@angular/core';
import { Http, Response } from '@angular/http';
import { Observable } from 'rxjs/Rx';
import { SERVER_API_URL } from '../../app.constants';

import { JhiDateUtils } from 'ng-jhipster';

import { Internacoes } from './internacoes.model';
import { ResponseWrapper, createRequestOption } from '../../shared';

@Injectable()
export class InternacoesService {

    private resourceUrl = SERVER_API_URL + 'api/internacoes';
    private resourceSearchUrl = SERVER_API_URL + 'api/_search/internacoes';

    constructor(private http: Http, private dateUtils: JhiDateUtils) { }

    create(internacoes: Internacoes): Observable<Internacoes> {
        const copy = this.convert(internacoes);
        return this.http.post(this.resourceUrl, copy).map((res: Response) => {
            const jsonResponse = res.json();
            return this.convertItemFromServer(jsonResponse);
        });
    }

    update(internacoes: Internacoes): Observable<Internacoes> {
        const copy = this.convert(internacoes);
        return this.http.put(this.resourceUrl, copy).map((res: Response) => {
            const jsonResponse = res.json();
            return this.convertItemFromServer(jsonResponse);
        });
    }

    find(id: number): Observable<Internacoes> {
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
     * Convert a returned JSON object to Internacoes.
     */
    private convertItemFromServer(json: any): Internacoes {
        const entity: Internacoes = Object.assign(new Internacoes(), json);
        entity.dataInternacao = this.dateUtils
            .convertLocalDateFromServer(json.dataInternacao);
        return entity;
    }

    /**
     * Convert a Internacoes to a JSON which can be sent to the server.
     */
    private convert(internacoes: Internacoes): Internacoes {
        const copy: Internacoes = Object.assign({}, internacoes);
        copy.dataInternacao = this.dateUtils
            .convertLocalDateToServer(internacoes.dataInternacao);
        return copy;
    }
}
