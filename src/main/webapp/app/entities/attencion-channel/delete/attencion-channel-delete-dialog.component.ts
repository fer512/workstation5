import { Component } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import SharedModule from 'app/shared/shared.module';
import { IAttencionChannel } from '../attencion-channel.model';
import { AttencionChannelService } from '../service/attencion-channel.service';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';

@Component({
  standalone: true,
  templateUrl: './attencion-channel-delete-dialog.component.html',
  imports: [SharedModule, FormsModule],
})
export class AttencionChannelDeleteDialogComponent {
  attencionChannel?: IAttencionChannel;

  constructor(protected attencionChannelService: AttencionChannelService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.attencionChannelService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
