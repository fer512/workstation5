import { Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { QueueComponent } from './list/queue.component';
import { QueueDetailComponent } from './detail/queue-detail.component';
import { QueueUpdateComponent } from './update/queue-update.component';
import QueueResolve from './route/queue-routing-resolve.service';
import { ASC } from 'app/config/navigation.constants';

const queueRoute: Routes = [
  {
    path: '',
    component: QueueComponent,
    data: {
      defaultSort: 'id,' + ASC,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: QueueDetailComponent,
    resolve: {
      queue: QueueResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: QueueUpdateComponent,
    resolve: {
      queue: QueueResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: QueueUpdateComponent,
    resolve: {
      queue: QueueResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default queueRoute;
