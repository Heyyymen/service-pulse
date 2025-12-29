export type ServiceStatus = 'OPERATIONAL' | 'DEGRADED' | 'MAINTENANCE' | 'OUTAGE';

export interface Service {
    id?: number;
    name: string;
    description: string;
    status: ServiceStatus;
    maintenanceMessage?: string;
    lastUpdated?: string;
    createdAt?: string;
}

export interface ServiceSearchParams {
    q?: string;
}
