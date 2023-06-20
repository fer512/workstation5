import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import { IWorkerProfile, NewWorkerProfile } from '../worker-profile.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IWorkerProfile for edit and NewWorkerProfileFormGroupInput for create.
 */
type WorkerProfileFormGroupInput = IWorkerProfile | PartialWithRequiredKeyOf<NewWorkerProfile>;

type WorkerProfileFormDefaults = Pick<NewWorkerProfile, 'id' | 'branches'>;

type WorkerProfileFormGroupContent = {
  id: FormControl<IWorkerProfile['id'] | NewWorkerProfile['id']>;
  name: FormControl<IWorkerProfile['name']>;
  status: FormControl<IWorkerProfile['status']>;
  attencionChannel: FormControl<IWorkerProfile['attencionChannel']>;
  company: FormControl<IWorkerProfile['company']>;
  branches: FormControl<IWorkerProfile['branches']>;
};

export type WorkerProfileFormGroup = FormGroup<WorkerProfileFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class WorkerProfileFormService {
  createWorkerProfileFormGroup(workerProfile: WorkerProfileFormGroupInput = { id: null }): WorkerProfileFormGroup {
    const workerProfileRawValue = {
      ...this.getFormDefaults(),
      ...workerProfile,
    };
    return new FormGroup<WorkerProfileFormGroupContent>({
      id: new FormControl(
        { value: workerProfileRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        }
      ),
      name: new FormControl(workerProfileRawValue.name, {
        validators: [Validators.required],
      }),
      status: new FormControl(workerProfileRawValue.status, {
        validators: [Validators.required],
      }),
      attencionChannel: new FormControl(workerProfileRawValue.attencionChannel),
      company: new FormControl(workerProfileRawValue.company),
      branches: new FormControl(workerProfileRawValue.branches ?? []),
    });
  }

  getWorkerProfile(form: WorkerProfileFormGroup): IWorkerProfile | NewWorkerProfile {
    return form.getRawValue() as IWorkerProfile | NewWorkerProfile;
  }

  resetForm(form: WorkerProfileFormGroup, workerProfile: WorkerProfileFormGroupInput): void {
    const workerProfileRawValue = { ...this.getFormDefaults(), ...workerProfile };
    form.reset(
      {
        ...workerProfileRawValue,
        id: { value: workerProfileRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */
    );
  }

  private getFormDefaults(): WorkerProfileFormDefaults {
    return {
      id: null,
      branches: [],
    };
  }
}
