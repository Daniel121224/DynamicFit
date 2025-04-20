import { Routes } from '@angular/router';
import { LoginComponent } from './auth/login/login.component';
import { CatalogoComponent } from './catalogo/catalogo.component';
import { ProductoComponent } from './producto/producto.component';
import { CarritoComponent } from './carrito/carrito.component';
import { InformesComponent } from './informes/informes.component';

export const routes: Routes = [
  { path: '', redirectTo: 'login', pathMatch: 'full' },
  { path: 'login', component: LoginComponent },
  { path: 'catalog', component: CatalogoComponent },
  { path: 'product/:id', component: ProductoComponent },
  { path: 'cart', component: CarritoComponent },
  { path: 'reports', component: InformesComponent },
];
