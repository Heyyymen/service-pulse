import { Injectable, inject } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Service, ServiceStatus } from '../models/service.model';

@Injectable({
    providedIn: 'root'
})
export class ServiceApiService {
    private http = inject(HttpClient);
    private apiUrl = 'http://localhost:8080/api/services';

    getServices(): Observable<Service[]> {
        return this.http.get<Service[]>(this.apiUrl);
    }

    getServiceById(id: number): Observable<Service> {
        return this.http.get<Service>(`${this.apiUrl}/${id}`);
    }

    getServicesByStatus(status: ServiceStatus): Observable<Service[]> {
        return this.http.get<Service[]>(`${this.apiUrl}/status/${status}`);
    }

    getAlerts(): Observable<Service[]> {
        return this.http.get<Service[]>(`${this.apiUrl}/alerts`);
    }

    searchServices(query: string): Observable<Service[]> {
        return this.http.get<Service[]>(`${this.apiUrl}/search`, {
            params: new HttpParams().set('q', query)
        });
    }

    createService(service: Service): Observable<Service> {
        return this.http.post<Service>(this.apiUrl, service);
    }

    updateService(id: number, service: Service): Observable<Service> {
        return this.http.put<Service>(`${this.apiUrl}/${id}`, service);
    }

    updateServiceStatus(id: number, status: ServiceStatus, message?: string): Observable<Service> {
        const body = { status, message };
        return this.http.patch<Service>(`${this.apiUrl}/${id}/status`, body);
    }

    deleteService(id: number): Observable<void> {
        return this.http.delete<void>(`${this.apiUrl}/${id}`);
    }

    getHealthSummary(): Observable<any> {
        return this.http.get<any>(`${this.apiUrl}/health-summary`);
    }
}
