import { Component } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import SharedModule from 'app/shared/shared.module';
import { IWaitingRoomAttencionChannel } from '../waiting-room-attencion-channel.model';
import { WaitingRoomAttencionChannelService } from '../service/waiting-room-attencion-channel.service';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';

@Component({
  standalone: true,
  templateUrl: './waiting-room-attencion-channel-delete-dialog.component.html',
  imports: [SharedModule, FormsModule],
})
export class WaitingRoomAttencionChannelDeleteDialogComponent {
  waitingRoomAttencionChannel?: IWaitingRoomAttencionChannel;

  constructor(protected waitingRoomAttencionChannelService: WaitingRoomAttencionChannelService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.waitingRoomAttencionChannelService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
