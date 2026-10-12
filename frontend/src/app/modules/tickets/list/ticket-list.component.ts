import { ChangeDetectorRef, Component, EventEmitter, OnInit, Output, inject } from '@angular/core';
import { ToastrService } from 'ngx-toastr';
import { finalize } from 'rxjs';

import { TicketService } from '../../../services/apis/ticket/ticket.service';
import { TICKET_STATUSES, Ticket, TicketStatus } from '../models/ticket.model';

@Component({
  selector: 'lodwar-ticket-list',
  standalone: false,
  templateUrl: './ticket-list.component.html',
  styleUrl: './ticket-list.component.scss'
})
export class TicketListComponent implements OnInit {

  private ticketService = inject(TicketService);
  private toastr = inject(ToastrService);
  private cdr = inject(ChangeDetectorRef);

  @Output() statusChanged = new EventEmitter<void>();

  tickets: Ticket[] = [];
  totalItems = 0;
  totalPages = 0;
  page = 0;
  size = 10;

  statusFilter: TicketStatus | '' = '';
  statuses = TICKET_STATUSES;

  isLoading = false;

  ngOnInit(): void {

    this.load();
  }

  reload(): void {

    this.page = 0;
    this.load();
  }

  onFilterChange(value: TicketStatus | ''): void {

    this.statusFilter = value;
    this.page = 0;
    this.load();
  }

  onStatusChange(ticket: Ticket, status: TicketStatus): void {

    if (ticket.status === status) {

      return;
    }

    this.ticketService.updateTicket(ticket.id, { status }).subscribe({
      next: (updated) => {
        ticket.status = updated.status;
        ticket.updatedAt = updated.updatedAt;
        this.toastr.success(`Ticket #${ticket.id} marked ${status}`);
        this.statusChanged.emit();
        this.cdr.detectChanges();
      },
      error: () => {
        this.toastr.error(`Failed to update ticket #${ticket.id}`);
      }
    });
  }

  nextPage(): void {

    if (this.page < this.totalPages - 1) {

      this.page++;
      this.load();
    }
  }

  prevPage(): void {

    if (this.page > 0) {

      this.page--;
      this.load();
    }
  }

  private load(): void {

    this.isLoading = true;

    const status = this.statusFilter || undefined;

    this.ticketService.listTickets(status, this.page, this.size).pipe(
      finalize(() => {
        this.isLoading = false;
        this.cdr.detectChanges();
      })
    ).subscribe({
      next: (data) => {
        this.tickets = data.content;
        this.totalItems = data.totalItems;
        this.totalPages = data.totalPages;
      },
      error: () => {
        this.toastr.error('Failed to load tickets');
      }
    });
  }
}
