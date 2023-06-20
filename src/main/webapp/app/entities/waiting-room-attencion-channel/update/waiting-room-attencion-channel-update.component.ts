import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import SharedModule from 'app/shared/shared.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import {
  WaitingRoomAttencionChannelFormService,
  WaitingRoomAttencionChannelFormGroup,
} from './waiting-room-attencion-channel-form.service';
import { IWaitingRoomAttencionChannel } from '../waiting-room-attencion-channel.model';
import { WaitingRoomAttencionChannelService } from '../service/waiting-room-attencion-channel.service';
import { WaitingRoomAttencionChannelType } from 'app/entities/enumerations/waiting-room-attencion-channel-type.model';

@Component({
  standalone: true,
  selector: 'jhi-waiting-room-attencion-channel-update',
  templateUrl: './waiting-room-attencion-channel-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class WaitingRoomAttencionChannelUpdateComponent implements OnInit {
  isSaving = false;
  waitingRoomAttencionChannel: IWaitingRoomAttencionChannel | null = null;
  waitingRoomAttencionChannelTypeValues = Object.keys(WaitingRoomAttencionChannelType);

  editForm: WaitingRoomAttencionChannelFormGroup = this.waitingRoomAttencionChannelFormService.createWaitingRoomAttencionChannelFormGroup();

  constructor(
    protected waitingRoomAttencionChannelService: WaitingRoomAttencionChannelService,
    protected waitingRoomAttencionChannelFormService: WaitingRoomAttencionChannelFormService,
    protected activatedRoute: ActivatedRoute
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ waitingRoomAttencionChannel }) => {
      this.waitingRoomAttencionChannel = waitingRoomAttencionChannel;
      if (waitingRoomAttencionChannel) {
        this.updateForm(waitingRoomAttencionChannel);
      }
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const waitingRoomAttencionChannel = this.waitingRoomAttencionChannelFormService.getWaitingRoomAttencionChannel(this.editForm);
    if (waitingRoomAttencionChannel.id !== null) {
      this.subscribeToSaveResponse(this.waitingRoomAttencionChannelService.update(waitingRoomAttencionChannel));
    } else {
      this.subscribeToSaveResponse(this.waitingRoomAttencionChannelService.create(waitingRoomAttencionChannel));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IWaitingRoomAttencionChannel>>): void {
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

  protected updateForm(waitingRoomAttencionChannel: IWaitingRoomAttencionChannel): void {
    this.waitingRoomAttencionChannel = waitingRoomAttencionChannel;
    this.waitingRoomAttencionChannelFormService.resetForm(this.editForm, waitingRoomAttencionChannel);
  }
}
