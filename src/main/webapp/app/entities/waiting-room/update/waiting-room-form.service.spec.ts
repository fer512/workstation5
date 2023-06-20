import { TestBed } from '@angular/core/testing';

import { sampleWithRequiredData, sampleWithNewData } from '../waiting-room.test-samples';

import { WaitingRoomFormService } from './waiting-room-form.service';

describe('WaitingRoom Form Service', () => {
  let service: WaitingRoomFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(WaitingRoomFormService);
  });

  describe('Service methods', () => {
    describe('createWaitingRoomFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createWaitingRoomFormGroup();

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

      it('passing IWaitingRoom should create a new form with FormGroup', () => {
        const formGroup = service.createWaitingRoomFormGroup(sampleWithRequiredData);

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

    describe('getWaitingRoom', () => {
      it('should return NewWaitingRoom for default WaitingRoom initial value', () => {
        // eslint-disable-next-line @typescript-eslint/no-unused-vars
        const formGroup = service.createWaitingRoomFormGroup(sampleWithNewData);

        const waitingRoom = service.getWaitingRoom(formGroup) as any;

        expect(waitingRoom).toMatchObject(sampleWithNewData);
      });

      it('should return NewWaitingRoom for empty WaitingRoom initial value', () => {
        const formGroup = service.createWaitingRoomFormGroup();

        const waitingRoom = service.getWaitingRoom(formGroup) as any;

        expect(waitingRoom).toMatchObject({});
      });

      it('should return IWaitingRoom', () => {
        const formGroup = service.createWaitingRoomFormGroup(sampleWithRequiredData);

        const waitingRoom = service.getWaitingRoom(formGroup) as any;

        expect(waitingRoom).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IWaitingRoom should not enable id FormControl', () => {
        const formGroup = service.createWaitingRoomFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewWaitingRoom should disable id FormControl', () => {
        const formGroup = service.createWaitingRoomFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
