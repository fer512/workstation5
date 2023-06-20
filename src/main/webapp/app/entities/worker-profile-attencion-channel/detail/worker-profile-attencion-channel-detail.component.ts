import { Component, Input } from '@angular/core';
import { ActivatedRoute, RouterModule } from '@angular/router';

import SharedModule from 'app/shared/shared.module';
import { DurationPipe, FormatMediumDatetimePipe, FormatMediumDatePipe } from 'app/shared/date';
import { IWorkerProfileAttencionChannel } from '../worker-profile-attencion-channel.model';

@Component({
  standalone: true,
  selector: 'jhi-worker-profile-attencion-channel-detail',
  templateUrl: './worker-profile-attencion-channel-detail.component.html',
  imports: [SharedModule, RouterModule, DurationPipe, FormatMediumDatetimePipe, FormatMediumDatePipe],
})
export class WorkerProfileAttencionChannelDetailComponent {
  @Input() workerProfileAttencionChannel: IWorkerProfileAttencionChannel | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  previousState(): void {
    window.history.back();
  }
}
