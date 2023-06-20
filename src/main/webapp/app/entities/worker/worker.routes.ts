import { Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { WorkerComponent } from './list/worker.component';
import { WorkerDetailComponent } from './detail/worker-detail.component';
import { WorkerUpdateComponent } from './update/worker-update.component';
import WorkerResolve from './route/worker-routing-resolve.service';
import { ASC } from 'app/config/navigation.constants';

const workerRoute: Routes = [
  {
    path: '',
    component: WorkerComponent,
    data: {
      defaultSort: 'id,' + ASC,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: WorkerDetailComponent,
    resolve: {
      worker: WorkerResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: WorkerUpdateComponent,
    resolve: {
      worker: WorkerResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: WorkerUpdateComponent,
    resolve: {
      worker: WorkerResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default workerRoute;
