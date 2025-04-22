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

  constructor(
    private router: Router,
    private productoService: ProductoService
  ) {}

  ngOnInit(): void {
    this.productoService.obtenerProductos().subscribe({
      next: (data) => {
        // Si quieres, asignar imagen temporal a cada producto
        this.productos = data.map(p => ({
          ...p,
          imagenUrl: `assets/productos/${p.nombre_producto}.png`
        }));
      },
      error: (err) => {
        console.error('Error al obtener productos:', err);
      }
    });
  }

  verDetalle(id: number) {
    this.router.navigate(['/producto', id]);
  }
}
