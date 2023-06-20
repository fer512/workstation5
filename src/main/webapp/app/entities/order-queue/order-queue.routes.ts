import { Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { OrderQueueComponent } from './list/order-queue.component';
import { OrderQueueDetailComponent } from './detail/order-queue-detail.component';
import { OrderQueueUpdateComponent } from './update/order-queue-update.component';
import OrderQueueResolve from './route/order-queue-routing-resolve.service';
import { ASC } from 'app/config/navigation.constants';

const orderQueueRoute: Routes = [
  {
    path: '',
    component: OrderQueueComponent,
    data: {
      defaultSort: 'id,' + ASC,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: OrderQueueDetailComponent,
    resolve: {
      orderQueue: OrderQueueResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: OrderQueueUpdateComponent,
    resolve: {
      orderQueue: OrderQueueResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: OrderQueueUpdateComponent,
    resolve: {
      orderQueue: OrderQueueResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default orderQueueRoute;
