import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { IQueue } from '../queue.model';
import { sampleWithRequiredData, sampleWithNewData, sampleWithPartialData, sampleWithFullData } from '../queue.test-samples';

import { QueueService } from './queue.service';

const requireRestSample: IQueue = {
  ...sampleWithRequiredData,
};

describe('Queue Service', () => {
  let service: QueueService;
  let httpMock: HttpTestingController;
  let expectedResult: IQueue | IQueue[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(QueueService);
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

    it('should create a Queue', () => {
      // eslint-disable-next-line @typescript-eslint/no-unused-vars
      const queue = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(queue).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a Queue', () => {
      const queue = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(queue).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a Queue', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of Queue', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a Queue', () => {
      const expected = true;

      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    describe('addQueueToCollectionIfMissing', () => {
      it('should add a Queue to an empty array', () => {
        const queue: IQueue = sampleWithRequiredData;
        expectedResult = service.addQueueToCollectionIfMissing([], queue);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(queue);
      });

      it('should not add a Queue to an array that contains it', () => {
        const queue: IQueue = sampleWithRequiredData;
        const queueCollection: IQueue[] = [
          {
            ...queue,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addQueueToCollectionIfMissing(queueCollection, queue);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a Queue to an array that doesn't contain it", () => {
        const queue: IQueue = sampleWithRequiredData;
        const queueCollection: IQueue[] = [sampleWithPartialData];
        expectedResult = service.addQueueToCollectionIfMissing(queueCollection, queue);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(queue);
      });

      it('should add only unique Queue to an array', () => {
        const queueArray: IQueue[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const queueCollection: IQueue[] = [sampleWithRequiredData];
        expectedResult = service.addQueueToCollectionIfMissing(queueCollection, ...queueArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const queue: IQueue = sampleWithRequiredData;
        const queue2: IQueue = sampleWithPartialData;
        expectedResult = service.addQueueToCollectionIfMissing([], queue, queue2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(queue);
        expect(expectedResult).toContain(queue2);
      });

      it('should accept null and undefined values', () => {
        const queue: IQueue = sampleWithRequiredData;
        expectedResult = service.addQueueToCollectionIfMissing([], null, queue, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(queue);
      });

      it('should return initial array if no Queue is added', () => {
        const queueCollection: IQueue[] = [sampleWithRequiredData];
        expectedResult = service.addQueueToCollectionIfMissing(queueCollection, undefined, null);
        expect(expectedResult).toEqual(queueCollection);
      });
    });

    describe('compareQueue', () => {
      it('Should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareQueue(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('Should return false if one entity is null', () => {
        const entity1 = { id: 123 };
        const entity2 = null;

        const compareResult1 = service.compareQueue(entity1, entity2);
        const compareResult2 = service.compareQueue(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey differs', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 456 };

        const compareResult1 = service.compareQueue(entity1, entity2);
        const compareResult2 = service.compareQueue(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey matches', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 123 };

        const compareResult1 = service.compareQueue(entity1, entity2);
        const compareResult2 = service.compareQueue(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
