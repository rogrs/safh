/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, async } from '@angular/core/testing';
import { DatePipe } from '@angular/common';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs/Rx';
import { JhiDateUtils, JhiDataUtils, JhiEventManager } from 'ng-jhipster';
import { SafhTestModule } from '../../../test.module';
import { MockActivatedRoute } from '../../../helpers/mock-route.service';
import { PacientesDetailComponent } from '../../../../../../main/webapp/app/entities/pacientes/pacientes-detail.component';
import { PacientesService } from '../../../../../../main/webapp/app/entities/pacientes/pacientes.service';
import { Pacientes } from '../../../../../../main/webapp/app/entities/pacientes/pacientes.model';

describe('Component Tests', () => {

    describe('Pacientes Management Detail Component', () => {
        let comp: PacientesDetailComponent;
        let fixture: ComponentFixture<PacientesDetailComponent>;
        let service: PacientesService;

        beforeEach(async(() => {
            TestBed.configureTestingModule({
                imports: [SafhTestModule],
                declarations: [PacientesDetailComponent],
                providers: [
                    JhiDateUtils,
                    JhiDataUtils,
                    DatePipe,
                    {
                        provide: ActivatedRoute,
                        useValue: new MockActivatedRoute({id: 123})
                    },
                    PacientesService,
                    JhiEventManager
                ]
            }).overrideTemplate(PacientesDetailComponent, '')
            .compileComponents();
        }));

        beforeEach(() => {
            fixture = TestBed.createComponent(PacientesDetailComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(PacientesService);
        });

        describe('OnInit', () => {
            it('Should call load all on init', () => {
            // GIVEN

            spyOn(service, 'find').and.returnValue(Observable.of(new Pacientes(10)));

            // WHEN
            comp.ngOnInit();

            // THEN
            expect(service.find).toHaveBeenCalledWith(123);
            expect(comp.pacientes).toEqual(jasmine.objectContaining({id: 10}));
            });
        });
    });

});
