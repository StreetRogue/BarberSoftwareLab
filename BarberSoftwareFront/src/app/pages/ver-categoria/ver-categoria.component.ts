import { Component, OnInit, signal, computed } from '@angular/core';
import { CommonModule, CurrencyPipe, SlicePipe } from '@angular/common';
import { RouterLink } from '@angular/router';

// Importamos los servicios y modelos
import { categoriaService } from '../../core/categorias/servicios/CategoriaService'; 
import { ServicioService } from '../../core/servicios/servicios/ServicioService';
import { Categoria } from '../../core/categorias/modelos/Categoria';
import { Servicio } from '../../core/servicios/modelos/Servicio';
import Swal from 'sweetalert2'; // Importamos Swal para errores

@Component({
  selector: 'app-ver-por-categoria',
  standalone: true,
  imports: [CommonModule, RouterLink, CurrencyPipe, SlicePipe],
  templateUrl: './ver-categoria.component.html', // Corregido: el nombre de tu archivo era ver-categoria.component.html
  styleUrl: './ver-categoria.component.css'
})
export class VerPorCategoriaComponent implements OnInit {

  // --- Signals para el Estado Reactivo ---
  public allCategories = signal<Categoria[]>([]);
  public activeServices = signal<Servicio[]>([]); // Los servicios que se muestran
  public selectedCategory = signal<Categoria | null>(null);
  public isLoading = signal<boolean>(true); // Para las categorías
  public isLoadingServices = signal<boolean>(false); // Para los servicios

  // --- Inyección por Constructor ---
  constructor(
    private categoriaService: categoriaService,
    private servicioService: ServicioService
  ) {}

  ngOnInit(): void {
    // 1. Cargar las categorías primero
    this.isLoading.set(true);
    this.categoriaService.getCategorias().subscribe({
      next: (cats) => {
        this.allCategories.set(cats);
        // 2. Al iniciar, cargar "Todos" los servicios por defecto
        this.selectCategory(null);
        this.isLoading.set(false);
      },
      error: (err) => this.handleError('categorías', err)
    });
  }

  // --- Lógica de Carga Optimizada ---
  selectCategory(categoria: Categoria | null): void {
    this.selectedCategory.set(categoria);
    this.isLoadingServices.set(true); 
    
    // Si la categoría es 'null', cargamos todos
    if (categoria === null) {
      this.servicioService.getServicios().subscribe({
        next: (servs) => {
          this.activeServices.set(servs);
          this.isLoadingServices.set(false);
        },
        error: (err) => this.handleError('todos los servicios', err)
      });
    } else {
      // Si hay una categoría, usamos el endpoint específico
      this.servicioService.getServiciosPorCategoria(categoria.id).subscribe({
        next: (servs) => {
          this.activeServices.set(servs);
          this.isLoadingServices.set(false);
        },
        error: (err) => this.handleError(`servicios para ${categoria.nombre}`, err)
      });
    }
  }

  // --- Funciones TrackBy para optimizar los bucles @for ---
  trackById(index: number, categoria: Categoria): number {
    return categoria.id;
  }
  trackByServiceId(index: number, servicio: Servicio): number {
    return servicio.id;
  }
  
  // --- Manejador de Errores ---
  private handleError(tipo: string, err: any): void {
    console.error(`Error cargando ${tipo}:`, err);
    this.isLoading.set(false);
    this.isLoadingServices.set(false);
    Swal.fire('Error', `No se pudieron cargar ${tipo}.`, 'error');
  }
}