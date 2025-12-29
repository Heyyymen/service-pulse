import { Injectable } from '@angular/core';
import { HttpEvent, HttpHandler, HttpInterceptor, HttpRequest } from '@angular/common/http';
import { Observable } from 'rxjs';
import { AuthService } from '../services/auth.service';

/**
 * 🎓 INTERCEPTEUR HTTP - Ajoute le token JWT à chaque requête
 * 
 * Analogie JavaScript (Axios):
 *   axios.interceptors.request.use(config => {
 *     config.headers.Authorization = `Bearer ${token}`;
 *     return config;
 *   });
 */
@Injectable()
export class AuthInterceptor implements HttpInterceptor {

  constructor(private authService: AuthService) {}

  intercept(request: HttpRequest<unknown>, next: HttpHandler): Observable<HttpEvent<unknown>> {
    const token = this.authService.getToken();
    
    // Si on a un token, on l'ajoute au header Authorization
    if (token) {
      request = request.clone({
        setHeaders: {
          Authorization: `Bearer ${token}`
        }
      });
    }
    
    return next.handle(request);
  }
}
