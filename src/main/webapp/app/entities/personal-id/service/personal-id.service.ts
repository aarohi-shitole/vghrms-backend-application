import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import dayjs from 'dayjs/esm';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IPersonalId, NewPersonalId } from '../personal-id.model';

export type PartialUpdatePersonalId = Partial<IPersonalId> & Pick<IPersonalId, 'id'>;

type RestOf<T extends IPersonalId | NewPersonalId> = Omit<T, 'issueDate' | 'expDate' | 'lastModified'> & {
  issueDate?: string | null;
  expDate?: string | null;
  lastModified?: string | null;
};

export type RestPersonalId = RestOf<IPersonalId>;

export type NewRestPersonalId = RestOf<NewPersonalId>;

export type PartialUpdateRestPersonalId = RestOf<PartialUpdatePersonalId>;

export type EntityResponseType = HttpResponse<IPersonalId>;
export type EntityArrayResponseType = HttpResponse<IPersonalId[]>;

@Injectable({ providedIn: 'root' })
export class PersonalIdService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/personal-ids');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(personalId: NewPersonalId): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(personalId);
    return this.http
      .post<RestPersonalId>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  update(personalId: IPersonalId): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(personalId);
    return this.http
      .put<RestPersonalId>(`${this.resourceUrl}/${this.getPersonalIdIdentifier(personalId)}`, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  partialUpdate(personalId: PartialUpdatePersonalId): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(personalId);
    return this.http
      .patch<RestPersonalId>(`${this.resourceUrl}/${this.getPersonalIdIdentifier(personalId)}`, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<RestPersonalId>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<RestPersonalId[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map(res => this.convertResponseArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getPersonalIdIdentifier(personalId: Pick<IPersonalId, 'id'>): number {
    return personalId.id;
  }

  comparePersonalId(o1: Pick<IPersonalId, 'id'> | null, o2: Pick<IPersonalId, 'id'> | null): boolean {
    return o1 && o2 ? this.getPersonalIdIdentifier(o1) === this.getPersonalIdIdentifier(o2) : o1 === o2;
  }

  addPersonalIdToCollectionIfMissing<Type extends Pick<IPersonalId, 'id'>>(
    personalIdCollection: Type[],
    ...personalIdsToCheck: (Type | null | undefined)[]
  ): Type[] {
    const personalIds: Type[] = personalIdsToCheck.filter(isPresent);
    if (personalIds.length > 0) {
      const personalIdCollectionIdentifiers = personalIdCollection.map(personalIdItem => this.getPersonalIdIdentifier(personalIdItem)!);
      const personalIdsToAdd = personalIds.filter(personalIdItem => {
        const personalIdIdentifier = this.getPersonalIdIdentifier(personalIdItem);
        if (personalIdCollectionIdentifiers.includes(personalIdIdentifier)) {
          return false;
        }
        personalIdCollectionIdentifiers.push(personalIdIdentifier);
        return true;
      });
      return [...personalIdsToAdd, ...personalIdCollection];
    }
    return personalIdCollection;
  }

  protected convertDateFromClient<T extends IPersonalId | NewPersonalId | PartialUpdatePersonalId>(personalId: T): RestOf<T> {
    return {
      ...personalId,
      issueDate: personalId.issueDate?.toJSON() ?? null,
      expDate: personalId.expDate?.toJSON() ?? null,
      lastModified: personalId.lastModified?.toJSON() ?? null,
    };
  }

  protected convertDateFromServer(restPersonalId: RestPersonalId): IPersonalId {
    return {
      ...restPersonalId,
      issueDate: restPersonalId.issueDate ? dayjs(restPersonalId.issueDate) : undefined,
      expDate: restPersonalId.expDate ? dayjs(restPersonalId.expDate) : undefined,
      lastModified: restPersonalId.lastModified ? dayjs(restPersonalId.lastModified) : undefined,
    };
  }

  protected convertResponseFromServer(res: HttpResponse<RestPersonalId>): HttpResponse<IPersonalId> {
    return res.clone({
      body: res.body ? this.convertDateFromServer(res.body) : null,
    });
  }

  protected convertResponseArrayFromServer(res: HttpResponse<RestPersonalId[]>): HttpResponse<IPersonalId[]> {
    return res.clone({
      body: res.body ? res.body.map(item => this.convertDateFromServer(item)) : null,
    });
  }
}
