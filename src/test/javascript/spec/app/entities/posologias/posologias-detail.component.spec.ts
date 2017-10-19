/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, async } from '@angular/core/testing';
import { DatePipe } from '@angular/common';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs/Rx';
import { JhiDateUtils, JhiDataUtils, JhiEventManager } from 'ng-jhipster';
import { SafhTestModule } from '../../../test.module';
import { MockActivatedRoute } from '../../../helpers/mock-route.service';
import { PosologiasDetailComponent } from '../../../../../../main/webapp/app/entities/posologias/posologias-detail.component';
import { PosologiasService } from '../../../../../../main/webapp/app/entities/posologias/posologias.service';
import { Posologias } from '../../../../../../main/webapp/app/entities/posologias/posologias.model';

describe('Component Tests', () => {

    describe('Posologias Management Detail Component', () => {
        let comp: PosologiasDetailComponent;
        let fixture: ComponentFixture<PosologiasDetailComponent>;
        let service: PosologiasService;

        beforeEach(async(() => {
            TestBed.configureTestingModule({
                imports: [SafhTestModule],
                declarations: [PosologiasDetailComponent],
                providers: [
                    JhiDateUtils,
                    JhiDataUtils,
                    DatePipe,
                    {
                        provide: ActivatedRoute,
                        useValue: new MockActivatedRoute({id: 123})
                    },
                    PosologiasService,
                    JhiEventManager
                ]
            }).overrideTemplate(PosologiasDetailComponent, '')
            .compileComponents();
        }));

        beforeEach(() => {
            fixture = TestBed.createComponent(PosologiasDetailComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(PosologiasService);
        });

        describe('OnInit', () => {
            it('Should call load all on init', () => {
            // GIVEN

            spyOn(service, 'find').and.returnValue(Observable.of(new Posologias(10)));

            // WHEN
            comp.ngOnInit();

            // THEN
            expect(service.find).toHaveBeenCalledWith(123);
            expect(comp.posologias).toEqual(jasmine.objectContaining({id: 10}));
            });
        });
    });

});
