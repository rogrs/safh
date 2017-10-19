import { Injectable } from '@angular/core';
import { Http, Response } from '@angular/http';
import { Observable } from 'rxjs/Rx';
import { SERVER_API_URL } from '../../app.constants';

import { Clinicas } from './clinicas.model';
import { ResponseWrapper, createRequestOption } from '../../shared';

@Injectable()
export class ClinicasService {

    private resourceUrl = SERVER_API_URL + 'api/clinicas';
    private resourceSearchUrl = SERVER_API_URL + 'api/_search/clinicas';

    constructor(private http: Http) { }

    create(clinicas: Clinicas): Observable<Clinicas> {
        const copy = this.convert(clinicas);
        return this.http.post(this.resourceUrl, copy).map((res: Response) => {
            const jsonResponse = res.json();
            return this.convertItemFromServer(jsonResponse);
        });
    }

    update(clinicas: Clinicas): Observable<Clinicas> {
        const copy = this.convert(clinicas);
        return this.http.put(this.resourceUrl, copy).map((res: Response) => {
            const jsonResponse = res.json();
            return this.convertItemFromServer(jsonResponse);
        });
    }

    find(id: number): Observable<Clinicas> {
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
     * Convert a returned JSON object to Clinicas.
     */
    private convertItemFromServer(json: any): Clinicas {
        const entity: Clinicas = Object.assign(new Clinicas(), json);
        return entity;
    }

    /**
     * Convert a Clinicas to a JSON which can be sent to the server.
     */
    private convert(clinicas: Clinicas): Clinicas {
        const copy: Clinicas = Object.assign({}, clinicas);
        return copy;
    }
}
