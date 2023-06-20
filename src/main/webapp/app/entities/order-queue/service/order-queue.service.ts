import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IOrderQueue, NewOrderQueue } from '../order-queue.model';

export type PartialUpdateOrderQueue = Partial<IOrderQueue> & Pick<IOrderQueue, 'id'>;

export type EntityResponseType = HttpResponse<IOrderQueue>;
export type EntityArrayResponseType = HttpResponse<IOrderQueue[]>;

@Injectable({ providedIn: 'root' })
export class OrderQueueService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/order-queues');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(orderQueue: NewOrderQueue): Observable<EntityResponseType> {
    return this.http.post<IOrderQueue>(this.resourceUrl, orderQueue, { observe: 'response' });
  }

  update(orderQueue: IOrderQueue): Observable<EntityResponseType> {
    return this.http.put<IOrderQueue>(`${this.resourceUrl}/${this.getOrderQueueIdentifier(orderQueue)}`, orderQueue, {
      observe: 'response',
    });
  }

  partialUpdate(orderQueue: PartialUpdateOrderQueue): Observable<EntityResponseType> {
    return this.http.patch<IOrderQueue>(`${this.resourceUrl}/${this.getOrderQueueIdentifier(orderQueue)}`, orderQueue, {
      observe: 'response',
    });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IOrderQueue>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IOrderQueue[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getOrderQueueIdentifier(orderQueue: Pick<IOrderQueue, 'id'>): number {
    return orderQueue.id;
  }

  compareOrderQueue(o1: Pick<IOrderQueue, 'id'> | null, o2: Pick<IOrderQueue, 'id'> | null): boolean {
    return o1 && o2 ? this.getOrderQueueIdentifier(o1) === this.getOrderQueueIdentifier(o2) : o1 === o2;
  }

  addOrderQueueToCollectionIfMissing<Type extends Pick<IOrderQueue, 'id'>>(
    orderQueueCollection: Type[],
    ...orderQueuesToCheck: (Type | null | undefined)[]
  ): Type[] {
    const orderQueues: Type[] = orderQueuesToCheck.filter(isPresent);
    if (orderQueues.length > 0) {
      const orderQueueCollectionIdentifiers = orderQueueCollection.map(orderQueueItem => this.getOrderQueueIdentifier(orderQueueItem)!);
      const orderQueuesToAdd = orderQueues.filter(orderQueueItem => {
        const orderQueueIdentifier = this.getOrderQueueIdentifier(orderQueueItem);
        if (orderQueueCollectionIdentifiers.includes(orderQueueIdentifier)) {
          return false;
        }
        orderQueueCollectionIdentifiers.push(orderQueueIdentifier);
        return true;
      });
      return [...orderQueuesToAdd, ...orderQueueCollection];
    }
    return orderQueueCollection;
  }
}
