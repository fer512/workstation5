import { Component, Input } from '@angular/core';
import { ActivatedRoute, RouterModule } from '@angular/router';

import SharedModule from 'app/shared/shared.module';
import { DurationPipe, FormatMediumDatetimePipe, FormatMediumDatePipe } from 'app/shared/date';
import { IAttencionChannel } from '../attencion-channel.model';

@Component({
  standalone: true,
  selector: 'jhi-attencion-channel-detail',
  templateUrl: './attencion-channel-detail.component.html',
  imports: [SharedModule, RouterModule, DurationPipe, FormatMediumDatetimePipe, FormatMediumDatePipe],
})
export class AttencionChannelDetailComponent {
  @Input() attencionChannel: IAttencionChannel | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  previousState(): void {
    window.history.back();
  }
}
