package br.gov.sp.cps.fatec.pdv.view.tablemodel;

import br.gov.sp.cps.fatec.pdv.model.entity.Produto;

import javax.swing.table.AbstractTableModel;
import java.math.BigDecimal;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * Modelo de Tabela customizado para o componente {@link javax.swing.JTable},
 * especializado na exibição de instâncias de {@link Produto}.
 * <p>
 * <b>Conceito Pedagógico (Padrão MVC no Swing / Model-Delegate):</b>
 * <ul>
 * <li>O Swing adota uma variação do MVC onde o componente visual
 * ({@code JTable})
 * desacopla totalmente a renderização dos dados brutos através da abstração
 * {@link javax.swing.table.TableModel}.</li>
 * <li>Ao estender {@link AbstractTableModel}, implementamos os métodos
 * fundamentais
 * {@code getRowCount()}, {@code getColumnCount()} e {@code getValueAt()}.</li>
 * <li>Notificação Reativa: Quando a lista interna de produtos é atualizada,
 * invocamos
 * {@code fireTableDataChanged()}, notificando a JTable para que ela repinte as
 * linhas
 * de forma atômica e eficiente na Event Dispatch Thread (EDT).</li>
 * </ul>
 *
 * @author Cainã Antunes Silva
 */
public class ProdutoTableModel extends AbstractTableModel {

    private static final String[] COLUNAS = {
            "ID",
            "Código de Barras",
            "Nome do Produto",
            "Preço Unitário",
            "Qtd. Estoque"
    };

    private static final NumberFormat MOEDA_FORMATTER = NumberFormat
            .getCurrencyInstance(Locale.forLanguageTag("pt-BR"));

    private List<Produto> produtos;

    /**
     * Construtor padrão inicializando a coleção vazia.
     */
    public ProdutoTableModel() {
        this.produtos = new ArrayList<>();
    }

    /**
     * Construtor recebendo uma lista inicial de produtos.
     *
     * @param produtos Lista de produtos a ser exibida.
     */
    public ProdutoTableModel(List<Produto> produtos) {
        this.produtos = (produtos != null) ? new ArrayList<>(produtos) : new ArrayList<>();
    }

    /**
     * Atualiza a lista completa de produtos e notifica os ouvintes da JTable.
     *
     * @param novosProdutos Nova coleção de produtos obtida do banco de dados.
     */
    public void setProdutos(List<Produto> novosProdutos) {
        this.produtos = (novosProdutos != null) ? new ArrayList<>(novosProdutos) : new ArrayList<>();
        fireTableDataChanged();
    }

    /**
     * Retorna a entidade {@link Produto} correspondente à linha selecionada na
     * tabela.
     *
     * @param rowIndex Índice da linha na JTable.
     * @return {@link Produto} associado, ou {@code null} se o índice for inválido.
     */
    public Produto getProdutoAt(int rowIndex) {
        if (rowIndex >= 0 && rowIndex < produtos.size()) {
            return produtos.get(rowIndex);
        }
        return null;
    }

    @Override
    public int getRowCount() {
        return produtos.size();
    }

    @Override
    public int getColumnCount() {
        return COLUNAS.length;
    }

    @Override
    public String getColumnName(int column) {
        return COLUNAS[column];
    }

    @Override
    public Class<?> getColumnClass(int columnIndex) {
        return switch (columnIndex) {
            case 0 -> Long.class;
            case 1, 2, 3 -> String.class;
            case 4 -> Integer.class;
            default -> Object.class;
        };
    }

    @Override
    public boolean isCellEditable(int rowIndex, int columnIndex) {
        // Células da tabela são somente leitura; a edição ocorre via formulário
        // dedicado (Clean View)
        return false;
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        Produto produto = produtos.get(rowIndex);
        return switch (columnIndex) {
            case 0 -> produto.getId();
            case 1 -> produto.getCodigoBarras();
            case 2 -> produto.getNome();
            case 3 -> MOEDA_FORMATTER.format(produto.getPreco() != null ? produto.getPreco() : BigDecimal.ZERO);
            case 4 -> produto.getQuantidadeEstoque();
            default -> null;
        };
    }
}
