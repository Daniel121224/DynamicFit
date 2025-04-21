import { Component, OnInit } from '@angular/core';
import { CarritoDTO, CarritoProductoDTO } from '../../models/carrito.model';
import { CarritoService } from '../../services/carrito.service';
import { ProductoService } from '../../services/producto.service';
import { Producto } from '../../models/producto.model';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';

@Component({
  selector: 'app-carrito',
  templateUrl: './carrito.component.html',
  styleUrls: ['./carrito.component.css'],
  standalone: true,
  imports: [CommonModule, FormsModule]

})
export class CarritoComponent implements OnInit {
  carrito!: CarritoDTO;
  productosEnCarrito: (Producto & { cantidad: number })[] = [];

  constructor(
    private carritoService: CarritoService,
    private productoService: ProductoService
  ) {}

  ngOnInit(): void {
    if (typeof window !== 'undefined' && localStorage.getItem('id_usuario')) {
      const idUsuario = localStorage.getItem('id_usuario');
      if (!idUsuario) {
        console.error('Usuario no logueado');
        return;
      }
  
      this.carritoService.obtenerCarritoPorUsuario(Number(idUsuario)).subscribe({
        next: (carrito) => {
          this.carrito = carrito;
          carrito.productos.forEach((item) => {
            this.productoService.obtenerProductos().subscribe((productos) => {
              const prod = productos.find(p => p.id_producto === item.producto_id);
              if (prod) {
                this.productosEnCarrito.push({ ...prod, cantidad: item.cantidad });
              }
            });
          });
        },
        error: (err) => {
          console.error('Error al obtener carrito:', err);
        }
      });
    } else {
      console.warn('localStorage no está disponible o no se encontró id_usuario.');
    }
  }


  actualizarCantidad(producto: Producto & { cantidad: number }) {
    const idCarrito = this.carrito.id_carrito;
    const idProducto = producto.id_producto;
    const nuevaCantidad = producto.cantidad;
  
    this.carritoService.actualizarCantidad(idCarrito, idProducto, nuevaCantidad).subscribe({
      next: (carritoActualizado) => {
        this.carrito = carritoActualizado;
        console.log('Cantidad actualizada');
      },
      error: (err) => {
        console.error('Error al actualizar cantidad:', err);
      }
    });
  }
  
  eliminarProducto(producto: Producto & { cantidad: number }) {
    const idCarrito = this.carrito.id_carrito;
    const idProducto = producto.id_producto;
  
    this.carritoService.eliminarProducto(idCarrito, idProducto).subscribe({
      next: () => {
        // Actualiza la lista en frontend
        this.productosEnCarrito = this.productosEnCarrito.filter(p => p.id_producto !== idProducto);
        // También puedes actualizar el total consultando el carrito nuevamente
        this.carritoService.obtenerCarritoPorUsuario(Number(localStorage.getItem('id_usuario'))).subscribe({
          next: (nuevoCarrito) => this.carrito = nuevoCarrito
        });
      },
      error: (err) => {
        console.error('Error al eliminar producto:', err);
      }
    });
  }
  

}
