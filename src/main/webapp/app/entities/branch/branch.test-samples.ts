import { BranchStatus } from 'app/entities/enumerations/branch-status.model';
import { Language } from 'app/entities/enumerations/language.model';

import { IBranch, NewBranch } from './branch.model';

export const sampleWithRequiredData: IBranch = {
  id: 92196,
  name: 'general Incredible Electronics',
  status: 'ACTIVE',
  language: 'ENGLISH',
};

export const sampleWithPartialData: IBranch = {
  id: 32880,
  name: 'Group Market lime',
  status: 'DISABLED',
  language: 'FRENCH',
};

export const sampleWithFullData: IBranch = {
  id: 27290,
  name: 'likewise as Folk',
  status: 'DISABLED',
  language: 'SPANISH',
};

export const sampleWithNewData: NewBranch = {
  name: 'Lead',
  status: 'DISABLED',
  language: 'SPANISH',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
