import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { IMedicamentos, Medicamentos } from '../medicamentos.model';

import { MedicamentosService } from './medicamentos.service';

describe('Service Tests', () => {
  describe('Medicamentos Service', () => {
    let service: MedicamentosService;
    let httpMock: HttpTestingController;
    let elemDefault: IMedicamentos;
    let expectedResult: IMedicamentos | IMedicamentos[] | boolean | null;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
      });
      expectedResult = null;
      service = TestBed.inject(MedicamentosService);
      httpMock = TestBed.inject(HttpTestingController);

      elemDefault = {
        id: 0,
        descricao: 'AAAAAAA',
        registroMinisterioSaude: 'AAAAAAA',
        codigoBarras: 'AAAAAAA',
        qtdAtual: 0,
        qtdMin: 0,
        qtdMax: 0,
        observacoes: 'AAAAAAA',
        apresentacao: 'AAAAAAA',
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

      it('should create a Medicamentos', () => {
        const returnedFromService = Object.assign(
          {
            id: 0,
          },
          elemDefault
        );

        const expected = Object.assign({}, returnedFromService);

        service.create(new Medicamentos()).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'POST' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should update a Medicamentos', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            descricao: 'BBBBBB',
            registroMinisterioSaude: 'BBBBBB',
            codigoBarras: 'BBBBBB',
            qtdAtual: 1,
            qtdMin: 1,
            qtdMax: 1,
            observacoes: 'BBBBBB',
            apresentacao: 'BBBBBB',
          },
          elemDefault
        );

        const expected = Object.assign({}, returnedFromService);

        service.update(expected).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'PUT' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should partial update a Medicamentos', () => {
        const patchObject = Object.assign(
          {
            descricao: 'BBBBBB',
            registroMinisterioSaude: 'BBBBBB',
            qtdAtual: 1,
            qtdMin: 1,
            qtdMax: 1,
            observacoes: 'BBBBBB',
          },
          new Medicamentos()
        );

        const returnedFromService = Object.assign(patchObject, elemDefault);

        const expected = Object.assign({}, returnedFromService);

        service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'PATCH' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should return a list of Medicamentos', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            descricao: 'BBBBBB',
            registroMinisterioSaude: 'BBBBBB',
            codigoBarras: 'BBBBBB',
            qtdAtual: 1,
            qtdMin: 1,
            qtdMax: 1,
            observacoes: 'BBBBBB',
            apresentacao: 'BBBBBB',
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

      it('should delete a Medicamentos', () => {
        service.delete(123).subscribe(resp => (expectedResult = resp.ok));

        const req = httpMock.expectOne({ method: 'DELETE' });
        req.flush({ status: 200 });
        expect(expectedResult);
      });

      describe('addMedicamentosToCollectionIfMissing', () => {
        it('should add a Medicamentos to an empty array', () => {
          const medicamentos: IMedicamentos = { id: 123 };
          expectedResult = service.addMedicamentosToCollectionIfMissing([], medicamentos);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(medicamentos);
        });

        it('should not add a Medicamentos to an array that contains it', () => {
          const medicamentos: IMedicamentos = { id: 123 };
          const medicamentosCollection: IMedicamentos[] = [
            {
              ...medicamentos,
            },
            { id: 456 },
          ];
          expectedResult = service.addMedicamentosToCollectionIfMissing(medicamentosCollection, medicamentos);
          expect(expectedResult).toHaveLength(2);
        });

        it("should add a Medicamentos to an array that doesn't contain it", () => {
          const medicamentos: IMedicamentos = { id: 123 };
          const medicamentosCollection: IMedicamentos[] = [{ id: 456 }];
          expectedResult = service.addMedicamentosToCollectionIfMissing(medicamentosCollection, medicamentos);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(medicamentos);
        });

        it('should add only unique Medicamentos to an array', () => {
          const medicamentosArray: IMedicamentos[] = [{ id: 123 }, { id: 456 }, { id: 77213 }];
          const medicamentosCollection: IMedicamentos[] = [{ id: 123 }];
          expectedResult = service.addMedicamentosToCollectionIfMissing(medicamentosCollection, ...medicamentosArray);
          expect(expectedResult).toHaveLength(3);
        });

        it('should accept varargs', () => {
          const medicamentos: IMedicamentos = { id: 123 };
          const medicamentos2: IMedicamentos = { id: 456 };
          expectedResult = service.addMedicamentosToCollectionIfMissing([], medicamentos, medicamentos2);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(medicamentos);
          expect(expectedResult).toContain(medicamentos2);
        });

        it('should accept null and undefined values', () => {
          const medicamentos: IMedicamentos = { id: 123 };
          expectedResult = service.addMedicamentosToCollectionIfMissing([], null, medicamentos, undefined);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(medicamentos);
        });
      });
    });

    afterEach(() => {
      httpMock.verify();
    });
  });
});
