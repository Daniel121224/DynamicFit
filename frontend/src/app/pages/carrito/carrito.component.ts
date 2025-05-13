import { Component, OnInit } from '@angular/core';
import { CarritoDTO, CarritoProductoDTO } from '../../models/carrito.model';
import { CarritoService } from '../../services/carrito.service';
import { ProductoService } from '../../services/producto.service';
import { Producto } from '../../models/producto.model';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { PedidoService } from '../../services/pedido.service'; // Asegúrate de tener este servicio o usar CarritoService si maneja pedidos
import { RouterModule } from '@angular/router'; // Asegúrate de importar RouterModule si usas rutas
import Swal from 'sweetalert2';


@Component({
  selector: 'app-carrito',
  templateUrl: './carrito.component.html',
  styleUrls: ['./carrito.component.css'],
  standalone: true,
  imports: [CommonModule, FormsModule, RouterModule] // Asegúrate de importar RouterModule si usas rutas
})
export class CarritoComponent implements OnInit {
  carrito!: CarritoDTO;
  productosEnCarrito: (Producto & { cantidad: number })[] = [];
  idUsuario!: number;

  constructor(
    private carritoService: CarritoService,
    private productoService: ProductoService,
    private pedidoService: PedidoService // Si usas un servicio diferente para manejar pedidos
  ) {}

  ngOnInit(): void {
    if (typeof window !== 'undefined' && localStorage.getItem('id_usuario')) {
      const idUsuarioString = localStorage.getItem('id_usuario');
      if (!idUsuarioString) {
        console.error('Usuario no logueado');
        return;
      }
      this.idUsuario = Number(idUsuarioString);

      this.carritoService.obtenerCarritoPorUsuario(this.idUsuario).subscribe({
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
      console.warn('localStorage no disponible o no hay id_usuario.');
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
        this.productosEnCarrito = this.productosEnCarrito.filter(p => p.id_producto !== idProducto);
        this.carritoService.obtenerCarritoPorUsuario(this.idUsuario).subscribe({
          next: (nuevoCarrito) => this.carrito = nuevoCarrito
        });
        Swal.fire({
          icon: 'success',
          title: 'Producto eliminado',
          text: 'El producto fue eliminado del carrito.',
          timer: 2000,
          showConfirmButton: false
        });
      },

      error: (err) => {
        console.error('Error al eliminar producto:', err);
      }
    });
  }

  vaciarCarrito(mostrarAlerta: boolean = true) {
    this.carritoService.vaciarCarrito(this.carrito.id_carrito).subscribe({
      next: () => {
        this.productosEnCarrito = [];
        this.carrito.total_carrito = 0;

        if (mostrarAlerta) {
          Swal.fire({
            icon: 'success',
            title: 'Carrito vaciado',
            text: 'Todos los productos fueron eliminados.',
            timer: 2000,
            showConfirmButton: false
          });
        }
      },
      error: (err) => {
        console.error('Error al vaciar carrito:', err);
      }
    });
  }


  generarPedido() {
    this.pedidoService.generarPedido(this.idUsuario).subscribe({
      next: (pedido) => {
        Swal.fire({
          icon: 'success',
          title: '¡Pedido generado!',
          text: 'Tu pedido fue realizado exitosamente.',
          timer: 2500,
          showConfirmButton: false
        });
        this.vaciarCarrito(false); // No mostrar mensaje
      },

      error: (err) => {
        console.error('Error al generar pedido:', err);
      }
    });
  }
}
