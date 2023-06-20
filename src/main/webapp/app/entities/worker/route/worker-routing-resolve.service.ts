import { inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of, EMPTY, Observable } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IWorker } from '../worker.model';
import { WorkerService } from '../service/worker.service';

export const workerResolve = (route: ActivatedRouteSnapshot): Observable<null | IWorker> => {
  const id = route.params['id'];
  if (id) {
    return inject(WorkerService)
      .find(id)
      .pipe(
        mergeMap((worker: HttpResponse<IWorker>) => {
          if (worker.body) {
            return of(worker.body);
          } else {
            inject(Router).navigate(['404']);
            return EMPTY;
          }
        })
      );
  }
  return of(null);
};

export default workerResolve;
