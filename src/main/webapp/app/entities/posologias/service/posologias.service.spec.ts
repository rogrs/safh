import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { IPosologias, Posologias } from '../posologias.model';

import { PosologiasService } from './posologias.service';

describe('Service Tests', () => {
  describe('Posologias Service', () => {
    let service: PosologiasService;
    let httpMock: HttpTestingController;
    let elemDefault: IPosologias;
    let expectedResult: IPosologias | IPosologias[] | boolean | null;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
      });
      expectedResult = null;
      service = TestBed.inject(PosologiasService);
      httpMock = TestBed.inject(HttpTestingController);

      elemDefault = {
        id: 0,
        posologia: 'AAAAAAA',
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

      it('should create a Posologias', () => {
        const returnedFromService = Object.assign(
          {
            id: 0,
          },
          elemDefault
        );

        const expected = Object.assign({}, returnedFromService);

        service.create(new Posologias()).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'POST' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should update a Posologias', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            posologia: 'BBBBBB',
          },
          elemDefault
        );

        const expected = Object.assign({}, returnedFromService);

        service.update(expected).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'PUT' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should partial update a Posologias', () => {
        const patchObject = Object.assign({}, new Posologias());

        const returnedFromService = Object.assign(patchObject, elemDefault);

        const expected = Object.assign({}, returnedFromService);

        service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'PATCH' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should return a list of Posologias', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            posologia: 'BBBBBB',
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

      it('should delete a Posologias', () => {
        service.delete(123).subscribe(resp => (expectedResult = resp.ok));

        const req = httpMock.expectOne({ method: 'DELETE' });
        req.flush({ status: 200 });
        expect(expectedResult);
      });

      describe('addPosologiasToCollectionIfMissing', () => {
        it('should add a Posologias to an empty array', () => {
          const posologias: IPosologias = { id: 123 };
          expectedResult = service.addPosologiasToCollectionIfMissing([], posologias);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(posologias);
        });

        it('should not add a Posologias to an array that contains it', () => {
          const posologias: IPosologias = { id: 123 };
          const posologiasCollection: IPosologias[] = [
            {
              ...posologias,
            },
            { id: 456 },
          ];
          expectedResult = service.addPosologiasToCollectionIfMissing(posologiasCollection, posologias);
          expect(expectedResult).toHaveLength(2);
        });

        it("should add a Posologias to an array that doesn't contain it", () => {
          const posologias: IPosologias = { id: 123 };
          const posologiasCollection: IPosologias[] = [{ id: 456 }];
          expectedResult = service.addPosologiasToCollectionIfMissing(posologiasCollection, posologias);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(posologias);
        });

        it('should add only unique Posologias to an array', () => {
          const posologiasArray: IPosologias[] = [{ id: 123 }, { id: 456 }, { id: 19724 }];
          const posologiasCollection: IPosologias[] = [{ id: 123 }];
          expectedResult = service.addPosologiasToCollectionIfMissing(posologiasCollection, ...posologiasArray);
          expect(expectedResult).toHaveLength(3);
        });

        it('should accept varargs', () => {
          const posologias: IPosologias = { id: 123 };
          const posologias2: IPosologias = { id: 456 };
          expectedResult = service.addPosologiasToCollectionIfMissing([], posologias, posologias2);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(posologias);
          expect(expectedResult).toContain(posologias2);
        });

        it('should accept null and undefined values', () => {
          const posologias: IPosologias = { id: 123 };
          expectedResult = service.addPosologiasToCollectionIfMissing([], null, posologias, undefined);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(posologias);
        });
      });
    });

    afterEach(() => {
      httpMock.verify();
    });
  });
});
