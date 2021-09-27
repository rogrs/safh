import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { IEspecialidades, Especialidades } from '../especialidades.model';

import { EspecialidadesService } from './especialidades.service';

describe('Service Tests', () => {
  describe('Especialidades Service', () => {
    let service: EspecialidadesService;
    let httpMock: HttpTestingController;
    let elemDefault: IEspecialidades;
    let expectedResult: IEspecialidades | IEspecialidades[] | boolean | null;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
      });
      expectedResult = null;
      service = TestBed.inject(EspecialidadesService);
      httpMock = TestBed.inject(HttpTestingController);

      elemDefault = {
        id: 0,
        especialidade: 'AAAAAAA',
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

      it('should create a Especialidades', () => {
        const returnedFromService = Object.assign(
          {
            id: 0,
          },
          elemDefault
        );

        const expected = Object.assign({}, returnedFromService);

        service.create(new Especialidades()).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'POST' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should update a Especialidades', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            especialidade: 'BBBBBB',
          },
          elemDefault
        );

        const expected = Object.assign({}, returnedFromService);

        service.update(expected).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'PUT' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should partial update a Especialidades', () => {
        const patchObject = Object.assign({}, new Especialidades());

        const returnedFromService = Object.assign(patchObject, elemDefault);

        const expected = Object.assign({}, returnedFromService);

        service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'PATCH' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should return a list of Especialidades', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            especialidade: 'BBBBBB',
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

      it('should delete a Especialidades', () => {
        service.delete(123).subscribe(resp => (expectedResult = resp.ok));

        const req = httpMock.expectOne({ method: 'DELETE' });
        req.flush({ status: 200 });
        expect(expectedResult);
      });

      describe('addEspecialidadesToCollectionIfMissing', () => {
        it('should add a Especialidades to an empty array', () => {
          const especialidades: IEspecialidades = { id: 123 };
          expectedResult = service.addEspecialidadesToCollectionIfMissing([], especialidades);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(especialidades);
        });

        it('should not add a Especialidades to an array that contains it', () => {
          const especialidades: IEspecialidades = { id: 123 };
          const especialidadesCollection: IEspecialidades[] = [
            {
              ...especialidades,
            },
            { id: 456 },
          ];
          expectedResult = service.addEspecialidadesToCollectionIfMissing(especialidadesCollection, especialidades);
          expect(expectedResult).toHaveLength(2);
        });

        it("should add a Especialidades to an array that doesn't contain it", () => {
          const especialidades: IEspecialidades = { id: 123 };
          const especialidadesCollection: IEspecialidades[] = [{ id: 456 }];
          expectedResult = service.addEspecialidadesToCollectionIfMissing(especialidadesCollection, especialidades);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(especialidades);
        });

        it('should add only unique Especialidades to an array', () => {
          const especialidadesArray: IEspecialidades[] = [{ id: 123 }, { id: 456 }, { id: 153 }];
          const especialidadesCollection: IEspecialidades[] = [{ id: 123 }];
          expectedResult = service.addEspecialidadesToCollectionIfMissing(especialidadesCollection, ...especialidadesArray);
          expect(expectedResult).toHaveLength(3);
        });

        it('should accept varargs', () => {
          const especialidades: IEspecialidades = { id: 123 };
          const especialidades2: IEspecialidades = { id: 456 };
          expectedResult = service.addEspecialidadesToCollectionIfMissing([], especialidades, especialidades2);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(especialidades);
          expect(expectedResult).toContain(especialidades2);
        });

        it('should accept null and undefined values', () => {
          const especialidades: IEspecialidades = { id: 123 };
          expectedResult = service.addEspecialidadesToCollectionIfMissing([], null, especialidades, undefined);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(especialidades);
        });
      });
    });

    afterEach(() => {
      httpMock.verify();
    });
  });
});
