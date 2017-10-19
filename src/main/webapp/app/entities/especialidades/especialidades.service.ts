import { Injectable } from '@angular/core';
import { Http, Response } from '@angular/http';
import { Observable } from 'rxjs/Rx';
import { SERVER_API_URL } from '../../app.constants';

import { Especialidades } from './especialidades.model';
import { ResponseWrapper, createRequestOption } from '../../shared';

@Injectable()
export class EspecialidadesService {

    private resourceUrl = SERVER_API_URL + 'api/especialidades';
    private resourceSearchUrl = SERVER_API_URL + 'api/_search/especialidades';

    constructor(private http: Http) { }

    create(especialidades: Especialidades): Observable<Especialidades> {
        const copy = this.convert(especialidades);
        return this.http.post(this.resourceUrl, copy).map((res: Response) => {
            const jsonResponse = res.json();
            return this.convertItemFromServer(jsonResponse);
        });
    }

    update(especialidades: Especialidades): Observable<Especialidades> {
        const copy = this.convert(especialidades);
        return this.http.put(this.resourceUrl, copy).map((res: Response) => {
            const jsonResponse = res.json();
            return this.convertItemFromServer(jsonResponse);
        });
    }

    find(id: number): Observable<Especialidades> {
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
     * Convert a returned JSON object to Especialidades.
     */
    private convertItemFromServer(json: any): Especialidades {
        const entity: Especialidades = Object.assign(new Especialidades(), json);
        return entity;
    }

    /**
     * Convert a Especialidades to a JSON which can be sent to the server.
     */
    private convert(especialidades: Especialidades): Especialidades {
        const copy: Especialidades = Object.assign({}, especialidades);
        return copy;
    }
}
