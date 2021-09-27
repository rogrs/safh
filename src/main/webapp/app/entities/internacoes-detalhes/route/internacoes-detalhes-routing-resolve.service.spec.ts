jest.mock('@angular/router');

import { TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of } from 'rxjs';

import { IInternacoesDetalhes, InternacoesDetalhes } from '../internacoes-detalhes.model';
import { InternacoesDetalhesService } from '../service/internacoes-detalhes.service';

import { InternacoesDetalhesRoutingResolveService } from './internacoes-detalhes-routing-resolve.service';

describe('Service Tests', () => {
  describe('InternacoesDetalhes routing resolve service', () => {
    let mockRouter: Router;
    let mockActivatedRouteSnapshot: ActivatedRouteSnapshot;
    let routingResolveService: InternacoesDetalhesRoutingResolveService;
    let service: InternacoesDetalhesService;
    let resultInternacoesDetalhes: IInternacoesDetalhes | undefined;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        providers: [Router, ActivatedRouteSnapshot],
      });
      mockRouter = TestBed.inject(Router);
      mockActivatedRouteSnapshot = TestBed.inject(ActivatedRouteSnapshot);
      routingResolveService = TestBed.inject(InternacoesDetalhesRoutingResolveService);
      service = TestBed.inject(InternacoesDetalhesService);
      resultInternacoesDetalhes = undefined;
    });

    describe('resolve', () => {
      it('should return IInternacoesDetalhes returned by find', () => {
        // GIVEN
        service.find = jest.fn(id => of(new HttpResponse({ body: { id } })));
        mockActivatedRouteSnapshot.params = { id: 123 };

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultInternacoesDetalhes = result;
        });

        // THEN
        expect(service.find).toBeCalledWith(123);
        expect(resultInternacoesDetalhes).toEqual({ id: 123 });
      });

      it('should return new IInternacoesDetalhes if id is not provided', () => {
        // GIVEN
        service.find = jest.fn();
        mockActivatedRouteSnapshot.params = {};

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultInternacoesDetalhes = result;
        });

        // THEN
        expect(service.find).not.toBeCalled();
        expect(resultInternacoesDetalhes).toEqual(new InternacoesDetalhes());
      });

      it('should route to 404 page if data not found in server', () => {
        // GIVEN
        spyOn(service, 'find').and.returnValue(of(new HttpResponse({ body: null })));
        mockActivatedRouteSnapshot.params = { id: 123 };

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultInternacoesDetalhes = result;
        });

        // THEN
        expect(service.find).toBeCalledWith(123);
        expect(resultInternacoesDetalhes).toEqual(undefined);
        expect(mockRouter.navigate).toHaveBeenCalledWith(['404']);
      });
    });
  });
});
