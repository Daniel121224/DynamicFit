import { Component } from '@angular/core';
import { NavbarComponent } from "../components/navbar/navbar.component";
import { RouterOutlet } from '@angular/router';
import { FooterComponent } from '../components/footer/footer.component'; 

@Component({
  selector: 'app-layout',
  imports: [NavbarComponent, RouterOutlet, FooterComponent],
  templateUrl: './layout.component.html',
  styleUrl: './layout.component.css'
})
export class LayoutComponent {

}
