import { CompanyStatus } from 'app/entities/enumerations/company-status.model';
import { Language } from 'app/entities/enumerations/language.model';

import { ICompany, NewCompany } from './company.model';

export const sampleWithRequiredData: ICompany = {
  id: 87867,
  name: 'balloon katal Electric',
  status: 'ACTIVE',
  language: 'FRENCH',
};

export const sampleWithPartialData: ICompany = {
  id: 86339,
  name: 'Pula',
  status: 'ACTIVE',
  language: 'FRENCH',
};

export const sampleWithFullData: ICompany = {
  id: 81972,
  name: 'hertz Rubber Branding',
  status: 'ACTIVE',
  language: 'FRENCH',
};

export const sampleWithNewData: NewCompany = {
  name: 'Tricycle',
  status: 'ACTIVE',
  language: 'SPANISH',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
