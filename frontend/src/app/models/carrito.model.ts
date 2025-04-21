export interface CarritoDTO {
    id_carrito: number;
    total_carrito: number;
    usuario_id: number;
    productos: CarritoProductoDTO[];
  }
  
  export interface CarritoProductoDTO {
    id_carrito_producto: number;
    producto_id: number;
    cantidad: number;
  }
  