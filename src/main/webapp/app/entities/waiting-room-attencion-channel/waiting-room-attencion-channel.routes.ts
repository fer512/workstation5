import { Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { WaitingRoomAttencionChannelComponent } from './list/waiting-room-attencion-channel.component';
import { WaitingRoomAttencionChannelDetailComponent } from './detail/waiting-room-attencion-channel-detail.component';
import { WaitingRoomAttencionChannelUpdateComponent } from './update/waiting-room-attencion-channel-update.component';
import WaitingRoomAttencionChannelResolve from './route/waiting-room-attencion-channel-routing-resolve.service';
import { ASC } from 'app/config/navigation.constants';

const waitingRoomAttencionChannelRoute: Routes = [
  {
    path: '',
    component: WaitingRoomAttencionChannelComponent,
    data: {
      defaultSort: 'id,' + ASC,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: WaitingRoomAttencionChannelDetailComponent,
    resolve: {
      waitingRoomAttencionChannel: WaitingRoomAttencionChannelResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: WaitingRoomAttencionChannelUpdateComponent,
    resolve: {
      waitingRoomAttencionChannel: WaitingRoomAttencionChannelResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: WaitingRoomAttencionChannelUpdateComponent,
    resolve: {
      waitingRoomAttencionChannel: WaitingRoomAttencionChannelResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default waitingRoomAttencionChannelRoute;
