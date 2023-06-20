import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import { IWaitingRoomAttencionChannel, NewWaitingRoomAttencionChannel } from '../waiting-room-attencion-channel.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IWaitingRoomAttencionChannel for edit and NewWaitingRoomAttencionChannelFormGroupInput for create.
 */
type WaitingRoomAttencionChannelFormGroupInput = IWaitingRoomAttencionChannel | PartialWithRequiredKeyOf<NewWaitingRoomAttencionChannel>;

type WaitingRoomAttencionChannelFormDefaults = Pick<NewWaitingRoomAttencionChannel, 'id'>;

type WaitingRoomAttencionChannelFormGroupContent = {
  id: FormControl<IWaitingRoomAttencionChannel['id'] | NewWaitingRoomAttencionChannel['id']>;
  name: FormControl<IWaitingRoomAttencionChannel['name']>;
  type: FormControl<IWaitingRoomAttencionChannel['type']>;
};

export type WaitingRoomAttencionChannelFormGroup = FormGroup<WaitingRoomAttencionChannelFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class WaitingRoomAttencionChannelFormService {
  createWaitingRoomAttencionChannelFormGroup(
    waitingRoomAttencionChannel: WaitingRoomAttencionChannelFormGroupInput = { id: null }
  ): WaitingRoomAttencionChannelFormGroup {
    const waitingRoomAttencionChannelRawValue = {
      ...this.getFormDefaults(),
      ...waitingRoomAttencionChannel,
    };
    return new FormGroup<WaitingRoomAttencionChannelFormGroupContent>({
      id: new FormControl(
        { value: waitingRoomAttencionChannelRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        }
      ),
      name: new FormControl(waitingRoomAttencionChannelRawValue.name, {
        validators: [Validators.required],
      }),
      type: new FormControl(waitingRoomAttencionChannelRawValue.type, {
        validators: [Validators.required],
      }),
    });
  }

  getWaitingRoomAttencionChannel(
    form: WaitingRoomAttencionChannelFormGroup
  ): IWaitingRoomAttencionChannel | NewWaitingRoomAttencionChannel {
    return form.getRawValue() as IWaitingRoomAttencionChannel | NewWaitingRoomAttencionChannel;
  }

  resetForm(form: WaitingRoomAttencionChannelFormGroup, waitingRoomAttencionChannel: WaitingRoomAttencionChannelFormGroupInput): void {
    const waitingRoomAttencionChannelRawValue = { ...this.getFormDefaults(), ...waitingRoomAttencionChannel };
    form.reset(
      {
        ...waitingRoomAttencionChannelRawValue,
        id: { value: waitingRoomAttencionChannelRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */
    );
  }

  private getFormDefaults(): WaitingRoomAttencionChannelFormDefaults {
    return {
      id: null,
    };
  }
}
