jest.mock('@angular/router');

import { TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of } from 'rxjs';

import { IFabricantes, Fabricantes } from '../fabricantes.model';
import { FabricantesService } from '../service/fabricantes.service';

import { FabricantesRoutingResolveService } from './fabricantes-routing-resolve.service';

describe('Service Tests', () => {
  describe('Fabricantes routing resolve service', () => {
    let mockRouter: Router;
    let mockActivatedRouteSnapshot: ActivatedRouteSnapshot;
    let routingResolveService: FabricantesRoutingResolveService;
    let service: FabricantesService;
    let resultFabricantes: IFabricantes | undefined;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        providers: [Router, ActivatedRouteSnapshot],
      });
      mockRouter = TestBed.inject(Router);
      mockActivatedRouteSnapshot = TestBed.inject(ActivatedRouteSnapshot);
      routingResolveService = TestBed.inject(FabricantesRoutingResolveService);
      service = TestBed.inject(FabricantesService);
      resultFabricantes = undefined;
    });

    describe('resolve', () => {
      it('should return IFabricantes returned by find', () => {
        // GIVEN
        service.find = jest.fn(id => of(new HttpResponse({ body: { id } })));
        mockActivatedRouteSnapshot.params = { id: 123 };

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultFabricantes = result;
        });

        // THEN
        expect(service.find).toBeCalledWith(123);
        expect(resultFabricantes).toEqual({ id: 123 });
      });

      it('should return new IFabricantes if id is not provided', () => {
        // GIVEN
        service.find = jest.fn();
        mockActivatedRouteSnapshot.params = {};

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultFabricantes = result;
        });

        // THEN
        expect(service.find).not.toBeCalled();
        expect(resultFabricantes).toEqual(new Fabricantes());
      });

      it('should route to 404 page if data not found in server', () => {
        // GIVEN
        spyOn(service, 'find').and.returnValue(of(new HttpResponse({ body: null })));
        mockActivatedRouteSnapshot.params = { id: 123 };

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultFabricantes = result;
        });

        // THEN
        expect(service.find).toBeCalledWith(123);
        expect(resultFabricantes).toEqual(undefined);
        expect(mockRouter.navigate).toHaveBeenCalledWith(['404']);
      });
    });
  });
});
