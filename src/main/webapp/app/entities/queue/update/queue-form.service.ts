import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import { IQueue, NewQueue } from '../queue.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IQueue for edit and NewQueueFormGroupInput for create.
 */
type QueueFormGroupInput = IQueue | PartialWithRequiredKeyOf<NewQueue>;

type QueueFormDefaults = Pick<NewQueue, 'id' | 'branches'>;

type QueueFormGroupContent = {
  id: FormControl<IQueue['id'] | NewQueue['id']>;
  name: FormControl<IQueue['name']>;
  status: FormControl<IQueue['status']>;
  attencionChannel: FormControl<IQueue['attencionChannel']>;
  company: FormControl<IQueue['company']>;
  branches: FormControl<IQueue['branches']>;
};

export type QueueFormGroup = FormGroup<QueueFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class QueueFormService {
  createQueueFormGroup(queue: QueueFormGroupInput = { id: null }): QueueFormGroup {
    const queueRawValue = {
      ...this.getFormDefaults(),
      ...queue,
    };
    return new FormGroup<QueueFormGroupContent>({
      id: new FormControl(
        { value: queueRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        }
      ),
      name: new FormControl(queueRawValue.name, {
        validators: [Validators.required],
      }),
      status: new FormControl(queueRawValue.status, {
        validators: [Validators.required],
      }),
      attencionChannel: new FormControl(queueRawValue.attencionChannel),
      company: new FormControl(queueRawValue.company),
      branches: new FormControl(queueRawValue.branches ?? []),
    });
  }

  getQueue(form: QueueFormGroup): IQueue | NewQueue {
    return form.getRawValue() as IQueue | NewQueue;
  }

  resetForm(form: QueueFormGroup, queue: QueueFormGroupInput): void {
    const queueRawValue = { ...this.getFormDefaults(), ...queue };
    form.reset(
      {
        ...queueRawValue,
        id: { value: queueRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */
    );
  }

  private getFormDefaults(): QueueFormDefaults {
    return {
      id: null,
      branches: [],
    };
  }
}
