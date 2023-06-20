import { Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { WaitingRoomComponent } from './list/waiting-room.component';
import { WaitingRoomDetailComponent } from './detail/waiting-room-detail.component';
import { WaitingRoomUpdateComponent } from './update/waiting-room-update.component';
import WaitingRoomResolve from './route/waiting-room-routing-resolve.service';
import { ASC } from 'app/config/navigation.constants';

const waitingRoomRoute: Routes = [
  {
    path: '',
    component: WaitingRoomComponent,
    data: {
      defaultSort: 'id,' + ASC,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: WaitingRoomDetailComponent,
    resolve: {
      waitingRoom: WaitingRoomResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: WaitingRoomUpdateComponent,
    resolve: {
      waitingRoom: WaitingRoomResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: WaitingRoomUpdateComponent,
    resolve: {
      waitingRoom: WaitingRoomResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default waitingRoomRoute;
