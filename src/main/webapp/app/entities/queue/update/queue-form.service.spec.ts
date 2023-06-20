import { TestBed } from '@angular/core/testing';

import { sampleWithRequiredData, sampleWithNewData } from '../queue.test-samples';

import { QueueFormService } from './queue-form.service';

describe('Queue Form Service', () => {
  let service: QueueFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(QueueFormService);
  });

  describe('Service methods', () => {
    describe('createQueueFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createQueueFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            name: expect.any(Object),
            status: expect.any(Object),
            attencionChannel: expect.any(Object),
            company: expect.any(Object),
            branches: expect.any(Object),
          })
        );
      });

      it('passing IQueue should create a new form with FormGroup', () => {
        const formGroup = service.createQueueFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            name: expect.any(Object),
            status: expect.any(Object),
            attencionChannel: expect.any(Object),
            company: expect.any(Object),
            branches: expect.any(Object),
          })
        );
      });
    });

    describe('getQueue', () => {
      it('should return NewQueue for default Queue initial value', () => {
        // eslint-disable-next-line @typescript-eslint/no-unused-vars
        const formGroup = service.createQueueFormGroup(sampleWithNewData);

        const queue = service.getQueue(formGroup) as any;

        expect(queue).toMatchObject(sampleWithNewData);
      });

      it('should return NewQueue for empty Queue initial value', () => {
        const formGroup = service.createQueueFormGroup();

        const queue = service.getQueue(formGroup) as any;

        expect(queue).toMatchObject({});
      });

      it('should return IQueue', () => {
        const formGroup = service.createQueueFormGroup(sampleWithRequiredData);

        const queue = service.getQueue(formGroup) as any;

        expect(queue).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IQueue should not enable id FormControl', () => {
        const formGroup = service.createQueueFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewQueue should disable id FormControl', () => {
        const formGroup = service.createQueueFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
