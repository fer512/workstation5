import { CompanyStatus } from 'app/entities/enumerations/company-status.model';
import { Language } from 'app/entities/enumerations/language.model';

export interface ICompany {
  id: number;
  name?: string | null;
  status?: keyof typeof CompanyStatus | null;
  language?: keyof typeof Language | null;
}

export type NewCompany = Omit<ICompany, 'id'> & { id: null };
