import { Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { WorkerProfileComponent } from './list/worker-profile.component';
import { WorkerProfileDetailComponent } from './detail/worker-profile-detail.component';
import { WorkerProfileUpdateComponent } from './update/worker-profile-update.component';
import WorkerProfileResolve from './route/worker-profile-routing-resolve.service';
import { ASC } from 'app/config/navigation.constants';

const workerProfileRoute: Routes = [
  {
    path: '',
    component: WorkerProfileComponent,
    data: {
      defaultSort: 'id,' + ASC,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: WorkerProfileDetailComponent,
    resolve: {
      workerProfile: WorkerProfileResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: WorkerProfileUpdateComponent,
    resolve: {
      workerProfile: WorkerProfileResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: WorkerProfileUpdateComponent,
    resolve: {
      workerProfile: WorkerProfileResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default workerProfileRoute;
