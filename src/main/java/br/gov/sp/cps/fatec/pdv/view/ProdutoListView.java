package br.gov.sp.cps.fatec.pdv.view;

import br.gov.sp.cps.fatec.pdv.model.entity.Produto;
import br.gov.sp.cps.fatec.pdv.view.tablemodel.ProdutoTableModel;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.util.List;

/**
 * Visão (View) da Listagem de Produtos em formato tabular ({@link JTable}).
 * <p>
 * <b>Conceito Pedagógico (Padrão MVC & Componentes Reativos do Swing):</b>
 * <ul>
 * <li>Apresenta a listagem atualizada de produtos persistidos no banco de
 * dados.</li>
 * <li>O desacoplamento é garantido através do {@link ProdutoTableModel}: a
 * tabela é atualizada
 * automaticamente sempre que novas listas são fornecidas pelo Controller.</li>
 * <li>Expõe botões de comando ("Novo Produto", "Editar", "Excluir") e método
 * para identificar
 * o registro atualmente selecionado pelo usuário.</li>
 * </ul>
 *
 * @author Cainã Antunes Silva
 */
public class ProdutoListView extends JPanel {

    private final JLabel lblTitulo;
    private final JLabel lblTotalRegistros;
    private final JTable tabelaProdutos;
    private final ProdutoTableModel tableModel;

    private final JButton btnNovo;
    private final JButton btnEditar;
    private final JButton btnExcluir;
    private final JButton btnRecarregar;

    public ProdutoListView() {
        setLayout(new BorderLayout(15, 15));
        setBorder(BorderFactory.createEmptyBorder(20, 25, 20, 25));

        // 1. Cabeçalho Didático
        JPanel painelCabecalho = new JPanel();
        painelCabecalho.setLayout(new BoxLayout(painelCabecalho, BoxLayout.Y_AXIS));

        lblTitulo = new JLabel("Catálogo de Produtos - PDV");
        lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 22));

        JLabel lblSubtitulo = new JLabel("Visualização em tempo real dos produtos persistidos no banco de dados.");
        lblSubtitulo.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        lblSubtitulo.setForeground(Color.GRAY);

        painelCabecalho.add(lblTitulo);
        painelCabecalho.add(Box.createVerticalStrut(4));
        painelCabecalho.add(lblSubtitulo);
        add(painelCabecalho, BorderLayout.NORTH);

        // 2. Tabela Central com ScrollPane
        tableModel = new ProdutoTableModel();
        tabelaProdutos = new JTable(tableModel);
        tabelaProdutos.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tabelaProdutos.setRowHeight(28);
        tabelaProdutos.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        tabelaProdutos.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 13));
        tabelaProdutos.getTableHeader().setReorderingAllowed(false);

        // Ajuste didático de largura das colunas
        tabelaProdutos.getColumnModel().getColumn(0).setPreferredWidth(60); // ID
        tabelaProdutos.getColumnModel().getColumn(1).setPreferredWidth(140); // Código de Barras
        tabelaProdutos.getColumnModel().getColumn(2).setPreferredWidth(280); // Nome
        tabelaProdutos.getColumnModel().getColumn(3).setPreferredWidth(120); // Preço
        tabelaProdutos.getColumnModel().getColumn(4).setPreferredWidth(100); // Quantidade

        JScrollPane scrollPane = new JScrollPane(tabelaProdutos);
        scrollPane.setBorder(BorderFactory.createTitledBorder(" Produtos Cadastrados "));
        add(scrollPane, BorderLayout.CENTER);

        // 3. Rodapé com Ações e Contador de Registros
        JPanel painelRodape = new JPanel(new BorderLayout(10, 10));

        lblTotalRegistros = new JLabel("Total: 0 produto(s)");
        lblTotalRegistros.setFont(new Font("Segoe UI", Font.ITALIC, 12));
        lblTotalRegistros.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        painelRodape.add(lblTotalRegistros, BorderLayout.WEST);

        JPanel painelBotoes = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 5));

        btnRecarregar = new JButton("Recarregar");
        btnRecarregar.setPreferredSize(new Dimension(110, 35));

        btnExcluir = new JButton("Excluir");
        btnExcluir.setPreferredSize(new Dimension(110, 35));
        btnExcluir.setForeground(new Color(180, 40, 40));

        btnEditar = new JButton("Editar");
        btnEditar.setPreferredSize(new Dimension(110, 35));

        btnNovo = new JButton("+ Novo Produto");
        btnNovo.setPreferredSize(new Dimension(150, 35));
        btnNovo.setFont(new Font("Segoe UI", Font.BOLD, 12));

        painelBotoes.add(btnRecarregar);
        painelBotoes.add(btnExcluir);
        painelBotoes.add(btnEditar);
        painelBotoes.add(btnNovo);

        painelRodape.add(painelBotoes, BorderLayout.EAST);
        add(painelRodape, BorderLayout.SOUTH);
    }

    /**
     * Atualiza a tabela com a lista atual de produtos e renova o contador do
     * rodapé.
     *
     * @param produtos Coleção de produtos a exibir.
     */
    public void atualizarTabela(List<Produto> produtos) {
        tableModel.setProdutos(produtos);
        int total = (produtos != null) ? produtos.size() : 0;
        lblTotalRegistros.setText(String.format("Total: %d produto(s) cadastrado(s)", total));
    }

    /**
     * Retorna a entidade {@link Produto} selecionada na linha ativa da tabela.
     *
     * @return {@link Produto} selecionado ou {@code null} se nenhuma linha estiver
     *         selecionada.
     */
    public Produto getProdutoSelecionado() {
        int selectedRow = tabelaProdutos.getSelectedRow();
        if (selectedRow >= 0) {
            return tableModel.getProdutoAt(selectedRow);
        }
        return null;
    }

    // ==========================================
    // GETTERS PARA COMPONENTES E OUVINTES
    // ==========================================

    public JTable getTabelaProdutos() {
        return tabelaProdutos;
    }

    public ProdutoTableModel getTableModel() {
        return tableModel;
    }

    public JButton getBtnNovo() {
        return btnNovo;
    }

    public JButton getBtnEditar() {
        return btnEditar;
    }

    public JButton getBtnExcluir() {
        return btnExcluir;
    }

    public JButton getBtnRecarregar() {
        return btnRecarregar;
    }
}
