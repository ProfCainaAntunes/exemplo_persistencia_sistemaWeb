import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Product } from '../models/product.model';
import { environment } from '../../../environments/environment';

/**
 * Serviço reativo Angular responsável pela integração assíncrona com os endpoints RESTful da API.
 * <p>
 * <b>Conceito Pedagógico (Comunicação Assíncrona e Injeção de Dependências):</b>
 * Utiliza o {@link HttpClient} baseado em Observables (RxJS) para emitir requisições HTTP não-bloqueantes
 * ao back-end Spring Boot, atualizando o estado visual da SPA sem recarregamento de página.
 */
@Injectable({
  providedIn: 'root'
})
export class ProductService {
  private readonly apiUrl = `${environment.apiUrl}/api/products`;

  constructor(private http: HttpClient) {}

  /**
   * Recupera a lista completa de produtos cadastrados.
   */
  findAll(): Observable<Product[]> {
    return this.http.get<Product[]>(this.apiUrl);
  }

  /**
   * Localiza um produto específico pelo seu identificador primário.
   */
  findById(id: number): Observable<Product> {
    return this.http.get<Product>(`${this.apiUrl}/${id}`);
  }

  /**
   * Persiste um produto criando um novo registro (POST) ou atualizando um existente (PUT).
   */
  save(product: Product): Observable<Product> {
    return product.id
      ? this.http.put<Product>(`${this.apiUrl}/${product.id}`, product)
      : this.http.post<Product>(this.apiUrl, product);
  }

  /**
   * Remove um produto pelo seu identificador primário (DELETE).
   */
  deleteById(id: number): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}/${id}`);
  }
}
