import { TestBed } from '@angular/core/testing';

import { sampleWithRequiredData, sampleWithNewData } from '../worker-profile.test-samples';

import { WorkerProfileFormService } from './worker-profile-form.service';

describe('WorkerProfile Form Service', () => {
  let service: WorkerProfileFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(WorkerProfileFormService);
  });

  describe('Service methods', () => {
    describe('createWorkerProfileFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createWorkerProfileFormGroup();

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

      it('passing IWorkerProfile should create a new form with FormGroup', () => {
        const formGroup = service.createWorkerProfileFormGroup(sampleWithRequiredData);

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

    describe('getWorkerProfile', () => {
      it('should return NewWorkerProfile for default WorkerProfile initial value', () => {
        // eslint-disable-next-line @typescript-eslint/no-unused-vars
        const formGroup = service.createWorkerProfileFormGroup(sampleWithNewData);

        const workerProfile = service.getWorkerProfile(formGroup) as any;

        expect(workerProfile).toMatchObject(sampleWithNewData);
      });

      it('should return NewWorkerProfile for empty WorkerProfile initial value', () => {
        const formGroup = service.createWorkerProfileFormGroup();

        const workerProfile = service.getWorkerProfile(formGroup) as any;

        expect(workerProfile).toMatchObject({});
      });

      it('should return IWorkerProfile', () => {
        const formGroup = service.createWorkerProfileFormGroup(sampleWithRequiredData);

        const workerProfile = service.getWorkerProfile(formGroup) as any;

        expect(workerProfile).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IWorkerProfile should not enable id FormControl', () => {
        const formGroup = service.createWorkerProfileFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewWorkerProfile should disable id FormControl', () => {
        const formGroup = service.createWorkerProfileFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
