import { Component, OnInit, ChangeDetectorRef } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { ActivatedRoute, Router, RouterModule } from '@angular/router';
import { ProductService } from '../../core/services/product.service';
import { Product } from '../../core/models/product.model';

/**
 * Componente da Tela de Formulário de Cadastro e Edição (espelhando a ProdutoFormView do Swing).
 * Gerencia validações reativas de campos, formatação e despacho de requisições assíncronas (POST/PUT).
 */
@Component({
  selector: 'app-product-form',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule, RouterModule],
  templateUrl: './product-form.component.html',
  styleUrls: ['./product-form.component.css']
})
export class ProductFormComponent implements OnInit {
  productForm!: FormGroup;
  isEditMode = false;
  productId: number | null = null;
  isSubmitting = false;
  isLoading = false;
  errorMessage = '';
  successMessage = '';

  constructor(
    private fb: FormBuilder,
    private productService: ProductService,
    private route: ActivatedRoute,
    private router: Router,
    private cdr: ChangeDetectorRef
  ) {}

  ngOnInit(): void {
    this.initForm();
    this.checkRouteMode();
  }

  private initForm(): void {
    this.productForm = this.fb.group({
      barcode: ['', [Validators.required, Validators.maxLength(50)]],
      name: ['', [Validators.required, Validators.maxLength(120)]],
      price: ['', [Validators.required, Validators.min(0.01)]],
      stockQuantity: ['', [Validators.required, Validators.min(0), Validators.pattern('^[0-9]+$')]]
    });
  }

  private checkRouteMode(): void {
    const idParam = this.route.snapshot.paramMap.get('id');
    if (idParam) {
      this.productId = Number(idParam);
      this.isEditMode = true;
      this.loadProductForEdit(this.productId);
    }
  }

  private loadProductForEdit(id: number): void {
    this.isLoading = true;
    this.errorMessage = '';
    this.cdr.markForCheck();

    this.productService.findById(id).subscribe({
      next: (product) => {
        this.isLoading = false;
        this.productForm.patchValue({
          barcode: product.barcode,
          name: product.name,
          price: product.price,
          stockQuantity: product.stockQuantity
        });
        this.cdr.markForCheck();
      },
      error: (err) => {
        this.isLoading = false;
        this.errorMessage = `Não foi possível carregar o produto #${id} para edição.`;
        console.error('Erro ao buscar produto:', err);
        this.cdr.markForCheck();
      }
    });
  }

  onClear(): void {
    this.productForm.reset();
    this.errorMessage = '';
    this.successMessage = '';
    this.cdr.markForCheck();
  }

  onBack(): void {
    this.router.navigate(['/products']);
  }

  onSubmit(): void {
    if (this.productForm.invalid) {
      this.productForm.markAllAsTouched();
      this.errorMessage = 'Por favor, preencha todos os campos obrigatórios corretamente.';
      this.cdr.markForCheck();
      return;
    }

    this.isSubmitting = true;
    this.errorMessage = '';
    this.successMessage = '';
    this.cdr.markForCheck();

    const formValue = this.productForm.value;
    const productPayload: Product = {
      id: this.isEditMode && this.productId ? this.productId : undefined,
      barcode: String(formValue.barcode).trim(),
      name: String(formValue.name).trim(),
      price: Number(formValue.price),
      stockQuantity: Number(formValue.stockQuantity)
    };

    this.productService.save(productPayload).subscribe({
      next: (saved) => {
        this.isSubmitting = false;
        this.successMessage = this.isEditMode
          ? `Produto #${saved.id} atualizado com sucesso!`
          : `Produto "${saved.name}" cadastrado com sucesso!`;
        this.cdr.markForCheck();

        setTimeout(() => {
          this.router.navigate(['/products']);
        }, 1200);
      },
      error: (err) => {
        this.isSubmitting = false;
        if (err.error && err.error.message) {
          this.errorMessage = err.error.message;
        } else {
          this.errorMessage = 'Falha ao salvar o produto na API. Verifique a conexão com o back-end.';
        }
        console.error('Erro ao salvar produto:', err);
        this.cdr.markForCheck();
      }
    });
  }

  isFieldInvalid(fieldName: string): boolean {
    const field = this.productForm.get(fieldName);
    return !!(field && field.invalid && (field.dirty || field.touched));
  }
}
