import { TestBed } from '@angular/core/testing';

import { sampleWithRequiredData, sampleWithNewData } from '../waiting-room-attencion-channel.test-samples';

import { WaitingRoomAttencionChannelFormService } from './waiting-room-attencion-channel-form.service';

describe('WaitingRoomAttencionChannel Form Service', () => {
  let service: WaitingRoomAttencionChannelFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(WaitingRoomAttencionChannelFormService);
  });

  describe('Service methods', () => {
    describe('createWaitingRoomAttencionChannelFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createWaitingRoomAttencionChannelFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            name: expect.any(Object),
            type: expect.any(Object),
          })
        );
      });

      it('passing IWaitingRoomAttencionChannel should create a new form with FormGroup', () => {
        const formGroup = service.createWaitingRoomAttencionChannelFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            name: expect.any(Object),
            type: expect.any(Object),
          })
        );
      });
    });

    describe('getWaitingRoomAttencionChannel', () => {
      it('should return NewWaitingRoomAttencionChannel for default WaitingRoomAttencionChannel initial value', () => {
        // eslint-disable-next-line @typescript-eslint/no-unused-vars
        const formGroup = service.createWaitingRoomAttencionChannelFormGroup(sampleWithNewData);

        const waitingRoomAttencionChannel = service.getWaitingRoomAttencionChannel(formGroup) as any;

        expect(waitingRoomAttencionChannel).toMatchObject(sampleWithNewData);
      });

      it('should return NewWaitingRoomAttencionChannel for empty WaitingRoomAttencionChannel initial value', () => {
        const formGroup = service.createWaitingRoomAttencionChannelFormGroup();

        const waitingRoomAttencionChannel = service.getWaitingRoomAttencionChannel(formGroup) as any;

        expect(waitingRoomAttencionChannel).toMatchObject({});
      });

      it('should return IWaitingRoomAttencionChannel', () => {
        const formGroup = service.createWaitingRoomAttencionChannelFormGroup(sampleWithRequiredData);

        const waitingRoomAttencionChannel = service.getWaitingRoomAttencionChannel(formGroup) as any;

        expect(waitingRoomAttencionChannel).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IWaitingRoomAttencionChannel should not enable id FormControl', () => {
        const formGroup = service.createWaitingRoomAttencionChannelFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewWaitingRoomAttencionChannel should disable id FormControl', () => {
        const formGroup = service.createWaitingRoomAttencionChannelFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
