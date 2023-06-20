import { inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of, EMPTY, Observable } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IWorkerProfile } from '../worker-profile.model';
import { WorkerProfileService } from '../service/worker-profile.service';

export const workerProfileResolve = (route: ActivatedRouteSnapshot): Observable<null | IWorkerProfile> => {
  const id = route.params['id'];
  if (id) {
    return inject(WorkerProfileService)
      .find(id)
      .pipe(
        mergeMap((workerProfile: HttpResponse<IWorkerProfile>) => {
          if (workerProfile.body) {
            return of(workerProfile.body);
          } else {
            inject(Router).navigate(['404']);
            return EMPTY;
          }
        })
      );
  }
  return of(null);
};

export default workerProfileResolve;
