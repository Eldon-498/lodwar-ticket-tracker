export type TicketStatus = 'OPEN' | 'IN_PROGRESS' | 'CLOSED';

export type TicketPriority = 'LOW' | 'MEDIUM' | 'HIGH';

export interface Ticket {
  id: number;
  title: string;
  description: string | null;
  status: TicketStatus;
  priority: TicketPriority;
  createdAt: string;
  updatedAt: string;
}

export interface CreateTicketRequest {
  title: string;
  description?: string | null;
  priority?: TicketPriority;
}

export interface UpdateTicketRequest {
  status?: TicketStatus;
  priority?: TicketPriority;
}

export interface TicketPage {
  content: Ticket[];
  totalElements: number;
  totalPages: number;
  number: number;
  size: number;
}

export interface TicketStats {
  total: number;
  byStatus: Record<TicketStatus, number>;
}

export const TICKET_STATUSES: TicketStatus[] = ['OPEN', 'IN_PROGRESS', 'CLOSED'];
export const TICKET_PRIORITIES: TicketPriority[] = ['LOW', 'MEDIUM', 'HIGH'];
