import { Component, OnInit, ChangeDetectorRef } from '@angular/core';
import { CommonModule } from '@angular/common';
import { Router, RouterModule } from '@angular/router';
import { Product } from '../../core/models/product.model';
import { ProductService } from '../../core/services/product.service';

/**
 * Componente da Tela de Listagem de Produtos (espelhando a ProdutoListView do Swing).
 * Gerencia a tabela de dados, seleção ativa de produto e ações de CRUD com chamadas assíncronas.
 */
@Component({
  selector: 'app-product-list',
  standalone: true,
  imports: [CommonModule, RouterModule],
  templateUrl: './product-list.component.html',
  styleUrls: ['./product-list.component.css']
})
export class ProductListComponent implements OnInit {
  products: Product[] = [];
  selectedProduct: Product | null = null;
  isLoading = false;
  isDeleting = false;
  errorMessage = '';
  successMessage = '';

  constructor(
    private productService: ProductService,
    private router: Router,
    private cdr: ChangeDetectorRef
  ) {}

  ngOnInit(): void {
    this.loadProducts();
  }

  loadProducts(): void {
    this.isLoading = true;
    this.errorMessage = '';
    this.cdr.markForCheck();

    this.productService.findAll().subscribe({
      next: (data) => {
        this.products = data || [];
        this.isLoading = false;
        // Se o produto selecionado não existe mais na lista, limpa seleção
        if (this.selectedProduct && !this.products.some(p => p.id === this.selectedProduct?.id)) {
          this.selectedProduct = null;
        }
        this.cdr.markForCheck();
      },
      error: (err) => {
        this.isLoading = false;
        this.errorMessage = 'Erro ao conectar à API REST: Verifique se o servidor back-end está em execução.';
        console.error('Falha ao carregar produtos:', err);
        this.cdr.markForCheck();
      }
    });
  }

  selectProduct(product: Product): void {
    if (this.selectedProduct?.id === product.id) {
      this.selectedProduct = null; // Toggle desselecionar
    } else {
      this.selectedProduct = product;
    }
    this.cdr.markForCheck();
  }

  onReload(): void {
    this.loadProducts();
  }

  onNewProduct(): void {
    this.router.navigate(['/products/new']);
  }

  onEditProduct(): void {
    if (this.selectedProduct?.id) {
      this.router.navigate(['/products/edit', this.selectedProduct.id]);
    }
  }

  onDeleteProduct(): void {
    if (!this.selectedProduct?.id) {
      return;
    }

    const confirmed = window.confirm(
      `Confirma a exclusão permanente do produto "${this.selectedProduct.name}" (ID #${this.selectedProduct.id})?`
    );

    if (confirmed) {
      this.isDeleting = true;
      this.cdr.markForCheck();
      const targetId = this.selectedProduct.id;
      this.productService.deleteById(targetId).subscribe({
        next: () => {
          this.isDeleting = false;
          this.successMessage = `Produto #${targetId} excluído com sucesso!`;
          this.selectedProduct = null;
          this.cdr.markForCheck();
          this.loadProducts();
          setTimeout(() => {
            this.successMessage = '';
            this.cdr.markForCheck();
          }, 4000);
        },
        error: (err) => {
          this.isDeleting = false;
          this.errorMessage = 'Não foi possível excluir o produto. Tente novamente.';
          console.error('Falha na exclusão:', err);
          this.cdr.markForCheck();
        }
      });
    }
  }

  formatPrice(price: number): string {
    if (price == null) return 'R$ 0,00';
    return new Intl.NumberFormat('pt-BR', { style: 'currency', currency: 'BRL' }).format(price);
  }
}

