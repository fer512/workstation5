import { inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of, EMPTY, Observable } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IAttencionChannel } from '../attencion-channel.model';
import { AttencionChannelService } from '../service/attencion-channel.service';

export const attencionChannelResolve = (route: ActivatedRouteSnapshot): Observable<null | IAttencionChannel> => {
  const id = route.params['id'];
  if (id) {
    return inject(AttencionChannelService)
      .find(id)
      .pipe(
        mergeMap((attencionChannel: HttpResponse<IAttencionChannel>) => {
          if (attencionChannel.body) {
            return of(attencionChannel.body);
          } else {
            inject(Router).navigate(['404']);
            return EMPTY;
          }
        })
      );
  }
  return of(null);
};

export default attencionChannelResolve;
