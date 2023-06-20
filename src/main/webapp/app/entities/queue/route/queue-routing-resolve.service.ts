import { inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of, EMPTY, Observable } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IQueue } from '../queue.model';
import { QueueService } from '../service/queue.service';

export const queueResolve = (route: ActivatedRouteSnapshot): Observable<null | IQueue> => {
  const id = route.params['id'];
  if (id) {
    return inject(QueueService)
      .find(id)
      .pipe(
        mergeMap((queue: HttpResponse<IQueue>) => {
          if (queue.body) {
            return of(queue.body);
          } else {
            inject(Router).navigate(['404']);
            return EMPTY;
          }
        })
      );
  }
  return of(null);
};

export default queueResolve;
