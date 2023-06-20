import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import SharedModule from 'app/shared/shared.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import {
  WorkerProfileAttencionChannelFormService,
  WorkerProfileAttencionChannelFormGroup,
} from './worker-profile-attencion-channel-form.service';
import { IWorkerProfileAttencionChannel } from '../worker-profile-attencion-channel.model';
import { WorkerProfileAttencionChannelService } from '../service/worker-profile-attencion-channel.service';
import { WorkerProfileAttencionChannelType } from 'app/entities/enumerations/worker-profile-attencion-channel-type.model';

@Component({
  standalone: true,
  selector: 'jhi-worker-profile-attencion-channel-update',
  templateUrl: './worker-profile-attencion-channel-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class WorkerProfileAttencionChannelUpdateComponent implements OnInit {
  isSaving = false;
  workerProfileAttencionChannel: IWorkerProfileAttencionChannel | null = null;
  workerProfileAttencionChannelTypeValues = Object.keys(WorkerProfileAttencionChannelType);

  editForm: WorkerProfileAttencionChannelFormGroup =
    this.workerProfileAttencionChannelFormService.createWorkerProfileAttencionChannelFormGroup();

  constructor(
    protected workerProfileAttencionChannelService: WorkerProfileAttencionChannelService,
    protected workerProfileAttencionChannelFormService: WorkerProfileAttencionChannelFormService,
    protected activatedRoute: ActivatedRoute
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ workerProfileAttencionChannel }) => {
      this.workerProfileAttencionChannel = workerProfileAttencionChannel;
      if (workerProfileAttencionChannel) {
        this.updateForm(workerProfileAttencionChannel);
      }
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const workerProfileAttencionChannel = this.workerProfileAttencionChannelFormService.getWorkerProfileAttencionChannel(this.editForm);
    if (workerProfileAttencionChannel.id !== null) {
      this.subscribeToSaveResponse(this.workerProfileAttencionChannelService.update(workerProfileAttencionChannel));
    } else {
      this.subscribeToSaveResponse(this.workerProfileAttencionChannelService.create(workerProfileAttencionChannel));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IWorkerProfileAttencionChannel>>): void {
    result.pipe(finalize(() => this.onSaveFinalize())).subscribe({
      next: () => this.onSaveSuccess(),
      error: () => this.onSaveError(),
    });
  }

  protected onSaveSuccess(): void {
    this.previousState();
  }

  protected onSaveError(): void {
    // Api for inheritance.
  }

  protected onSaveFinalize(): void {
    this.isSaving = false;
  }

  protected updateForm(workerProfileAttencionChannel: IWorkerProfileAttencionChannel): void {
    this.workerProfileAttencionChannel = workerProfileAttencionChannel;
    this.workerProfileAttencionChannelFormService.resetForm(this.editForm, workerProfileAttencionChannel);
  }
}
