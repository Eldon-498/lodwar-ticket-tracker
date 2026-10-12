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
  totalItems: number;
  totalPages: number;
  currentPage: number;
  pageSize: number;
}

export interface TicketStats {
  total: number;
  byStatus: Record<TicketStatus, number>;
}

export interface ApiResponse<T> {
  status: { code: number; message: string };
  data: T | null;
  totalItems?: number | null;
  totalPages?: number | null;
  currentPage?: number | null;
  pageSize?: number | null;
}

export const SUCCESS_CODE = 1;

export const TICKET_STATUSES: TicketStatus[] = ['OPEN', 'IN_PROGRESS', 'CLOSED'];
export const TICKET_PRIORITIES: TicketPriority[] = ['LOW', 'MEDIUM', 'HIGH'];
