import { Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { AttencionChannelComponent } from './list/attencion-channel.component';
import { AttencionChannelDetailComponent } from './detail/attencion-channel-detail.component';
import { AttencionChannelUpdateComponent } from './update/attencion-channel-update.component';
import AttencionChannelResolve from './route/attencion-channel-routing-resolve.service';
import { ASC } from 'app/config/navigation.constants';

const attencionChannelRoute: Routes = [
  {
    path: '',
    component: AttencionChannelComponent,
    data: {
      defaultSort: 'id,' + ASC,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: AttencionChannelDetailComponent,
    resolve: {
      attencionChannel: AttencionChannelResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: AttencionChannelUpdateComponent,
    resolve: {
      attencionChannel: AttencionChannelResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: AttencionChannelUpdateComponent,
    resolve: {
      attencionChannel: AttencionChannelResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default attencionChannelRoute;
