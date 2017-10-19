/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, async } from '@angular/core/testing';
import { DatePipe } from '@angular/common';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs/Rx';
import { JhiDateUtils, JhiDataUtils, JhiEventManager } from 'ng-jhipster';
import { SafhTestModule } from '../../../test.module';
import { MockActivatedRoute } from '../../../helpers/mock-route.service';
import { EspecialidadesDetailComponent } from '../../../../../../main/webapp/app/entities/especialidades/especialidades-detail.component';
import { EspecialidadesService } from '../../../../../../main/webapp/app/entities/especialidades/especialidades.service';
import { Especialidades } from '../../../../../../main/webapp/app/entities/especialidades/especialidades.model';

describe('Component Tests', () => {

    describe('Especialidades Management Detail Component', () => {
        let comp: EspecialidadesDetailComponent;
        let fixture: ComponentFixture<EspecialidadesDetailComponent>;
        let service: EspecialidadesService;

        beforeEach(async(() => {
            TestBed.configureTestingModule({
                imports: [SafhTestModule],
                declarations: [EspecialidadesDetailComponent],
                providers: [
                    JhiDateUtils,
                    JhiDataUtils,
                    DatePipe,
                    {
                        provide: ActivatedRoute,
                        useValue: new MockActivatedRoute({id: 123})
                    },
                    EspecialidadesService,
                    JhiEventManager
                ]
            }).overrideTemplate(EspecialidadesDetailComponent, '')
            .compileComponents();
        }));

        beforeEach(() => {
            fixture = TestBed.createComponent(EspecialidadesDetailComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(EspecialidadesService);
        });

        describe('OnInit', () => {
            it('Should call load all on init', () => {
            // GIVEN

            spyOn(service, 'find').and.returnValue(Observable.of(new Especialidades(10)));

            // WHEN
            comp.ngOnInit();

            // THEN
            expect(service.find).toHaveBeenCalledWith(123);
            expect(comp.especialidades).toEqual(jasmine.objectContaining({id: 10}));
            });
        });
    });

});
