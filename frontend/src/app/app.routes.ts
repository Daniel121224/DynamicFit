import { Routes } from '@angular/router';
import { LoginComponent } from './pages/auth/login/login.component';
import { CatalogoComponent } from './pages/catalogo/catalogo.component';
import { ProductoComponent } from './pages/producto/producto.component';
import { CarritoComponent } from './pages/carrito/carrito.component';
import { InformesComponent } from './pages/informes/informes.component';
import { RegisterComponent } from './pages/auth/register/register.component';
import { LayoutComponent } from './layout/layout.component';

export const routes: Routes = [
  { path: '', redirectTo: 'login', pathMatch: 'full' },
  { path: 'login', component: LoginComponent },
  { path: 'register', component: RegisterComponent },
  {
    path: '',
    component: LayoutComponent,
    children: [
      { path: 'catalogo', component: CatalogoComponent },
      { path: 'producto/:id', component: ProductoComponent },
      { path: 'carrito', component: CarritoComponent },
      { path: 'informes', component: InformesComponent },
    ]
  }
];
