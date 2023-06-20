import { inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of, EMPTY, Observable } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IWorkerProfileAttencionChannel } from '../worker-profile-attencion-channel.model';
import { WorkerProfileAttencionChannelService } from '../service/worker-profile-attencion-channel.service';

export const workerProfileAttencionChannelResolve = (route: ActivatedRouteSnapshot): Observable<null | IWorkerProfileAttencionChannel> => {
  const id = route.params['id'];
  if (id) {
    return inject(WorkerProfileAttencionChannelService)
      .find(id)
      .pipe(
        mergeMap((workerProfileAttencionChannel: HttpResponse<IWorkerProfileAttencionChannel>) => {
          if (workerProfileAttencionChannel.body) {
            return of(workerProfileAttencionChannel.body);
          } else {
            inject(Router).navigate(['404']);
            return EMPTY;
          }
        })
      );
  }
  return of(null);
};

export default workerProfileAttencionChannelResolve;
