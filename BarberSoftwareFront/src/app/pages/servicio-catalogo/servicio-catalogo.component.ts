import { Component, OnInit, signal, computed } from '@angular/core';
import { CommonModule, CurrencyPipe } from '@angular/common';
import { Servicio } from '../../core/servicios/modelos/Servicio';
import { ServicioService } from '../../core/servicios/servicios/ServicioService';
import { Categoria } from '../../core/categorias/modelos/Categoria';
import { categoriaService } from '../../core/categorias/servicios/CategoriaService'; 
import Swal from 'sweetalert2';
import { RouterLink } from '@angular/router';

@Component({
  selector: 'app-servicio-catalogo',
  standalone: true,
  imports: [CommonModule, CurrencyPipe, RouterLink],
  templateUrl: './servicio-catalogo.component.html',
  styleUrl: './servicio-catalogo.component.css'
})
export class ServicioCatalogoComponent implements OnInit {

  // --- Signals de Estado ---
  public isLoading = signal<boolean>(true);
  public selectedCategoryId = signal<number | null>(null); 
  public allCategories = signal<Categoria[]>([]);
  public filteredServices = signal<Servicio[]>([]);

  // 'currentTitle' sigue funcionando porque depende de signals que aún existen.
  public currentTitle = computed(() => {
    const categoryId = this.selectedCategoryId();
    if (categoryId === null) {
      return "Todos los Servicios";
    }
    const category = this.allCategories().find(c => c.id === categoryId);
    return category ? category.nombre : "Servicios";
  });

  constructor(
    private servicioService: ServicioService,
    private categoriaService: categoriaService
  ) {}


  ngOnInit(): void {
    this.loadData();
  }

  loadData(): void {
    this.isLoading.set(true);
    
    this.categoriaService.getCategorias().subscribe({
      next: (categorias) => {
        this.allCategories.set(categorias);
        
        this.servicioService.getServiciosCliente().subscribe({
          next: (servicios) => {
            this.filteredServices.set(servicios); 
            this.isLoading.set(false); // Terminamos de cargar todo
          },
          error: (err) => this.handleError(err)
        });
      },
      error: (err) => this.handleError(err)
    });
  }

  selectCategory(id: number | null): void {
    this.selectedCategoryId.set(id);
    this.isLoading.set(true); 

    // Determina a qué endpoint llamar
    const apiCall = id === null  ? this.servicioService.getServiciosCliente() : this.servicioService.getServiciosClientePorCategoria(id);
    apiCall.subscribe({
      next: (servicios) => {
        this.filteredServices.set(servicios); 
        this.isLoading.set(false);
      },
      error: (err) => this.handleError(err)
    });
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

