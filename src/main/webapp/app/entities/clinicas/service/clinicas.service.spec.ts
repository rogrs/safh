import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { IClinicas, Clinicas } from '../clinicas.model';

import { ClinicasService } from './clinicas.service';

describe('Service Tests', () => {
  describe('Clinicas Service', () => {
    let service: ClinicasService;
    let httpMock: HttpTestingController;
    let elemDefault: IClinicas;
    let expectedResult: IClinicas | IClinicas[] | boolean | null;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
      });
      expectedResult = null;
      service = TestBed.inject(ClinicasService);
      httpMock = TestBed.inject(HttpTestingController);

      elemDefault = {
        id: 0,
        clinica: 'AAAAAAA',
        descricao: 'AAAAAAA',
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

      it('should create a Clinicas', () => {
        const returnedFromService = Object.assign(
          {
            id: 0,
          },
          elemDefault
        );

        const expected = Object.assign({}, returnedFromService);

        service.create(new Clinicas()).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'POST' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should update a Clinicas', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            clinica: 'BBBBBB',
            descricao: 'BBBBBB',
          },
          elemDefault
        );

        const expected = Object.assign({}, returnedFromService);

        service.update(expected).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'PUT' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should partial update a Clinicas', () => {
        const patchObject = Object.assign(
          {
            clinica: 'BBBBBB',
          },
          new Clinicas()
        );

        const returnedFromService = Object.assign(patchObject, elemDefault);

        const expected = Object.assign({}, returnedFromService);

        service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'PATCH' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should return a list of Clinicas', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            clinica: 'BBBBBB',
            descricao: 'BBBBBB',
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

      it('should delete a Clinicas', () => {
        service.delete(123).subscribe(resp => (expectedResult = resp.ok));

        const req = httpMock.expectOne({ method: 'DELETE' });
        req.flush({ status: 200 });
        expect(expectedResult);
      });

      describe('addClinicasToCollectionIfMissing', () => {
        it('should add a Clinicas to an empty array', () => {
          const clinicas: IClinicas = { id: 123 };
          expectedResult = service.addClinicasToCollectionIfMissing([], clinicas);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(clinicas);
        });

        it('should not add a Clinicas to an array that contains it', () => {
          const clinicas: IClinicas = { id: 123 };
          const clinicasCollection: IClinicas[] = [
            {
              ...clinicas,
            },
            { id: 456 },
          ];
          expectedResult = service.addClinicasToCollectionIfMissing(clinicasCollection, clinicas);
          expect(expectedResult).toHaveLength(2);
        });

        it("should add a Clinicas to an array that doesn't contain it", () => {
          const clinicas: IClinicas = { id: 123 };
          const clinicasCollection: IClinicas[] = [{ id: 456 }];
          expectedResult = service.addClinicasToCollectionIfMissing(clinicasCollection, clinicas);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(clinicas);
        });

        it('should add only unique Clinicas to an array', () => {
          const clinicasArray: IClinicas[] = [{ id: 123 }, { id: 456 }, { id: 12567 }];
          const clinicasCollection: IClinicas[] = [{ id: 123 }];
          expectedResult = service.addClinicasToCollectionIfMissing(clinicasCollection, ...clinicasArray);
          expect(expectedResult).toHaveLength(3);
        });

        it('should accept varargs', () => {
          const clinicas: IClinicas = { id: 123 };
          const clinicas2: IClinicas = { id: 456 };
          expectedResult = service.addClinicasToCollectionIfMissing([], clinicas, clinicas2);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(clinicas);
          expect(expectedResult).toContain(clinicas2);
        });

        it('should accept null and undefined values', () => {
          const clinicas: IClinicas = { id: 123 };
          expectedResult = service.addClinicasToCollectionIfMissing([], null, clinicas, undefined);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(clinicas);
        });
      });
    });

    afterEach(() => {
      httpMock.verify();
    });
  });
});
