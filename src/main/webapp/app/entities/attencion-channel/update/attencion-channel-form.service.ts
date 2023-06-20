import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import { IAttencionChannel, NewAttencionChannel } from '../attencion-channel.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IAttencionChannel for edit and NewAttencionChannelFormGroupInput for create.
 */
type AttencionChannelFormGroupInput = IAttencionChannel | PartialWithRequiredKeyOf<NewAttencionChannel>;

type AttencionChannelFormDefaults = Pick<NewAttencionChannel, 'id'>;

type AttencionChannelFormGroupContent = {
  id: FormControl<IAttencionChannel['id'] | NewAttencionChannel['id']>;
  name: FormControl<IAttencionChannel['name']>;
  type: FormControl<IAttencionChannel['type']>;
};

export type AttencionChannelFormGroup = FormGroup<AttencionChannelFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class AttencionChannelFormService {
  createAttencionChannelFormGroup(attencionChannel: AttencionChannelFormGroupInput = { id: null }): AttencionChannelFormGroup {
    const attencionChannelRawValue = {
      ...this.getFormDefaults(),
      ...attencionChannel,
    };
    return new FormGroup<AttencionChannelFormGroupContent>({
      id: new FormControl(
        { value: attencionChannelRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        }
      ),
      name: new FormControl(attencionChannelRawValue.name, {
        validators: [Validators.required],
      }),
      type: new FormControl(attencionChannelRawValue.type, {
        validators: [Validators.required],
      }),
    });
  }

  getAttencionChannel(form: AttencionChannelFormGroup): IAttencionChannel | NewAttencionChannel {
    return form.getRawValue() as IAttencionChannel | NewAttencionChannel;
  }

  resetForm(form: AttencionChannelFormGroup, attencionChannel: AttencionChannelFormGroupInput): void {
    const attencionChannelRawValue = { ...this.getFormDefaults(), ...attencionChannel };
    form.reset(
      {
        ...attencionChannelRawValue,
        id: { value: attencionChannelRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */
    );
  }

  private getFormDefaults(): AttencionChannelFormDefaults {
    return {
      id: null,
    };
  }
}
