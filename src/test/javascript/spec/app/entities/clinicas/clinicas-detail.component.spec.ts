/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, async } from '@angular/core/testing';
import { DatePipe } from '@angular/common';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs/Rx';
import { JhiDateUtils, JhiDataUtils, JhiEventManager } from 'ng-jhipster';
import { SafhTestModule } from '../../../test.module';
import { MockActivatedRoute } from '../../../helpers/mock-route.service';
import { ClinicasDetailComponent } from '../../../../../../main/webapp/app/entities/clinicas/clinicas-detail.component';
import { ClinicasService } from '../../../../../../main/webapp/app/entities/clinicas/clinicas.service';
import { Clinicas } from '../../../../../../main/webapp/app/entities/clinicas/clinicas.model';

describe('Component Tests', () => {

    describe('Clinicas Management Detail Component', () => {
        let comp: ClinicasDetailComponent;
        let fixture: ComponentFixture<ClinicasDetailComponent>;
        let service: ClinicasService;

        beforeEach(async(() => {
            TestBed.configureTestingModule({
                imports: [SafhTestModule],
                declarations: [ClinicasDetailComponent],
                providers: [
                    JhiDateUtils,
                    JhiDataUtils,
                    DatePipe,
                    {
                        provide: ActivatedRoute,
                        useValue: new MockActivatedRoute({id: 123})
                    },
                    ClinicasService,
                    JhiEventManager
                ]
            }).overrideTemplate(ClinicasDetailComponent, '')
            .compileComponents();
        }));

        beforeEach(() => {
            fixture = TestBed.createComponent(ClinicasDetailComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(ClinicasService);
        });

        describe('OnInit', () => {
            it('Should call load all on init', () => {
            // GIVEN

            spyOn(service, 'find').and.returnValue(Observable.of(new Clinicas(10)));

            // WHEN
            comp.ngOnInit();

            // THEN
            expect(service.find).toHaveBeenCalledWith(123);
            expect(comp.clinicas).toEqual(jasmine.objectContaining({id: 10}));
            });
        });
    });

});
