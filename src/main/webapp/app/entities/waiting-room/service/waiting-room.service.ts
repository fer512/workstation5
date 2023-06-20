import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IWaitingRoom, NewWaitingRoom } from '../waiting-room.model';

export type PartialUpdateWaitingRoom = Partial<IWaitingRoom> & Pick<IWaitingRoom, 'id'>;

export type EntityResponseType = HttpResponse<IWaitingRoom>;
export type EntityArrayResponseType = HttpResponse<IWaitingRoom[]>;

@Injectable({ providedIn: 'root' })
export class WaitingRoomService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/waiting-rooms');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(waitingRoom: NewWaitingRoom): Observable<EntityResponseType> {
    return this.http.post<IWaitingRoom>(this.resourceUrl, waitingRoom, { observe: 'response' });
  }

  update(waitingRoom: IWaitingRoom): Observable<EntityResponseType> {
    return this.http.put<IWaitingRoom>(`${this.resourceUrl}/${this.getWaitingRoomIdentifier(waitingRoom)}`, waitingRoom, {
      observe: 'response',
    });
  }

  partialUpdate(waitingRoom: PartialUpdateWaitingRoom): Observable<EntityResponseType> {
    return this.http.patch<IWaitingRoom>(`${this.resourceUrl}/${this.getWaitingRoomIdentifier(waitingRoom)}`, waitingRoom, {
      observe: 'response',
    });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IWaitingRoom>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IWaitingRoom[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getWaitingRoomIdentifier(waitingRoom: Pick<IWaitingRoom, 'id'>): number {
    return waitingRoom.id;
  }

  compareWaitingRoom(o1: Pick<IWaitingRoom, 'id'> | null, o2: Pick<IWaitingRoom, 'id'> | null): boolean {
    return o1 && o2 ? this.getWaitingRoomIdentifier(o1) === this.getWaitingRoomIdentifier(o2) : o1 === o2;
  }

  addWaitingRoomToCollectionIfMissing<Type extends Pick<IWaitingRoom, 'id'>>(
    waitingRoomCollection: Type[],
    ...waitingRoomsToCheck: (Type | null | undefined)[]
  ): Type[] {
    const waitingRooms: Type[] = waitingRoomsToCheck.filter(isPresent);
    if (waitingRooms.length > 0) {
      const waitingRoomCollectionIdentifiers = waitingRoomCollection.map(
        waitingRoomItem => this.getWaitingRoomIdentifier(waitingRoomItem)!
      );
      const waitingRoomsToAdd = waitingRooms.filter(waitingRoomItem => {
        const waitingRoomIdentifier = this.getWaitingRoomIdentifier(waitingRoomItem);
        if (waitingRoomCollectionIdentifiers.includes(waitingRoomIdentifier)) {
          return false;
        }
        waitingRoomCollectionIdentifiers.push(waitingRoomIdentifier);
        return true;
      });
      return [...waitingRoomsToAdd, ...waitingRoomCollection];
    }
    return waitingRoomCollection;
  }
}
