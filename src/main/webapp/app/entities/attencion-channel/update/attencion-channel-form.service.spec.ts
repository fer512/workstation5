import { TestBed } from '@angular/core/testing';

import { sampleWithRequiredData, sampleWithNewData } from '../attencion-channel.test-samples';

import { AttencionChannelFormService } from './attencion-channel-form.service';

describe('AttencionChannel Form Service', () => {
  let service: AttencionChannelFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(AttencionChannelFormService);
  });

  describe('Service methods', () => {
    describe('createAttencionChannelFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createAttencionChannelFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            name: expect.any(Object),
            type: expect.any(Object),
          })
        );
      });

      it('passing IAttencionChannel should create a new form with FormGroup', () => {
        const formGroup = service.createAttencionChannelFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            name: expect.any(Object),
            type: expect.any(Object),
          })
        );
      });
    });

    describe('getAttencionChannel', () => {
      it('should return NewAttencionChannel for default AttencionChannel initial value', () => {
        // eslint-disable-next-line @typescript-eslint/no-unused-vars
        const formGroup = service.createAttencionChannelFormGroup(sampleWithNewData);

        const attencionChannel = service.getAttencionChannel(formGroup) as any;

        expect(attencionChannel).toMatchObject(sampleWithNewData);
      });

      it('should return NewAttencionChannel for empty AttencionChannel initial value', () => {
        const formGroup = service.createAttencionChannelFormGroup();

        const attencionChannel = service.getAttencionChannel(formGroup) as any;

        expect(attencionChannel).toMatchObject({});
      });

      it('should return IAttencionChannel', () => {
        const formGroup = service.createAttencionChannelFormGroup(sampleWithRequiredData);

        const attencionChannel = service.getAttencionChannel(formGroup) as any;

        expect(attencionChannel).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IAttencionChannel should not enable id FormControl', () => {
        const formGroup = service.createAttencionChannelFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewAttencionChannel should disable id FormControl', () => {
        const formGroup = service.createAttencionChannelFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
