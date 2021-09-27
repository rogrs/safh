jest.mock('@angular/router');

import { TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of } from 'rxjs';

import { IPosologias, Posologias } from '../posologias.model';
import { PosologiasService } from '../service/posologias.service';

import { PosologiasRoutingResolveService } from './posologias-routing-resolve.service';

describe('Service Tests', () => {
  describe('Posologias routing resolve service', () => {
    let mockRouter: Router;
    let mockActivatedRouteSnapshot: ActivatedRouteSnapshot;
    let routingResolveService: PosologiasRoutingResolveService;
    let service: PosologiasService;
    let resultPosologias: IPosologias | undefined;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        providers: [Router, ActivatedRouteSnapshot],
      });
      mockRouter = TestBed.inject(Router);
      mockActivatedRouteSnapshot = TestBed.inject(ActivatedRouteSnapshot);
      routingResolveService = TestBed.inject(PosologiasRoutingResolveService);
      service = TestBed.inject(PosologiasService);
      resultPosologias = undefined;
    });

    describe('resolve', () => {
      it('should return IPosologias returned by find', () => {
        // GIVEN
        service.find = jest.fn(id => of(new HttpResponse({ body: { id } })));
        mockActivatedRouteSnapshot.params = { id: 123 };

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultPosologias = result;
        });

        // THEN
        expect(service.find).toBeCalledWith(123);
        expect(resultPosologias).toEqual({ id: 123 });
      });

      it('should return new IPosologias if id is not provided', () => {
        // GIVEN
        service.find = jest.fn();
        mockActivatedRouteSnapshot.params = {};

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultPosologias = result;
        });

        // THEN
        expect(service.find).not.toBeCalled();
        expect(resultPosologias).toEqual(new Posologias());
      });

      it('should route to 404 page if data not found in server', () => {
        // GIVEN
        spyOn(service, 'find').and.returnValue(of(new HttpResponse({ body: null })));
        mockActivatedRouteSnapshot.params = { id: 123 };

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultPosologias = result;
        });

        // THEN
        expect(service.find).toBeCalledWith(123);
        expect(resultPosologias).toEqual(undefined);
        expect(mockRouter.navigate).toHaveBeenCalledWith(['404']);
      });
    });
  });
});
