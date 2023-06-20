import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import { IOrderQueue, NewOrderQueue } from '../order-queue.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IOrderQueue for edit and NewOrderQueueFormGroupInput for create.
 */
type OrderQueueFormGroupInput = IOrderQueue | PartialWithRequiredKeyOf<NewOrderQueue>;

type OrderQueueFormDefaults = Pick<NewOrderQueue, 'id'>;

type OrderQueueFormGroupContent = {
  id: FormControl<IOrderQueue['id'] | NewOrderQueue['id']>;
  order: FormControl<IOrderQueue['order']>;
  queue: FormControl<IOrderQueue['queue']>;
  workerProfile: FormControl<IOrderQueue['workerProfile']>;
};

export type OrderQueueFormGroup = FormGroup<OrderQueueFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class OrderQueueFormService {
  createOrderQueueFormGroup(orderQueue: OrderQueueFormGroupInput = { id: null }): OrderQueueFormGroup {
    const orderQueueRawValue = {
      ...this.getFormDefaults(),
      ...orderQueue,
    };
    return new FormGroup<OrderQueueFormGroupContent>({
      id: new FormControl(
        { value: orderQueueRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        }
      ),
      order: new FormControl(orderQueueRawValue.order, {
        validators: [Validators.required],
      }),
      queue: new FormControl(orderQueueRawValue.queue),
      workerProfile: new FormControl(orderQueueRawValue.workerProfile),
    });
  }

  getOrderQueue(form: OrderQueueFormGroup): IOrderQueue | NewOrderQueue {
    return form.getRawValue() as IOrderQueue | NewOrderQueue;
  }

  resetForm(form: OrderQueueFormGroup, orderQueue: OrderQueueFormGroupInput): void {
    const orderQueueRawValue = { ...this.getFormDefaults(), ...orderQueue };
    form.reset(
      {
        ...orderQueueRawValue,
        id: { value: orderQueueRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */
    );
  }

  private getFormDefaults(): OrderQueueFormDefaults {
    return {
      id: null,
    };
  }
}
