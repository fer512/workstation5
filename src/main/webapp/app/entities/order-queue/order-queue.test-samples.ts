import { IOrderQueue, NewOrderQueue } from './order-queue.model';

export const sampleWithRequiredData: IOrderQueue = {
  id: 85698,
  order: 29217,
};

export const sampleWithPartialData: IOrderQueue = {
  id: 49192,
  order: 21897,
};

export const sampleWithFullData: IOrderQueue = {
  id: 67008,
  order: 5875,
};

export const sampleWithNewData: NewOrderQueue = {
  order: 96025,
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
