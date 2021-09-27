import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { IFabricantes, Fabricantes } from '../fabricantes.model';

import { FabricantesService } from './fabricantes.service';

describe('Service Tests', () => {
  describe('Fabricantes Service', () => {
    let service: FabricantesService;
    let httpMock: HttpTestingController;
    let elemDefault: IFabricantes;
    let expectedResult: IFabricantes | IFabricantes[] | boolean | null;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
      });
      expectedResult = null;
      service = TestBed.inject(FabricantesService);
      httpMock = TestBed.inject(HttpTestingController);

      elemDefault = {
        id: 0,
        fabricante: 'AAAAAAA',
      };
    });

    describe('Service methods', () => {
      it('should find an element', () => {
        const returnedFromService = Object.assign({}, elemDefault);

        service.find(123).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'GET' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(elemDefault);
      });

      it('should create a Fabricantes', () => {
        const returnedFromService = Object.assign(
          {
            id: 0,
          },
          elemDefault
        );

        const expected = Object.assign({}, returnedFromService);

        service.create(new Fabricantes()).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'POST' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should update a Fabricantes', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            fabricante: 'BBBBBB',
          },
          elemDefault
        );

        const expected = Object.assign({}, returnedFromService);

        service.update(expected).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'PUT' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should partial update a Fabricantes', () => {
        const patchObject = Object.assign({}, new Fabricantes());

        const returnedFromService = Object.assign(patchObject, elemDefault);

        const expected = Object.assign({}, returnedFromService);

        service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'PATCH' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should return a list of Fabricantes', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            fabricante: 'BBBBBB',
          },
          elemDefault
        );

        const expected = Object.assign({}, returnedFromService);

        service.query().subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'GET' });
        req.flush([returnedFromService]);
        httpMock.verify();
        expect(expectedResult).toContainEqual(expected);
      });

      it('should delete a Fabricantes', () => {
        service.delete(123).subscribe(resp => (expectedResult = resp.ok));

        const req = httpMock.expectOne({ method: 'DELETE' });
        req.flush({ status: 200 });
        expect(expectedResult);
      });

      describe('addFabricantesToCollectionIfMissing', () => {
        it('should add a Fabricantes to an empty array', () => {
          const fabricantes: IFabricantes = { id: 123 };
          expectedResult = service.addFabricantesToCollectionIfMissing([], fabricantes);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(fabricantes);
        });

        it('should not add a Fabricantes to an array that contains it', () => {
          const fabricantes: IFabricantes = { id: 123 };
          const fabricantesCollection: IFabricantes[] = [
            {
              ...fabricantes,
            },
            { id: 456 },
          ];
          expectedResult = service.addFabricantesToCollectionIfMissing(fabricantesCollection, fabricantes);
          expect(expectedResult).toHaveLength(2);
        });

        it("should add a Fabricantes to an array that doesn't contain it", () => {
          const fabricantes: IFabricantes = { id: 123 };
          const fabricantesCollection: IFabricantes[] = [{ id: 456 }];
          expectedResult = service.addFabricantesToCollectionIfMissing(fabricantesCollection, fabricantes);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(fabricantes);
        });

        it('should add only unique Fabricantes to an array', () => {
          const fabricantesArray: IFabricantes[] = [{ id: 123 }, { id: 456 }, { id: 96484 }];
          const fabricantesCollection: IFabricantes[] = [{ id: 123 }];
          expectedResult = service.addFabricantesToCollectionIfMissing(fabricantesCollection, ...fabricantesArray);
          expect(expectedResult).toHaveLength(3);
        });

        it('should accept varargs', () => {
          const fabricantes: IFabricantes = { id: 123 };
          const fabricantes2: IFabricantes = { id: 456 };
          expectedResult = service.addFabricantesToCollectionIfMissing([], fabricantes, fabricantes2);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(fabricantes);
          expect(expectedResult).toContain(fabricantes2);
        });

        it('should accept null and undefined values', () => {
          const fabricantes: IFabricantes = { id: 123 };
          expectedResult = service.addFabricantesToCollectionIfMissing([], null, fabricantes, undefined);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(fabricantes);
        });
      });
    });

    afterEach(() => {
      httpMock.verify();
    });
  });
});
