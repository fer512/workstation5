import { Component, Input } from '@angular/core';
import { ActivatedRoute, RouterModule } from '@angular/router';

import SharedModule from 'app/shared/shared.module';
import { DurationPipe, FormatMediumDatetimePipe, FormatMediumDatePipe } from 'app/shared/date';
import { IWaitingRoomAttencionChannel } from '../waiting-room-attencion-channel.model';

@Component({
  standalone: true,
  selector: 'jhi-waiting-room-attencion-channel-detail',
  templateUrl: './waiting-room-attencion-channel-detail.component.html',
  imports: [SharedModule, RouterModule, DurationPipe, FormatMediumDatetimePipe, FormatMediumDatePipe],
})
export class WaitingRoomAttencionChannelDetailComponent {
  @Input() waitingRoomAttencionChannel: IWaitingRoomAttencionChannel | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  previousState(): void {
    window.history.back();
  }
}
