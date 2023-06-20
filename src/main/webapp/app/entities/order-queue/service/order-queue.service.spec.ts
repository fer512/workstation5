import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { IOrderQueue } from '../order-queue.model';
import { sampleWithRequiredData, sampleWithNewData, sampleWithPartialData, sampleWithFullData } from '../order-queue.test-samples';

import { OrderQueueService } from './order-queue.service';

const requireRestSample: IOrderQueue = {
  ...sampleWithRequiredData,
};

describe('OrderQueue Service', () => {
  let service: OrderQueueService;
  let httpMock: HttpTestingController;
  let expectedResult: IOrderQueue | IOrderQueue[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(OrderQueueService);
    httpMock = TestBed.inject(HttpTestingController);
  });

  describe('Service methods', () => {
    it('should find an element', () => {
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.find(123).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should create a OrderQueue', () => {
      // eslint-disable-next-line @typescript-eslint/no-unused-vars
      const orderQueue = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(orderQueue).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a OrderQueue', () => {
      const orderQueue = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(orderQueue).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a OrderQueue', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of OrderQueue', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a OrderQueue', () => {
      const expected = true;

      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    describe('addOrderQueueToCollectionIfMissing', () => {
      it('should add a OrderQueue to an empty array', () => {
        const orderQueue: IOrderQueue = sampleWithRequiredData;
        expectedResult = service.addOrderQueueToCollectionIfMissing([], orderQueue);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(orderQueue);
      });

      it('should not add a OrderQueue to an array that contains it', () => {
        const orderQueue: IOrderQueue = sampleWithRequiredData;
        const orderQueueCollection: IOrderQueue[] = [
          {
            ...orderQueue,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addOrderQueueToCollectionIfMissing(orderQueueCollection, orderQueue);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a OrderQueue to an array that doesn't contain it", () => {
        const orderQueue: IOrderQueue = sampleWithRequiredData;
        const orderQueueCollection: IOrderQueue[] = [sampleWithPartialData];
        expectedResult = service.addOrderQueueToCollectionIfMissing(orderQueueCollection, orderQueue);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(orderQueue);
      });

      it('should add only unique OrderQueue to an array', () => {
        const orderQueueArray: IOrderQueue[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const orderQueueCollection: IOrderQueue[] = [sampleWithRequiredData];
        expectedResult = service.addOrderQueueToCollectionIfMissing(orderQueueCollection, ...orderQueueArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const orderQueue: IOrderQueue = sampleWithRequiredData;
        const orderQueue2: IOrderQueue = sampleWithPartialData;
        expectedResult = service.addOrderQueueToCollectionIfMissing([], orderQueue, orderQueue2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(orderQueue);
        expect(expectedResult).toContain(orderQueue2);
      });

      it('should accept null and undefined values', () => {
        const orderQueue: IOrderQueue = sampleWithRequiredData;
        expectedResult = service.addOrderQueueToCollectionIfMissing([], null, orderQueue, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(orderQueue);
      });

      it('should return initial array if no OrderQueue is added', () => {
        const orderQueueCollection: IOrderQueue[] = [sampleWithRequiredData];
        expectedResult = service.addOrderQueueToCollectionIfMissing(orderQueueCollection, undefined, null);
        expect(expectedResult).toEqual(orderQueueCollection);
      });
    });

    describe('compareOrderQueue', () => {
      it('Should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareOrderQueue(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('Should return false if one entity is null', () => {
        const entity1 = { id: 123 };
        const entity2 = null;

        const compareResult1 = service.compareOrderQueue(entity1, entity2);
        const compareResult2 = service.compareOrderQueue(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey differs', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 456 };

        const compareResult1 = service.compareOrderQueue(entity1, entity2);
        const compareResult2 = service.compareOrderQueue(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey matches', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 123 };

        const compareResult1 = service.compareOrderQueue(entity1, entity2);
        const compareResult2 = service.compareOrderQueue(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
