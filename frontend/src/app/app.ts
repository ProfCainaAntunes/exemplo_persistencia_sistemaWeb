import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterOutlet, RouterModule } from '@angular/router';
import { environment } from '../environments/environment';

@Component({
  selector: 'app-root',
  standalone: true,
  imports: [CommonModule, RouterOutlet, RouterModule],
  templateUrl: './app.html',
  styleUrl: './app.css'
})
export class AppComponent {
  title = 'PDV Core';
  swaggerUrl = environment.apiUrl 
    ? `${environment.apiUrl.replace(/\/+$/, '')}/swagger-ui.html` 
    : '/swagger-ui.html';
}
