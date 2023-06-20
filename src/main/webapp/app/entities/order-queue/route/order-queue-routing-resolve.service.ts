import { inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of, EMPTY, Observable } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IOrderQueue } from '../order-queue.model';
import { OrderQueueService } from '../service/order-queue.service';

export const orderQueueResolve = (route: ActivatedRouteSnapshot): Observable<null | IOrderQueue> => {
  const id = route.params['id'];
  if (id) {
    return inject(OrderQueueService)
      .find(id)
      .pipe(
        mergeMap((orderQueue: HttpResponse<IOrderQueue>) => {
          if (orderQueue.body) {
            return of(orderQueue.body);
          } else {
            inject(Router).navigate(['404']);
            return EMPTY;
          }
        })
      );
  }
  return of(null);
};

export default orderQueueResolve;
