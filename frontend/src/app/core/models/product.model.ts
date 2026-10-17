/**
 * Modelo de dados TypeScript que espelha o contrato RESTful de Produto retornado pelo back-end.
 */
export interface Product {
  id?: number;
  barcode: string;
  name: string;
  price: number;
  stockQuantity: number;
}
