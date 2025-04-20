import { Component } from '@angular/core'; //Decorador para definir un componente
import { RouterOutlet } from '@angular/router';

@Component({
  selector: 'app-root',
  standalone: true,
  imports: [RouterOutlet],
  template: '<router-outlet></router-outlet>'
})
export class AppComponent {
  title = 'app';
  // Aquí puedes agregar cualquier lógica adicional que necesites en tu componente principal
  // o simplemente dejarlo vacío si solo quieres mostrar el LoginComponent.
}
