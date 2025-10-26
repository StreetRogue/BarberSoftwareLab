import { Component, ElementRef, AfterViewInit, ViewChild, ViewChildren, QueryList, Inject, PLATFORM_ID } from '@angular/core';
import { CommonModule, isPlatformBrowser } from '@angular/common';
import { gsap } from 'gsap';
import SplitType from 'split-type';
import { ScrollTrigger } from 'gsap/ScrollTrigger';
import { CustomCursorComponent } from '../../shared/custom-cursor/custom-cursor.component';
import { LocationComponent } from '../../shared/location/location.component';

@Component({
  selector: 'app-home',
  standalone: true,
  imports: [
    CommonModule, 
    CustomCursorComponent,
    LocationComponent
  ],
  templateUrl: './home.component.html',
  styleUrl: './home.component.css',
  
  // Arreglo de hidratación
  host: { 'ngSkipHydration': 'true' }
})
export class HomeComponent implements AfterViewInit {
  // --- Referencias al Hero ---
  @ViewChild('heroLine1') heroLine1!: ElementRef;
  @ViewChild('heroLine2') heroLine2!: ElementRef;
  @ViewChild('flipWord') flipWord!: ElementRef;
  @ViewChild('heroBtn') heroBtn!: ElementRef;
  @ViewChild('heroSubtext') heroSubtext!: ElementRef;

  // --- Referencias a los Servicios ---
  // (Las referencias siguen aquí por si las necesitamos, pero no las animamos con GSAP)
  @ViewChild('serviceTitle') serviceTitle!: ElementRef;
  @ViewChildren('serviceCard') serviceCards!: QueryList<ElementRef>;

  private isBrowser: boolean;

  constructor(@Inject(PLATFORM_ID) private platformId: Object) {
    this.isBrowser = isPlatformBrowser(this.platformId);
  }

  ngAfterViewInit(): void {
    if (this.isBrowser) {
      this.initAnimations();
    }
  }

  initAnimations(): void {
    // 1. Registrar plugins de GSAP
    gsap.registerPlugin(ScrollTrigger); 

    // --- 2. Animación Flipword para "ELEGANTE." ---
    const elegantText = new SplitType(this.flipWord.nativeElement, { types: 'chars' });

    gsap.from(elegantText.chars, {
      y: 100,
      rotateX: -90,
      opacity: 0,
      stagger: 0.05,
      duration: 0.7,
      ease: 'power3.out',
      delay: 0.5 
    });

    // --- 3. Animación de entrada para el resto del Héroe ---
    const tl = gsap.timeline({ defaults: { ease: 'power3.out' } });

    tl.to(this.heroLine1.nativeElement, { opacity: 1, y: 0, duration: 0.8 })
      .to(this.heroLine2.nativeElement, { opacity: 1, y: 0, duration: 0.8 }, "-=0.6")
      .to(this.heroBtn.nativeElement, { opacity: 1, y: 0, duration: 0.6 }, 1.2)
      .to(this.heroSubtext.nativeElement, { opacity: 1, y: 0, duration: 0.6 }, "-=0.4");
  }
}

