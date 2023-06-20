import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { OrderQueueFormService } from './order-queue-form.service';
import { OrderQueueService } from '../service/order-queue.service';
import { IOrderQueue } from '../order-queue.model';
import { IQueue } from 'app/entities/queue/queue.model';
import { QueueService } from 'app/entities/queue/service/queue.service';
import { IWorkerProfile } from 'app/entities/worker-profile/worker-profile.model';
import { WorkerProfileService } from 'app/entities/worker-profile/service/worker-profile.service';

import { OrderQueueUpdateComponent } from './order-queue-update.component';

describe('OrderQueue Management Update Component', () => {
  let comp: OrderQueueUpdateComponent;
  let fixture: ComponentFixture<OrderQueueUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let orderQueueFormService: OrderQueueFormService;
  let orderQueueService: OrderQueueService;
  let queueService: QueueService;
  let workerProfileService: WorkerProfileService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([]), OrderQueueUpdateComponent],
      providers: [
        FormBuilder,
        {
          provide: ActivatedRoute,
          useValue: {
            params: from([{}]),
          },
        },
      ],
    })
      .overrideTemplate(OrderQueueUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(OrderQueueUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    orderQueueFormService = TestBed.inject(OrderQueueFormService);
    orderQueueService = TestBed.inject(OrderQueueService);
    queueService = TestBed.inject(QueueService);
    workerProfileService = TestBed.inject(WorkerProfileService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call Queue query and add missing value', () => {
      const orderQueue: IOrderQueue = { id: 456 };
      const queue: IQueue = { id: 55311 };
      orderQueue.queue = queue;

      const queueCollection: IQueue[] = [{ id: 62846 }];
      jest.spyOn(queueService, 'query').mockReturnValue(of(new HttpResponse({ body: queueCollection })));
      const additionalQueues = [queue];
      const expectedCollection: IQueue[] = [...additionalQueues, ...queueCollection];
      jest.spyOn(queueService, 'addQueueToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ orderQueue });
      comp.ngOnInit();

      expect(queueService.query).toHaveBeenCalled();
      expect(queueService.addQueueToCollectionIfMissing).toHaveBeenCalledWith(
        queueCollection,
        ...additionalQueues.map(expect.objectContaining)
      );
      expect(comp.queuesSharedCollection).toEqual(expectedCollection);
    });

    it('Should call WorkerProfile query and add missing value', () => {
      const orderQueue: IOrderQueue = { id: 456 };
      const workerProfile: IWorkerProfile = { id: 81102 };
      orderQueue.workerProfile = workerProfile;

      const workerProfileCollection: IWorkerProfile[] = [{ id: 51245 }];
      jest.spyOn(workerProfileService, 'query').mockReturnValue(of(new HttpResponse({ body: workerProfileCollection })));
      const additionalWorkerProfiles = [workerProfile];
      const expectedCollection: IWorkerProfile[] = [...additionalWorkerProfiles, ...workerProfileCollection];
      jest.spyOn(workerProfileService, 'addWorkerProfileToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ orderQueue });
      comp.ngOnInit();

      expect(workerProfileService.query).toHaveBeenCalled();
      expect(workerProfileService.addWorkerProfileToCollectionIfMissing).toHaveBeenCalledWith(
        workerProfileCollection,
        ...additionalWorkerProfiles.map(expect.objectContaining)
      );
      expect(comp.workerProfilesSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const orderQueue: IOrderQueue = { id: 456 };
      const queue: IQueue = { id: 59525 };
      orderQueue.queue = queue;
      const workerProfile: IWorkerProfile = { id: 34939 };
      orderQueue.workerProfile = workerProfile;

      activatedRoute.data = of({ orderQueue });
      comp.ngOnInit();

      expect(comp.queuesSharedCollection).toContain(queue);
      expect(comp.workerProfilesSharedCollection).toContain(workerProfile);
      expect(comp.orderQueue).toEqual(orderQueue);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IOrderQueue>>();
      const orderQueue = { id: 123 };
      jest.spyOn(orderQueueFormService, 'getOrderQueue').mockReturnValue(orderQueue);
      jest.spyOn(orderQueueService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ orderQueue });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: orderQueue }));
      saveSubject.complete();

      // THEN
      expect(orderQueueFormService.getOrderQueue).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(orderQueueService.update).toHaveBeenCalledWith(expect.objectContaining(orderQueue));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IOrderQueue>>();
      const orderQueue = { id: 123 };
      jest.spyOn(orderQueueFormService, 'getOrderQueue').mockReturnValue({ id: null });
      jest.spyOn(orderQueueService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ orderQueue: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: orderQueue }));
      saveSubject.complete();

      // THEN
      expect(orderQueueFormService.getOrderQueue).toHaveBeenCalled();
      expect(orderQueueService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IOrderQueue>>();
      const orderQueue = { id: 123 };
      jest.spyOn(orderQueueService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ orderQueue });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(orderQueueService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Compare relationships', () => {
    describe('compareQueue', () => {
      it('Should forward to queueService', () => {
        const entity = { id: 123 };
        const entity2 = { id: 456 };
        jest.spyOn(queueService, 'compareQueue');
        comp.compareQueue(entity, entity2);
        expect(queueService.compareQueue).toHaveBeenCalledWith(entity, entity2);
      });
    });

    describe('compareWorkerProfile', () => {
      it('Should forward to workerProfileService', () => {
        const entity = { id: 123 };
        const entity2 = { id: 456 };
        jest.spyOn(workerProfileService, 'compareWorkerProfile');
        comp.compareWorkerProfile(entity, entity2);
        expect(workerProfileService.compareWorkerProfile).toHaveBeenCalledWith(entity, entity2);
      });
    });
  });
});
