import { inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of, EMPTY, Observable } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IWaitingRoomAttencionChannel } from '../waiting-room-attencion-channel.model';
import { WaitingRoomAttencionChannelService } from '../service/waiting-room-attencion-channel.service';

export const waitingRoomAttencionChannelResolve = (route: ActivatedRouteSnapshot): Observable<null | IWaitingRoomAttencionChannel> => {
  const id = route.params['id'];
  if (id) {
    return inject(WaitingRoomAttencionChannelService)
      .find(id)
      .pipe(
        mergeMap((waitingRoomAttencionChannel: HttpResponse<IWaitingRoomAttencionChannel>) => {
          if (waitingRoomAttencionChannel.body) {
            return of(waitingRoomAttencionChannel.body);
          } else {
            inject(Router).navigate(['404']);
            return EMPTY;
          }
        })
      );
  }
  return of(null);
};

export default waitingRoomAttencionChannelResolve;
