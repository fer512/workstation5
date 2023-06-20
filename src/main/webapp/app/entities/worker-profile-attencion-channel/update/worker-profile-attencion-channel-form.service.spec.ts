import { TestBed } from '@angular/core/testing';

import { sampleWithRequiredData, sampleWithNewData } from '../worker-profile-attencion-channel.test-samples';

import { WorkerProfileAttencionChannelFormService } from './worker-profile-attencion-channel-form.service';

describe('WorkerProfileAttencionChannel Form Service', () => {
  let service: WorkerProfileAttencionChannelFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(WorkerProfileAttencionChannelFormService);
  });

  describe('Service methods', () => {
    describe('createWorkerProfileAttencionChannelFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createWorkerProfileAttencionChannelFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            name: expect.any(Object),
            type: expect.any(Object),
          })
        );
      });

      it('passing IWorkerProfileAttencionChannel should create a new form with FormGroup', () => {
        const formGroup = service.createWorkerProfileAttencionChannelFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            name: expect.any(Object),
            type: expect.any(Object),
          })
        );
      });
    });

    describe('getWorkerProfileAttencionChannel', () => {
      it('should return NewWorkerProfileAttencionChannel for default WorkerProfileAttencionChannel initial value', () => {
        // eslint-disable-next-line @typescript-eslint/no-unused-vars
        const formGroup = service.createWorkerProfileAttencionChannelFormGroup(sampleWithNewData);

        const workerProfileAttencionChannel = service.getWorkerProfileAttencionChannel(formGroup) as any;

        expect(workerProfileAttencionChannel).toMatchObject(sampleWithNewData);
      });

      it('should return NewWorkerProfileAttencionChannel for empty WorkerProfileAttencionChannel initial value', () => {
        const formGroup = service.createWorkerProfileAttencionChannelFormGroup();

        const workerProfileAttencionChannel = service.getWorkerProfileAttencionChannel(formGroup) as any;

        expect(workerProfileAttencionChannel).toMatchObject({});
      });

      it('should return IWorkerProfileAttencionChannel', () => {
        const formGroup = service.createWorkerProfileAttencionChannelFormGroup(sampleWithRequiredData);

        const workerProfileAttencionChannel = service.getWorkerProfileAttencionChannel(formGroup) as any;

        expect(workerProfileAttencionChannel).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IWorkerProfileAttencionChannel should not enable id FormControl', () => {
        const formGroup = service.createWorkerProfileAttencionChannelFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewWorkerProfileAttencionChannel should disable id FormControl', () => {
        const formGroup = service.createWorkerProfileAttencionChannelFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
