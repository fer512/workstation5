import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import { IWorkerProfileAttencionChannel, NewWorkerProfileAttencionChannel } from '../worker-profile-attencion-channel.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IWorkerProfileAttencionChannel for edit and NewWorkerProfileAttencionChannelFormGroupInput for create.
 */
type WorkerProfileAttencionChannelFormGroupInput =
  | IWorkerProfileAttencionChannel
  | PartialWithRequiredKeyOf<NewWorkerProfileAttencionChannel>;

type WorkerProfileAttencionChannelFormDefaults = Pick<NewWorkerProfileAttencionChannel, 'id'>;

type WorkerProfileAttencionChannelFormGroupContent = {
  id: FormControl<IWorkerProfileAttencionChannel['id'] | NewWorkerProfileAttencionChannel['id']>;
  name: FormControl<IWorkerProfileAttencionChannel['name']>;
  type: FormControl<IWorkerProfileAttencionChannel['type']>;
};

export type WorkerProfileAttencionChannelFormGroup = FormGroup<WorkerProfileAttencionChannelFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class WorkerProfileAttencionChannelFormService {
  createWorkerProfileAttencionChannelFormGroup(
    workerProfileAttencionChannel: WorkerProfileAttencionChannelFormGroupInput = { id: null }
  ): WorkerProfileAttencionChannelFormGroup {
    const workerProfileAttencionChannelRawValue = {
      ...this.getFormDefaults(),
      ...workerProfileAttencionChannel,
    };
    return new FormGroup<WorkerProfileAttencionChannelFormGroupContent>({
      id: new FormControl(
        { value: workerProfileAttencionChannelRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        }
      ),
      name: new FormControl(workerProfileAttencionChannelRawValue.name, {
        validators: [Validators.required],
      }),
      type: new FormControl(workerProfileAttencionChannelRawValue.type, {
        validators: [Validators.required],
      }),
    });
  }

  getWorkerProfileAttencionChannel(
    form: WorkerProfileAttencionChannelFormGroup
  ): IWorkerProfileAttencionChannel | NewWorkerProfileAttencionChannel {
    return form.getRawValue() as IWorkerProfileAttencionChannel | NewWorkerProfileAttencionChannel;
  }

  resetForm(
    form: WorkerProfileAttencionChannelFormGroup,
    workerProfileAttencionChannel: WorkerProfileAttencionChannelFormGroupInput
  ): void {
    const workerProfileAttencionChannelRawValue = { ...this.getFormDefaults(), ...workerProfileAttencionChannel };
    form.reset(
      {
        ...workerProfileAttencionChannelRawValue,
        id: { value: workerProfileAttencionChannelRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */
    );
  }

  private getFormDefaults(): WorkerProfileAttencionChannelFormDefaults {
    return {
      id: null,
    };
  }
}
