import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IQueue, NewQueue } from '../queue.model';

export type PartialUpdateQueue = Partial<IQueue> & Pick<IQueue, 'id'>;

export type EntityResponseType = HttpResponse<IQueue>;
export type EntityArrayResponseType = HttpResponse<IQueue[]>;

@Injectable({ providedIn: 'root' })
export class QueueService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/queues');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(queue: NewQueue): Observable<EntityResponseType> {
    return this.http.post<IQueue>(this.resourceUrl, queue, { observe: 'response' });
  }

  update(queue: IQueue): Observable<EntityResponseType> {
    return this.http.put<IQueue>(`${this.resourceUrl}/${this.getQueueIdentifier(queue)}`, queue, { observe: 'response' });
  }

  partialUpdate(queue: PartialUpdateQueue): Observable<EntityResponseType> {
    return this.http.patch<IQueue>(`${this.resourceUrl}/${this.getQueueIdentifier(queue)}`, queue, { observe: 'response' });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IQueue>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IQueue[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getQueueIdentifier(queue: Pick<IQueue, 'id'>): number {
    return queue.id;
  }

  compareQueue(o1: Pick<IQueue, 'id'> | null, o2: Pick<IQueue, 'id'> | null): boolean {
    return o1 && o2 ? this.getQueueIdentifier(o1) === this.getQueueIdentifier(o2) : o1 === o2;
  }

  addQueueToCollectionIfMissing<Type extends Pick<IQueue, 'id'>>(
    queueCollection: Type[],
    ...queuesToCheck: (Type | null | undefined)[]
  ): Type[] {
    const queues: Type[] = queuesToCheck.filter(isPresent);
    if (queues.length > 0) {
      const queueCollectionIdentifiers = queueCollection.map(queueItem => this.getQueueIdentifier(queueItem)!);
      const queuesToAdd = queues.filter(queueItem => {
        const queueIdentifier = this.getQueueIdentifier(queueItem);
        if (queueCollectionIdentifiers.includes(queueIdentifier)) {
          return false;
        }
        queueCollectionIdentifiers.push(queueIdentifier);
        return true;
      });
      return [...queuesToAdd, ...queueCollection];
    }
    return queueCollection;
  }
}
