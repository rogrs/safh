jest.mock('@angular/router');

import { TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of } from 'rxjs';

import { IInternacoes, Internacoes } from '../internacoes.model';
import { InternacoesService } from '../service/internacoes.service';

import { InternacoesRoutingResolveService } from './internacoes-routing-resolve.service';

describe('Service Tests', () => {
  describe('Internacoes routing resolve service', () => {
    let mockRouter: Router;
    let mockActivatedRouteSnapshot: ActivatedRouteSnapshot;
    let routingResolveService: InternacoesRoutingResolveService;
    let service: InternacoesService;
    let resultInternacoes: IInternacoes | undefined;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        providers: [Router, ActivatedRouteSnapshot],
      });
      mockRouter = TestBed.inject(Router);
      mockActivatedRouteSnapshot = TestBed.inject(ActivatedRouteSnapshot);
      routingResolveService = TestBed.inject(InternacoesRoutingResolveService);
      service = TestBed.inject(InternacoesService);
      resultInternacoes = undefined;
    });

    describe('resolve', () => {
      it('should return IInternacoes returned by find', () => {
        // GIVEN
        service.find = jest.fn(id => of(new HttpResponse({ body: { id } })));
        mockActivatedRouteSnapshot.params = { id: 123 };

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultInternacoes = result;
        });

        // THEN
        expect(service.find).toBeCalledWith(123);
        expect(resultInternacoes).toEqual({ id: 123 });
      });

      it('should return new IInternacoes if id is not provided', () => {
        // GIVEN
        service.find = jest.fn();
        mockActivatedRouteSnapshot.params = {};

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultInternacoes = result;
        });

        // THEN
        expect(service.find).not.toBeCalled();
        expect(resultInternacoes).toEqual(new Internacoes());
      });

      it('should route to 404 page if data not found in server', () => {
        // GIVEN
        spyOn(service, 'find').and.returnValue(of(new HttpResponse({ body: null })));
        mockActivatedRouteSnapshot.params = { id: 123 };

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultInternacoes = result;
        });

        // THEN
        expect(service.find).toBeCalledWith(123);
        expect(resultInternacoes).toEqual(undefined);
        expect(mockRouter.navigate).toHaveBeenCalledWith(['404']);
      });
    });
  });
});
