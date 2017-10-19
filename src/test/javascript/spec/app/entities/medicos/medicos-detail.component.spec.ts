/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, async } from '@angular/core/testing';
import { DatePipe } from '@angular/common';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs/Rx';
import { JhiDateUtils, JhiDataUtils, JhiEventManager } from 'ng-jhipster';
import { SafhTestModule } from '../../../test.module';
import { MockActivatedRoute } from '../../../helpers/mock-route.service';
import { MedicosDetailComponent } from '../../../../../../main/webapp/app/entities/medicos/medicos-detail.component';
import { MedicosService } from '../../../../../../main/webapp/app/entities/medicos/medicos.service';
import { Medicos } from '../../../../../../main/webapp/app/entities/medicos/medicos.model';

describe('Component Tests', () => {

    describe('Medicos Management Detail Component', () => {
        let comp: MedicosDetailComponent;
        let fixture: ComponentFixture<MedicosDetailComponent>;
        let service: MedicosService;

        beforeEach(async(() => {
            TestBed.configureTestingModule({
                imports: [SafhTestModule],
                declarations: [MedicosDetailComponent],
                providers: [
                    JhiDateUtils,
                    JhiDataUtils,
                    DatePipe,
                    {
                        provide: ActivatedRoute,
                        useValue: new MockActivatedRoute({id: 123})
                    },
                    MedicosService,
                    JhiEventManager
                ]
            }).overrideTemplate(MedicosDetailComponent, '')
            .compileComponents();
        }));

        beforeEach(() => {
            fixture = TestBed.createComponent(MedicosDetailComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(MedicosService);
        });

        describe('OnInit', () => {
            it('Should call load all on init', () => {
            // GIVEN

            spyOn(service, 'find').and.returnValue(Observable.of(new Medicos(10)));

            // WHEN
            comp.ngOnInit();

            // THEN
            expect(service.find).toHaveBeenCalledWith(123);
            expect(comp.medicos).toEqual(jasmine.objectContaining({id: 10}));
            });
        });
    });

});
