import { TestBed } from '@angular/core/testing';

import { sampleWithRequiredData, sampleWithNewData } from '../order-queue.test-samples';

import { OrderQueueFormService } from './order-queue-form.service';

describe('OrderQueue Form Service', () => {
  let service: OrderQueueFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(OrderQueueFormService);
  });

  describe('Service methods', () => {
    describe('createOrderQueueFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createOrderQueueFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            order: expect.any(Object),
            queue: expect.any(Object),
            workerProfile: expect.any(Object),
          })
        );
      });

      it('passing IOrderQueue should create a new form with FormGroup', () => {
        const formGroup = service.createOrderQueueFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            order: expect.any(Object),
            queue: expect.any(Object),
            workerProfile: expect.any(Object),
          })
        );
      });
    });

    describe('getOrderQueue', () => {
      it('should return NewOrderQueue for default OrderQueue initial value', () => {
        // eslint-disable-next-line @typescript-eslint/no-unused-vars
        const formGroup = service.createOrderQueueFormGroup(sampleWithNewData);

        const orderQueue = service.getOrderQueue(formGroup) as any;

        expect(orderQueue).toMatchObject(sampleWithNewData);
      });

      it('should return NewOrderQueue for empty OrderQueue initial value', () => {
        const formGroup = service.createOrderQueueFormGroup();

        const orderQueue = service.getOrderQueue(formGroup) as any;

        expect(orderQueue).toMatchObject({});
      });

      it('should return IOrderQueue', () => {
        const formGroup = service.createOrderQueueFormGroup(sampleWithRequiredData);

        const orderQueue = service.getOrderQueue(formGroup) as any;

        expect(orderQueue).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IOrderQueue should not enable id FormControl', () => {
        const formGroup = service.createOrderQueueFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewOrderQueue should disable id FormControl', () => {
        const formGroup = service.createOrderQueueFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
