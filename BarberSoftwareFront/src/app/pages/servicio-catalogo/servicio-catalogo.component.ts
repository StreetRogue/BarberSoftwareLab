import { Component, OnInit, inject, signal, computed } from '@angular/core';
import { CommonModule, CurrencyPipe } from '@angular/common';
import { Servicio } from '../../core/servicios/modelos/Servicio';
import { ServicioService } from '../../core/servicios/servicios/ServicioService';
import { Categoria } from '../../core/categorias/modelos/Categoria';
import { categoriaService } from '../../core/categorias/servicios/CategoriaService'; 
import Swal from 'sweetalert2';

@Component({
  selector: 'app-servicio-catalogo',
  standalone: true,
  imports: [CommonModule, CurrencyPipe],
  templateUrl: './servicio-catalogo.component.html',
  styleUrl: './servicio-catalogo.component.css'
})
export class ServicioCatalogoComponent implements OnInit {

  // --- Inyección de Servicios ---
  private servicioService = inject(ServicioService);
  private categoriaService = inject(categoriaService);

  // --- Signals de Estado ---
  public isLoading = signal<boolean>(true);
  public selectedCategoryId = signal<number | null>(null); // null = "Todos"
  
  private allServices = signal<Servicio[]>([]);
  public allCategories = signal<Categoria[]>([]);

  // --- Signals Computados (La Magia) ---
  
  // Lista de servicios filtrada, se actualiza sola
  public filteredServices = computed(() => {
    const categoryId = this.selectedCategoryId();
    
    if (categoryId === null) {
      return this.allServices(); // Mostrar todos
    }
    
    return this.allServices().filter(
      servicio => servicio.objCategoria?.id === categoryId
    );
  });

  // Título dinámico
  public currentTitle = computed(() => {
    const categoryId = this.selectedCategoryId();
    if (categoryId === null) {
      return "Todos los Servicios";
    }
    // Busca el nombre de la categoría en la lista
    const category = this.allCategories().find(c => c.id === categoryId);
    return category ? category.nombre : "Servicios";
  });


  ngOnInit(): void {
    this.loadData();
  }

  loadData(): void {
    this.isLoading.set(true);
    
    // 1. Cargar Categorías
    this.categoriaService.getCategorias().subscribe({
      next: (categorias) => {
        this.allCategories.set(categorias);
        
        // 2. Cargar Servicios (solo después de tener categorías)
        this.servicioService.getServicios().subscribe({
          next: (servicios) => {
            this.allServices.set(servicios);
            this.isLoading.set(false); // Terminamos de cargar todo
          },
          error: (err) => this.handleError(err)
        });
      },
      error: (err) => this.handleError(err)
    });
  }

  // --- Métodos de Acción ---

  selectCategory(id: number | null): void {
    this.selectedCategoryId.set(id);
  }

  private handleError(err: any): void {
    console.error("Error cargando datos:", err);
    this.isLoading.set(false);
    Swal.fire(
      'Error',
      'No se pudieron cargar los datos. Por favor, intente más tarde.',
      'error'
    );
  }
}

