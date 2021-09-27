jest.mock('@angular/router');

import { TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of } from 'rxjs';

import { IEnfermarias, Enfermarias } from '../enfermarias.model';
import { EnfermariasService } from '../service/enfermarias.service';

import { EnfermariasRoutingResolveService } from './enfermarias-routing-resolve.service';

describe('Service Tests', () => {
  describe('Enfermarias routing resolve service', () => {
    let mockRouter: Router;
    let mockActivatedRouteSnapshot: ActivatedRouteSnapshot;
    let routingResolveService: EnfermariasRoutingResolveService;
    let service: EnfermariasService;
    let resultEnfermarias: IEnfermarias | undefined;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        providers: [Router, ActivatedRouteSnapshot],
      });
      mockRouter = TestBed.inject(Router);
      mockActivatedRouteSnapshot = TestBed.inject(ActivatedRouteSnapshot);
      routingResolveService = TestBed.inject(EnfermariasRoutingResolveService);
      service = TestBed.inject(EnfermariasService);
      resultEnfermarias = undefined;
    });

    describe('resolve', () => {
      it('should return IEnfermarias returned by find', () => {
        // GIVEN
        service.find = jest.fn(id => of(new HttpResponse({ body: { id } })));
        mockActivatedRouteSnapshot.params = { id: 123 };

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultEnfermarias = result;
        });

        // THEN
        expect(service.find).toBeCalledWith(123);
        expect(resultEnfermarias).toEqual({ id: 123 });
      });

      it('should return new IEnfermarias if id is not provided', () => {
        // GIVEN
        service.find = jest.fn();
        mockActivatedRouteSnapshot.params = {};

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultEnfermarias = result;
        });

        // THEN
        expect(service.find).not.toBeCalled();
        expect(resultEnfermarias).toEqual(new Enfermarias());
      });

      it('should route to 404 page if data not found in server', () => {
        // GIVEN
        spyOn(service, 'find').and.returnValue(of(new HttpResponse({ body: null })));
        mockActivatedRouteSnapshot.params = { id: 123 };

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultEnfermarias = result;
        });

        // THEN
        expect(service.find).toBeCalledWith(123);
        expect(resultEnfermarias).toEqual(undefined);
        expect(mockRouter.navigate).toHaveBeenCalledWith(['404']);
      });
    });
  });
});
