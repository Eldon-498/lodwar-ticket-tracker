import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { HttpService } from '../../http/http.service';
import {
  CreateTicketRequest,
  Ticket,
  TicketPage,
  TicketStats,
  TicketStatus,
  UpdateTicketRequest
} from '../../../modules/tickets/models/ticket.model';

@Injectable({
  providedIn: 'root'
})
export class TicketService {

  constructor(private hs: HttpService) {}

  public createTicket(data: CreateTicketRequest): Observable<Ticket> {

    return this.hs.makeRequest<Ticket>('/tickets', 'POST', data);
  }

  public listTickets(status?: TicketStatus, page = 0, size = 20): Observable<TicketPage> {

    const params: any = { page, size };

    if (status) {

      params.status = status;
    }

    return this.hs.makeRequest<TicketPage>('/tickets', 'GET', undefined, { params });
  }

  public updateTicket(id: number, data: UpdateTicketRequest): Observable<Ticket> {

    return this.hs.makeRequest<Ticket>(`/tickets/${id}`, 'PATCH', data);
  }

  public retrieveStats(): Observable<TicketStats> {

    return this.hs.makeRequest<TicketStats>('/tickets/stats', 'GET');
  }
}
