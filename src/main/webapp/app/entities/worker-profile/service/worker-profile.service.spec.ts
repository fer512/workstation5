import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { IWorkerProfile } from '../worker-profile.model';
import { sampleWithRequiredData, sampleWithNewData, sampleWithPartialData, sampleWithFullData } from '../worker-profile.test-samples';

import { WorkerProfileService } from './worker-profile.service';

const requireRestSample: IWorkerProfile = {
  ...sampleWithRequiredData,
};

describe('WorkerProfile Service', () => {
  let service: WorkerProfileService;
  let httpMock: HttpTestingController;
  let expectedResult: IWorkerProfile | IWorkerProfile[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(WorkerProfileService);
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

    it('should create a WorkerProfile', () => {
      // eslint-disable-next-line @typescript-eslint/no-unused-vars
      const workerProfile = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(workerProfile).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a WorkerProfile', () => {
      const workerProfile = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(workerProfile).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a WorkerProfile', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of WorkerProfile', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a WorkerProfile', () => {
      const expected = true;

      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    describe('addWorkerProfileToCollectionIfMissing', () => {
      it('should add a WorkerProfile to an empty array', () => {
        const workerProfile: IWorkerProfile = sampleWithRequiredData;
        expectedResult = service.addWorkerProfileToCollectionIfMissing([], workerProfile);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(workerProfile);
      });

      it('should not add a WorkerProfile to an array that contains it', () => {
        const workerProfile: IWorkerProfile = sampleWithRequiredData;
        const workerProfileCollection: IWorkerProfile[] = [
          {
            ...workerProfile,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addWorkerProfileToCollectionIfMissing(workerProfileCollection, workerProfile);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a WorkerProfile to an array that doesn't contain it", () => {
        const workerProfile: IWorkerProfile = sampleWithRequiredData;
        const workerProfileCollection: IWorkerProfile[] = [sampleWithPartialData];
        expectedResult = service.addWorkerProfileToCollectionIfMissing(workerProfileCollection, workerProfile);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(workerProfile);
      });

      it('should add only unique WorkerProfile to an array', () => {
        const workerProfileArray: IWorkerProfile[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const workerProfileCollection: IWorkerProfile[] = [sampleWithRequiredData];
        expectedResult = service.addWorkerProfileToCollectionIfMissing(workerProfileCollection, ...workerProfileArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const workerProfile: IWorkerProfile = sampleWithRequiredData;
        const workerProfile2: IWorkerProfile = sampleWithPartialData;
        expectedResult = service.addWorkerProfileToCollectionIfMissing([], workerProfile, workerProfile2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(workerProfile);
        expect(expectedResult).toContain(workerProfile2);
      });

      it('should accept null and undefined values', () => {
        const workerProfile: IWorkerProfile = sampleWithRequiredData;
        expectedResult = service.addWorkerProfileToCollectionIfMissing([], null, workerProfile, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(workerProfile);
      });

      it('should return initial array if no WorkerProfile is added', () => {
        const workerProfileCollection: IWorkerProfile[] = [sampleWithRequiredData];
        expectedResult = service.addWorkerProfileToCollectionIfMissing(workerProfileCollection, undefined, null);
        expect(expectedResult).toEqual(workerProfileCollection);
      });
    });

    describe('compareWorkerProfile', () => {
      it('Should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareWorkerProfile(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('Should return false if one entity is null', () => {
        const entity1 = { id: 123 };
        const entity2 = null;

        const compareResult1 = service.compareWorkerProfile(entity1, entity2);
        const compareResult2 = service.compareWorkerProfile(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey differs', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 456 };

        const compareResult1 = service.compareWorkerProfile(entity1, entity2);
        const compareResult2 = service.compareWorkerProfile(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey matches', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 123 };

        const compareResult1 = service.compareWorkerProfile(entity1, entity2);
        const compareResult2 = service.compareWorkerProfile(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
