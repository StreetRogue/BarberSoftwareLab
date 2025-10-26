import { Routes } from '@angular/router';
import { HomeComponent } from './pages/home/home.component';
import { LoginComponent } from './pages/login/login.component';
import { ClienteDashboardComponent } from './pages/cliente-dashboard/cliente-dashboard.component';
import { AdminDashboardComponent } from './pages/admin-dashboard/admin-dashboard.component';

// Importamos los componentes del taller (del PDF)
import { RegistrarProductoComponent } from './pages/registrar-producto/registrar-producto.component';
import { ListarProductoComponent } from './pages/listar-producto/listar-producto.component';
import { VerPorCategoriaComponent } from './pages/ver-categoria/ver-categoria.component'; 
import { ServicioCatalogoComponent } from './pages/servicio-catalogo/servicio-catalogo.component'; 
// (Aquí importarías los otros componentes del taller: Actualizar, Ofertas, etc.)

export const routes: Routes = [
  
  // --- Flujo Público / Cliente ---
  { 
    path: '', 
    component: HomeComponent, 
    pathMatch: 'full' 
  },
  { 
    path: 'login', 
    component: LoginComponent 
  },
  { 
    path: 'cliente', 
    component: ServicioCatalogoComponent 
  },

  // --- Flujo de Administrador ---
  { 
    path: 'admin', 
    component: AdminDashboardComponent 
    // (En un futuro, esto estaría protegido por un AuthGuard)
  },
  // Rutas anidadas para las funcionalidades del admin
  {
    path: 'admin/registrar-producto',
    component: RegistrarProductoComponent
  },
  {
    path: 'admin/listar-productos',
    component: ListarProductoComponent
  },

   { path: 'admin/ver-categorias', component: VerPorCategoriaComponent },
  // { path: 'admin/ofertas', component: OfertasComponent },
  // { path: 'admin/cupones', component: CuponesComponent },

  // --- Ruta Wildcard ---
  // Redirige cualquier ruta no encontrada al inicio
  { 
    path: '**', 
    redirectTo: '' 
  }
];
