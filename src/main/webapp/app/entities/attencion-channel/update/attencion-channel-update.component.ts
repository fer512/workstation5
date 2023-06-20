import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import SharedModule from 'app/shared/shared.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { AttencionChannelFormService, AttencionChannelFormGroup } from './attencion-channel-form.service';
import { IAttencionChannel } from '../attencion-channel.model';
import { AttencionChannelService } from '../service/attencion-channel.service';
import { AttencionChannelType } from 'app/entities/enumerations/attencion-channel-type.model';

@Component({
  standalone: true,
  selector: 'jhi-attencion-channel-update',
  templateUrl: './attencion-channel-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class AttencionChannelUpdateComponent implements OnInit {
  isSaving = false;
  attencionChannel: IAttencionChannel | null = null;
  attencionChannelTypeValues = Object.keys(AttencionChannelType);

  editForm: AttencionChannelFormGroup = this.attencionChannelFormService.createAttencionChannelFormGroup();

  constructor(
    protected attencionChannelService: AttencionChannelService,
    protected attencionChannelFormService: AttencionChannelFormService,
    protected activatedRoute: ActivatedRoute
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ attencionChannel }) => {
      this.attencionChannel = attencionChannel;
      if (attencionChannel) {
        this.updateForm(attencionChannel);
      }
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const attencionChannel = this.attencionChannelFormService.getAttencionChannel(this.editForm);
    if (attencionChannel.id !== null) {
      this.subscribeToSaveResponse(this.attencionChannelService.update(attencionChannel));
    } else {
      this.subscribeToSaveResponse(this.attencionChannelService.create(attencionChannel));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IAttencionChannel>>): void {
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

  protected updateForm(attencionChannel: IAttencionChannel): void {
    this.attencionChannel = attencionChannel;
    this.attencionChannelFormService.resetForm(this.editForm, attencionChannel);
  }
}
