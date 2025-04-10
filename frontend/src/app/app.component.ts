import { Component } from '@angular/core';
import { LoginComponent } from './auth/login/login.component';

@Component({
  selector: 'app-root',
  standalone: true,
  imports: [LoginComponent], // Importamos LoginComponent directamente
  template: '<app-login></app-login>', // Mostramos solo el Login
})
export class AppComponent {}
