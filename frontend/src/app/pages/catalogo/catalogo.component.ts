import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { Producto } from '../../models/producto.model';
import { ProductoService } from '../../services/producto.service';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-catalogo',
  templateUrl: './catalogo.component.html',
  styleUrls: ['./catalogo.component.css'],
  standalone: true,
  imports: [CommonModule]
})
export class CatalogoComponent implements OnInit {
  productos: Producto[] = [];

  banners: string[] = [
    'assets/banner-zapatos.png',
    'assets/banner-zapatos2.jpeg',
    'assets/banner-zapatos3.jpg'
  ];

  slideActual: number = 0;
  intervalo!: any;

  constructor(
    private router: Router,
    private productoService: ProductoService
  ) {}

  ngOnInit(): void {
    this.productoService.obtenerProductos().subscribe({
      next: (data) => {
        this.productos = data.map(p => ({
          ...p,
          imagenUrl: `assets/productos/${p.nombre_producto}.png`
        }));
      },
      error: (err) => {
        console.error('Error al obtener productos:', err);
      }
    });

    this.iniciarCarrusel();
  }

  iniciarCarrusel() {
    this.intervalo = setInterval(() => {
      this.siguiente();
    }, 5000);
  }

  detenerCarrusel() {
    clearInterval(this.intervalo);
  }

  siguiente() {
    this.slideActual = (this.slideActual + 1) % this.banners.length;
  }

  anterior() {
    this.slideActual = (this.slideActual - 1 + this.banners.length) % this.banners.length;
  }

  verDetalle(id: number) {
    this.router.navigate(['/producto', id]);
  }
}

