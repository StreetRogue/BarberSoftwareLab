import { Component, OnInit, inject, signal, computed } from '@angular/core';
import { CommonModule, CurrencyPipe, SlicePipe } from '@angular/common';
import { RouterLink } from '@angular/router';

// Importamos los servicios y modelos
import { categoriaService } from '../../core/categorias/servicios/CategoriaService'; 
import { ServicioService } from '../../core/servicios/servicios/ServicioService';
import { Categoria } from '../../core/categorias/modelos/Categoria';
import { Servicio } from '../../core/servicios/modelos/Servicio';

@Component({
  selector: 'app-ver-por-categoria',
  standalone: true,
  imports: [CommonModule, RouterLink, CurrencyPipe, SlicePipe],
  templateUrl: './ver-categoria.component.html',
  styleUrl: './ver-categoria.component.css'
})
export class VerPorCategoriaComponent implements OnInit {

  // --- Inyección de Servicios ---
  private categoriaService = inject(categoriaService);
  private servicioService = inject(ServicioService);

  // --- Signals para el Estado Reactivo ---
  
  // 1. Listados "maestros"
  public allCategories = signal<Categoria[]>([]);
  public allServices = signal<Servicio[]>([]);
  
  // 2. Estado de la UI
  public selectedCategory = signal<Categoria | null>(null);
  public isLoading = signal<boolean>(true);

  // 3. (COMPUTED) La lista filtrada que ve el usuario
  public filteredServices = computed(() => {
    const selectedCat = this.selectedCategory();
    
    // Si no hay categoría seleccionada, muestra todos
    if (selectedCat === null) {
      return this.allServices();
    }
    
    // Filtra los servicios cuyo ID de categoría coincida
    return this.allServices().filter(
      servicio => servicio.objCategoria?.id === selectedCat.id
    );
  });

  ngOnInit(): void {
    this.loadData();
  }

  loadData(): void {
    this.isLoading.set(true);
    // Cargamos las categorías
    this.categoriaService.getCategorias().subscribe({
      next: (cats) => {
        this.allCategories.set(cats);
        
        // Después de cargar categorías, cargamos todos los servicios
        this.servicioService.getServicios().subscribe({
          next: (servs) => {
            this.allServices.set(servs);
            this.isLoading.set(false); // Terminamos de cargar
          },
          error: (err) => {
            console.error('Error cargando servicios:', err);
            this.isLoading.set(false);
          }
        });
      },
      error: (err) => {
        console.error('Error cargando categorías:', err);
        this.isLoading.set(false);
      }
    });
  }

  // --- Métodos de Acción ---

  selectCategory(categoria: Categoria | null): void {
    this.selectedCategory.set(categoria);
  }

  // --- Funciones TrackBy para optimizar los bucles @for ---
  trackById(index: number, categoria: Categoria): number {
    return categoria.id;
  }
  trackByServiceId(index: number, servicio: Servicio): number {
    return servicio.id;
  }
}

