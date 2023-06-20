import { Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { WorkerProfileAttencionChannelComponent } from './list/worker-profile-attencion-channel.component';
import { WorkerProfileAttencionChannelDetailComponent } from './detail/worker-profile-attencion-channel-detail.component';
import { WorkerProfileAttencionChannelUpdateComponent } from './update/worker-profile-attencion-channel-update.component';
import WorkerProfileAttencionChannelResolve from './route/worker-profile-attencion-channel-routing-resolve.service';
import { ASC } from 'app/config/navigation.constants';

const workerProfileAttencionChannelRoute: Routes = [
  {
    path: '',
    component: WorkerProfileAttencionChannelComponent,
    data: {
      defaultSort: 'id,' + ASC,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: WorkerProfileAttencionChannelDetailComponent,
    resolve: {
      workerProfileAttencionChannel: WorkerProfileAttencionChannelResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: WorkerProfileAttencionChannelUpdateComponent,
    resolve: {
      workerProfileAttencionChannel: WorkerProfileAttencionChannelResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: WorkerProfileAttencionChannelUpdateComponent,
    resolve: {
      workerProfileAttencionChannel: WorkerProfileAttencionChannelResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default workerProfileAttencionChannelRoute;
