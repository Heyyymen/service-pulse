import { inject } from '@angular/core';
import { CanActivateFn, Router } from '@angular/router';
import { AuthService } from '../services/auth.service';

/**
 * 🎓 AUTH GUARD - Protège les routes (nécessite authentification)
 * 
 * Analogie React Router:
 *   <Route element={<PrivateRoute />}>
 *     <Route path="/admin" element={<Admin />} />
 *   </Route>
 */
export const authGuard: CanActivateFn = (route, state) => {
  const authService = inject(AuthService);
  const router = inject(Router);

  if (authService.isAuthenticated()) {
    return true;
  }

  // Rediriger vers login avec l'URL de retour
  router.navigate(['/login'], { queryParams: { returnUrl: state.url } });
  return false;
};
