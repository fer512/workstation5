import { Component } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import SharedModule from 'app/shared/shared.module';
import { IWorkerProfileAttencionChannel } from '../worker-profile-attencion-channel.model';
import { WorkerProfileAttencionChannelService } from '../service/worker-profile-attencion-channel.service';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';

@Component({
  standalone: true,
  templateUrl: './worker-profile-attencion-channel-delete-dialog.component.html',
  imports: [SharedModule, FormsModule],
})
export class WorkerProfileAttencionChannelDeleteDialogComponent {
  workerProfileAttencionChannel?: IWorkerProfileAttencionChannel;

  constructor(
    protected workerProfileAttencionChannelService: WorkerProfileAttencionChannelService,
    protected activeModal: NgbActiveModal
  ) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.workerProfileAttencionChannelService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
