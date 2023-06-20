import { inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of, EMPTY, Observable } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IWaitingRoom } from '../waiting-room.model';
import { WaitingRoomService } from '../service/waiting-room.service';

export const waitingRoomResolve = (route: ActivatedRouteSnapshot): Observable<null | IWaitingRoom> => {
  const id = route.params['id'];
  if (id) {
    return inject(WaitingRoomService)
      .find(id)
      .pipe(
        mergeMap((waitingRoom: HttpResponse<IWaitingRoom>) => {
          if (waitingRoom.body) {
            return of(waitingRoom.body);
          } else {
            inject(Router).navigate(['404']);
            return EMPTY;
          }
        })
      );
  }
  return of(null);
};

export default waitingRoomResolve;
