import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import dayjs from 'dayjs/esm';

import { isPresent } from 'app/core/util/operators';
import { DATE_FORMAT } from 'app/config/input.constants';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IPersonalDetails, NewPersonalDetails } from '../personal-details.model';

export type PartialUpdatePersonalDetails = Partial<IPersonalDetails> & Pick<IPersonalDetails, 'id'>;

type RestOf<T extends IPersonalDetails | NewPersonalDetails> = Omit<T, 'dateOfBirth' | 'lastModified'> & {
  dateOfBirth?: string | null;
  lastModified?: string | null;
};

export type RestPersonalDetails = RestOf<IPersonalDetails>;

export type NewRestPersonalDetails = RestOf<NewPersonalDetails>;

export type PartialUpdateRestPersonalDetails = RestOf<PartialUpdatePersonalDetails>;

export type EntityResponseType = HttpResponse<IPersonalDetails>;
export type EntityArrayResponseType = HttpResponse<IPersonalDetails[]>;

@Injectable({ providedIn: 'root' })
export class PersonalDetailsService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/personal-details');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(personalDetails: NewPersonalDetails): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(personalDetails);
    return this.http
      .post<RestPersonalDetails>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  update(personalDetails: IPersonalDetails): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(personalDetails);
    return this.http
      .put<RestPersonalDetails>(`${this.resourceUrl}/${this.getPersonalDetailsIdentifier(personalDetails)}`, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  partialUpdate(personalDetails: PartialUpdatePersonalDetails): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(personalDetails);
    return this.http
      .patch<RestPersonalDetails>(`${this.resourceUrl}/${this.getPersonalDetailsIdentifier(personalDetails)}`, copy, {
        observe: 'response',
      })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<RestPersonalDetails>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<RestPersonalDetails[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map(res => this.convertResponseArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getPersonalDetailsIdentifier(personalDetails: Pick<IPersonalDetails, 'id'>): number {
    return personalDetails.id;
  }

  comparePersonalDetails(o1: Pick<IPersonalDetails, 'id'> | null, o2: Pick<IPersonalDetails, 'id'> | null): boolean {
    return o1 && o2 ? this.getPersonalDetailsIdentifier(o1) === this.getPersonalDetailsIdentifier(o2) : o1 === o2;
  }

  addPersonalDetailsToCollectionIfMissing<Type extends Pick<IPersonalDetails, 'id'>>(
    personalDetailsCollection: Type[],
    ...personalDetailsToCheck: (Type | null | undefined)[]
  ): Type[] {
    const personalDetails: Type[] = personalDetailsToCheck.filter(isPresent);
    if (personalDetails.length > 0) {
      const personalDetailsCollectionIdentifiers = personalDetailsCollection.map(
        personalDetailsItem => this.getPersonalDetailsIdentifier(personalDetailsItem)!
      );
      const personalDetailsToAdd = personalDetails.filter(personalDetailsItem => {
        const personalDetailsIdentifier = this.getPersonalDetailsIdentifier(personalDetailsItem);
        if (personalDetailsCollectionIdentifiers.includes(personalDetailsIdentifier)) {
          return false;
        }
        personalDetailsCollectionIdentifiers.push(personalDetailsIdentifier);
        return true;
      });
      return [...personalDetailsToAdd, ...personalDetailsCollection];
    }
    return personalDetailsCollection;
  }

  protected convertDateFromClient<T extends IPersonalDetails | NewPersonalDetails | PartialUpdatePersonalDetails>(
    personalDetails: T
  ): RestOf<T> {
    return {
      ...personalDetails,
      dateOfBirth: personalDetails.dateOfBirth?.format(DATE_FORMAT) ?? null,
      lastModified: personalDetails.lastModified?.toJSON() ?? null,
    };
  }

  protected convertDateFromServer(restPersonalDetails: RestPersonalDetails): IPersonalDetails {
    return {
      ...restPersonalDetails,
      dateOfBirth: restPersonalDetails.dateOfBirth ? dayjs(restPersonalDetails.dateOfBirth) : undefined,
      lastModified: restPersonalDetails.lastModified ? dayjs(restPersonalDetails.lastModified) : undefined,
    };
  }

  protected convertResponseFromServer(res: HttpResponse<RestPersonalDetails>): HttpResponse<IPersonalDetails> {
    return res.clone({
      body: res.body ? this.convertDateFromServer(res.body) : null,
    });
  }

  protected convertResponseArrayFromServer(res: HttpResponse<RestPersonalDetails[]>): HttpResponse<IPersonalDetails[]> {
    return res.clone({
      body: res.body ? res.body.map(item => this.convertDateFromServer(item)) : null,
    });
  }
}
