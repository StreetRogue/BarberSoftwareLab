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
import { CuponesComponent } from './pages/cupones/cupones.component';
import { AyudaComponent } from './pages/ayuda/ayuda.component';
import { OfertasComponent } from './pages/ofertas/ofertas.component';

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

  // --- Flujo de Cliente ---
  { 
    path: 'cliente', 
    component: ClienteDashboardComponent 
  },

  { 
    path: 'cliente/ver-servicio', 
    component: ServicioCatalogoComponent 
  },

  
  { 
    path: 'admin', 
    component: AdminDashboardComponent 
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

   { path: 'ofertas', component: OfertasComponent },
   { path: 'cupones', component: CuponesComponent },
   { path: 'ayuda', component: AyudaComponent },

  // --- Ruta Wildcard ---
  // Redirige cualquier ruta no encontrada al inicio
  { 
    path: '**', 
    redirectTo: '' 
  }
];
