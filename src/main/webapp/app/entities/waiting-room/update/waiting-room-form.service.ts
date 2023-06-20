import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import { IWaitingRoom, NewWaitingRoom } from '../waiting-room.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IWaitingRoom for edit and NewWaitingRoomFormGroupInput for create.
 */
type WaitingRoomFormGroupInput = IWaitingRoom | PartialWithRequiredKeyOf<NewWaitingRoom>;

type WaitingRoomFormDefaults = Pick<NewWaitingRoom, 'id' | 'branches'>;

type WaitingRoomFormGroupContent = {
  id: FormControl<IWaitingRoom['id'] | NewWaitingRoom['id']>;
  name: FormControl<IWaitingRoom['name']>;
  status: FormControl<IWaitingRoom['status']>;
  attencionChannel: FormControl<IWaitingRoom['attencionChannel']>;
  company: FormControl<IWaitingRoom['company']>;
  branches: FormControl<IWaitingRoom['branches']>;
};

export type WaitingRoomFormGroup = FormGroup<WaitingRoomFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class WaitingRoomFormService {
  createWaitingRoomFormGroup(waitingRoom: WaitingRoomFormGroupInput = { id: null }): WaitingRoomFormGroup {
    const waitingRoomRawValue = {
      ...this.getFormDefaults(),
      ...waitingRoom,
    };
    return new FormGroup<WaitingRoomFormGroupContent>({
      id: new FormControl(
        { value: waitingRoomRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        }
      ),
      name: new FormControl(waitingRoomRawValue.name, {
        validators: [Validators.required],
      }),
      status: new FormControl(waitingRoomRawValue.status, {
        validators: [Validators.required],
      }),
      attencionChannel: new FormControl(waitingRoomRawValue.attencionChannel),
      company: new FormControl(waitingRoomRawValue.company),
      branches: new FormControl(waitingRoomRawValue.branches ?? []),
    });
  }

  getWaitingRoom(form: WaitingRoomFormGroup): IWaitingRoom | NewWaitingRoom {
    return form.getRawValue() as IWaitingRoom | NewWaitingRoom;
  }

  resetForm(form: WaitingRoomFormGroup, waitingRoom: WaitingRoomFormGroupInput): void {
    const waitingRoomRawValue = { ...this.getFormDefaults(), ...waitingRoom };
    form.reset(
      {
        ...waitingRoomRawValue,
        id: { value: waitingRoomRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */
    );
  }

  private getFormDefaults(): WaitingRoomFormDefaults {
    return {
      id: null,
      branches: [],
    };
  }
}
