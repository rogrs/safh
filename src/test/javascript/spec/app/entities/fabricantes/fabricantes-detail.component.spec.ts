/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, async } from '@angular/core/testing';
import { DatePipe } from '@angular/common';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs/Rx';
import { JhiDateUtils, JhiDataUtils, JhiEventManager } from 'ng-jhipster';
import { SafhTestModule } from '../../../test.module';
import { MockActivatedRoute } from '../../../helpers/mock-route.service';
import { FabricantesDetailComponent } from '../../../../../../main/webapp/app/entities/fabricantes/fabricantes-detail.component';
import { FabricantesService } from '../../../../../../main/webapp/app/entities/fabricantes/fabricantes.service';
import { Fabricantes } from '../../../../../../main/webapp/app/entities/fabricantes/fabricantes.model';

describe('Component Tests', () => {

    describe('Fabricantes Management Detail Component', () => {
        let comp: FabricantesDetailComponent;
        let fixture: ComponentFixture<FabricantesDetailComponent>;
        let service: FabricantesService;

        beforeEach(async(() => {
            TestBed.configureTestingModule({
                imports: [SafhTestModule],
                declarations: [FabricantesDetailComponent],
                providers: [
                    JhiDateUtils,
                    JhiDataUtils,
                    DatePipe,
                    {
                        provide: ActivatedRoute,
                        useValue: new MockActivatedRoute({id: 123})
                    },
                    FabricantesService,
                    JhiEventManager
                ]
            }).overrideTemplate(FabricantesDetailComponent, '')
            .compileComponents();
        }));

        beforeEach(() => {
            fixture = TestBed.createComponent(FabricantesDetailComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(FabricantesService);
        });

        describe('OnInit', () => {
            it('Should call load all on init', () => {
            // GIVEN

            spyOn(service, 'find').and.returnValue(Observable.of(new Fabricantes(10)));

            // WHEN
            comp.ngOnInit();

            // THEN
            expect(service.find).toHaveBeenCalledWith(123);
            expect(comp.fabricantes).toEqual(jasmine.objectContaining({id: 10}));
            });
        });
    });

});
