import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { BehaviorSubject, Observable, tap } from 'rxjs';
import { LoginRequest, LoginResponse, User } from '../models/auth.model';
import { Router } from '@angular/router';

/**
 * 🎓 SERVICE D'AUTHENTIFICATION
 * 
 * Gère le login, logout, stockage du token, et état de l'utilisateur
 * 
 * Analogie JavaScript:
 *   localStorage.setItem('token', jwt);
 *   axios.defaults.headers.common['Authorization'] = `Bearer ${token}`;
 */
@Injectable({
  providedIn: 'root'
})
export class AuthService {
  private apiUrl = 'http://localhost:8080/api/auth';
  
  // BehaviorSubject = Observable avec valeur initiale (comme un state React)
  private currentUserSubject = new BehaviorSubject<User | null>(this.getUserFromStorage());
  public currentUser$ = this.currentUserSubject.asObservable();

  constructor(
    private http: HttpClient,
    private router: Router
  ) {}

  /**
   * Connexion utilisateur
   */
  login(credentials: LoginRequest): Observable<LoginResponse> {
    return this.http.post<LoginResponse>(`${this.apiUrl}/login`, credentials).pipe(
      tap(response => {
        // Stocker le token et les infos user dans localStorage
        localStorage.setItem('token', response.token);
        localStorage.setItem('user', JSON.stringify({
          username: response.username,
          fullName: response.fullName,
          roles: response.roles
        }));
        
        // Mettre à jour le BehaviorSubject
        this.currentUserSubject.next({
          username: response.username,
          fullName: response.fullName,
          roles: response.roles
        });
      })
    );
  }

  /**
   * Déconnexion
   */
  logout(): void {
    localStorage.removeItem('token');
    localStorage.removeItem('user');
    this.currentUserSubject.next(null);
    this.router.navigate(['/login']);
  }

  /**
   * Obtenir le token JWT
   */
  getToken(): string | null {
    return localStorage.getItem('token');
  }

  /**
   * Vérifier si l'utilisateur est connecté
   */
  isAuthenticated(): boolean {
    return this.getToken() !== null;
  }

  /**
   * Obtenir l'utilisateur actuel
   */
  getCurrentUser(): User | null {
    return this.currentUserSubject.value;
  }

  /**
   * Vérifier si l'utilisateur a un rôle spécifique
   */
  hasRole(role: string): boolean {
    const user = this.getCurrentUser();
    return user?.roles.includes(role) ?? false;
  }

  /**
   * Récupérer l'utilisateur depuis le localStorage
   */
  private getUserFromStorage(): User | null {
    const userJson = localStorage.getItem('user');
    return userJson ? JSON.parse(userJson) : null;
  }
}
