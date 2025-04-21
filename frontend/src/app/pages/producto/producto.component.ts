import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { ProductoService } from '../../services/producto.service';
import { Producto } from '../../models/producto.model';
import { CarritoService } from '../../services/carrito.service';
import { FormsModule } from '@angular/forms';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-producto',
  templateUrl: './producto.component.html',
  styleUrls: ['./producto.component.css'],
  standalone: true,
  imports: [[CommonModule, FormsModule]]
})
export class ProductoComponent implements OnInit {
  producto!: Producto;
  cantidad: number = 1;

  constructor(
    private route: ActivatedRoute,
    private productoService: ProductoService,
    private carritoService: CarritoService
  ) {}

  ngOnInit(): void {
    const id = Number(this.route.snapshot.paramMap.get('id'));
    this.productoService.obtenerProductoPorId(id).subscribe({
      next: (data) => {
        this.producto = {
          ...data,
          imagenUrl: 'assets/shoes-yellow.png' // imagen por defecto
        };
      },
      error: (err) => console.error('Error al cargar producto:', err)
    });
  }

  agregarAlCarrito() {
    const idCarrito = Number(localStorage.getItem('id_carrito'));
    this.carritoService.agregarProducto(idCarrito, this.producto.id_producto, this.cantidad)
      .subscribe({
        next: (res) => alert('Producto agregado correctamente.'),
        error: (err) => alert('Error al agregar al carrito')
      });
  }
}
