import { Component } from '@angular/core';
import { Router, RouterModule } from '@angular/router'; // 👈 Asegúrate de importar RouterModule
import { CommonModule } from '@angular/common'; // También es buena práctica

@Component({
  selector: 'app-navbar',
  standalone: true,
  imports: [CommonModule, RouterModule], // 👈 Aquí está el truco
  templateUrl: './navbar.component.html',
  styleUrls: ['./navbar.component.css']
})
export class NavbarComponent {
  constructor(private router: Router) {}

  cerrarSesion() {
    localStorage.clear(); // limpia todo el almacenamiento
    this.router.navigate(['/login']);
  }
}
