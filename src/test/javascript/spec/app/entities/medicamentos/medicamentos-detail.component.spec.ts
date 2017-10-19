/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, async } from '@angular/core/testing';
import { DatePipe } from '@angular/common';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs/Rx';
import { JhiDateUtils, JhiDataUtils, JhiEventManager } from 'ng-jhipster';
import { SafhTestModule } from '../../../test.module';
import { MockActivatedRoute } from '../../../helpers/mock-route.service';
import { MedicamentosDetailComponent } from '../../../../../../main/webapp/app/entities/medicamentos/medicamentos-detail.component';
import { MedicamentosService } from '../../../../../../main/webapp/app/entities/medicamentos/medicamentos.service';
import { Medicamentos } from '../../../../../../main/webapp/app/entities/medicamentos/medicamentos.model';

describe('Component Tests', () => {

    describe('Medicamentos Management Detail Component', () => {
        let comp: MedicamentosDetailComponent;
        let fixture: ComponentFixture<MedicamentosDetailComponent>;
        let service: MedicamentosService;

        beforeEach(async(() => {
            TestBed.configureTestingModule({
                imports: [SafhTestModule],
                declarations: [MedicamentosDetailComponent],
                providers: [
                    JhiDateUtils,
                    JhiDataUtils,
                    DatePipe,
                    {
                        provide: ActivatedRoute,
                        useValue: new MockActivatedRoute({id: 123})
                    },
                    MedicamentosService,
                    JhiEventManager
                ]
            }).overrideTemplate(MedicamentosDetailComponent, '')
            .compileComponents();
        }));

        beforeEach(() => {
            fixture = TestBed.createComponent(MedicamentosDetailComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(MedicamentosService);
        });

        describe('OnInit', () => {
            it('Should call load all on init', () => {
            // GIVEN

            spyOn(service, 'find').and.returnValue(Observable.of(new Medicamentos(10)));

            // WHEN
            comp.ngOnInit();

            // THEN
            expect(service.find).toHaveBeenCalledWith(123);
            expect(comp.medicamentos).toEqual(jasmine.objectContaining({id: 10}));
            });
        });
    });

});
