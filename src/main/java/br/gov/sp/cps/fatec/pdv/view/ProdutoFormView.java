package br.gov.sp.cps.fatec.pdv.view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import br.gov.sp.cps.fatec.pdv.model.entity.Produto;

/**
 * Visão (View) do Formulário de Cadastro e Edição de Produtos.
 * <p>
 * <b>Conceito Pedagógico (Padrão MVC - Separação da Camada View):</b>
 * <ul>
 * <li>Esta classe é estritamente responsável pela apresentação gráfica e coleta
 * de dados brutos
 * digitados pelo usuário.</li>
 * <li><b>Não contém:</b> Acesso a banco de dados (JPA/SQL), regras de negócio
 * ou lógica transacional.</li>
 * <li>Os botões e campos de entrada expõem seus ouvintes/ações diretamente ao
 * {@code ProdutoController},
 * preservando o Princípio de Inversão de Controle e Responsabilidade Única
 * (SOLID).</li>
 * </ul>
 *
 * @author Cainã Antunes Silva
 */
public class ProdutoFormView extends JPanel {

    private final JLabel lblTitulo;
    private final JLabel lblSubtitulo;

    private final JTextField txtId;
    private final JTextField txtCodigoBarras;
    private final JTextField txtNome;
    private final JTextField txtPreco;
    private final JTextField txtQuantidadeEstoque;

    private final JButton btnSalvar;
    private final JButton btnLimpar;
    private final JButton btnVoltar;

    private Long produtoIdAtual;

    public ProdutoFormView() {
        setLayout(new BorderLayout(15, 15));
        setBorder(BorderFactory.createEmptyBorder(20, 25, 20, 25));

        // 1. Cabeçalho Didático
        JPanel painelCabecalho = new JPanel();
        painelCabecalho.setLayout(new BoxLayout(painelCabecalho, BoxLayout.Y_AXIS));

        lblTitulo = new JLabel("Formulário de Produto");
        lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 22));

        lblSubtitulo = new JLabel("Preencha as informações do produto abaixo para persistência via JPA.");
        lblSubtitulo.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        lblSubtitulo.setForeground(Color.GRAY);

        painelCabecalho.add(lblTitulo);
        painelCabecalho.add(Box.createVerticalStrut(4));
        painelCabecalho.add(lblSubtitulo);
        add(painelCabecalho, BorderLayout.NORTH);

        // 2. Formulário Centralizado utilizando GridBagLayout para alinhamento limpo
        JPanel painelCampos = new JPanel(new GridBagLayout());
        painelCampos.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createTitledBorder(" Dados do Produto "),
                BorderFactory.createEmptyBorder(15, 15, 15, 15)));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.anchor = GridBagConstraints.WEST;

        // Campo ID (Somente Leitura)
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 0.0;
        painelCampos.add(new JLabel("ID (Automático):"), gbc);
        txtId = new JTextField();
        txtId.setEditable(false);
        txtId.setEnabled(false);
        txtId.setPreferredSize(new Dimension(150, 30));
        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.weightx = 1.0;
        painelCampos.add(txtId, gbc);

        // Campo Código de Barras
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.weightx = 0.0;
        painelCampos.add(new JLabel("Código de Barras (*):"), gbc);
        txtCodigoBarras = new JTextField();
        txtCodigoBarras.setPreferredSize(new Dimension(300, 30));
        txtCodigoBarras.setToolTipText("Exemplo: 7891234567890 (Identificador único de barras)");
        gbc.gridx = 1;
        gbc.gridy = 1;
        gbc.weightx = 1.0;
        painelCampos.add(txtCodigoBarras, gbc);

        // Campo Nome
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.weightx = 0.0;
        painelCampos.add(new JLabel("Nome do Produto (*):"), gbc);
        txtNome = new JTextField();
        txtNome.setPreferredSize(new Dimension(300, 30));
        txtNome.setToolTipText("Nome descritivo do produto");
        gbc.gridx = 1;
        gbc.gridy = 2;
        gbc.weightx = 1.0;
        painelCampos.add(txtNome, gbc);

        // Campo Preço
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.weightx = 0.0;
        painelCampos.add(new JLabel("Preço Unitário R$ (*):"), gbc);
        txtPreco = new JTextField();
        txtPreco.setPreferredSize(new Dimension(150, 30));
        txtPreco.setToolTipText("Exemplo: 19.99 (Utilize ponto ou vírgula)");
        gbc.gridx = 1;
        gbc.gridy = 3;
        gbc.weightx = 1.0;
        painelCampos.add(txtPreco, gbc);

        // Campo Quantidade em Estoque
        gbc.gridx = 0;
        gbc.gridy = 4;
        gbc.weightx = 0.0;
        painelCampos.add(new JLabel("Quantidade Estoque (*):"), gbc);
        txtQuantidadeEstoque = new JTextField();
        txtQuantidadeEstoque.setPreferredSize(new Dimension(150, 30));
        txtQuantidadeEstoque.setToolTipText("Exemplo: 10 (Número inteiro positivo ou zero)");
        gbc.gridx = 1;
        gbc.gridy = 4;
        gbc.weightx = 1.0;
        painelCampos.add(txtQuantidadeEstoque, gbc);

        // Painel envelopador para manter o formulário no topo
        JPanel painelEnvelopador = new JPanel(new BorderLayout());
        painelEnvelopador.add(painelCampos, BorderLayout.NORTH);
        add(painelEnvelopador, BorderLayout.CENTER);

        // 3. Barra de Ações / Botões (Rodapé)
        JPanel painelBotoes = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 10));

        btnVoltar = new JButton("<< Voltar para Listagem");
        btnVoltar.setPreferredSize(new Dimension(180, 35));

        btnLimpar = new JButton("Limpar");
        btnLimpar.setPreferredSize(new Dimension(110, 35));

        btnSalvar = new JButton("Salvar Produto");
        btnSalvar.setPreferredSize(new Dimension(150, 35));
        btnSalvar.setFont(new Font("Segoe UI", Font.BOLD, 12));

        painelBotoes.add(btnVoltar);
        painelBotoes.add(btnLimpar);
        painelBotoes.add(btnSalvar);
        add(painelBotoes, BorderLayout.SOUTH);

        configurarModoNovo();
    }

    /**
     * Prepara o formulário para o cadastro de um novo produto (criação).
     */
    public void configurarModoNovo() {
        this.produtoIdAtual = null;
        this.lblTitulo.setText("Novo Produto - Cadastro");
        this.lblSubtitulo.setText("Preencha todos os campos obrigatórios (*) para persistir no banco de dados.");
        this.btnSalvar.setText("Cadastrar");
        limparCampos();
    }

    /**
     * Carrega uma entidade existente para edição no formulário.
     *
     * @param produto Objeto {@link Produto} selecionado para alteração.
     */
    public void carregarParaEdicao(Produto produto) {
        if (produto == null) {
            configurarModoNovo();
            return;
        }
        this.produtoIdAtual = produto.getId();
        this.lblTitulo.setText("Editar Produto #" + produto.getId());
        this.lblSubtitulo.setText("Atualize as informações do produto e clique em Salvar Alterações.");
        this.btnSalvar.setText("Salvar Alterações");

        this.txtId.setText(String.valueOf(produto.getId()));
        this.txtCodigoBarras.setText(produto.getCodigoBarras() != null ? produto.getCodigoBarras() : "");
        this.txtNome.setText(produto.getNome() != null ? produto.getNome() : "");
        this.txtPreco.setText(produto.getPreco() != null ? produto.getPreco().toPlainString() : "");
        this.txtQuantidadeEstoque
                .setText(produto.getQuantidadeEstoque() != null ? String.valueOf(produto.getQuantidadeEstoque()) : "0");
    }

    /**
     * Limpa os campos do formulário para nova inserção.
     */
    public void limparCampos() {
        this.txtId.setText("[Gerado após salvar]");
        this.txtCodigoBarras.setText("");
        this.txtNome.setText("");
        this.txtPreco.setText("");
        this.txtQuantidadeEstoque.setText("");
        this.txtCodigoBarras.requestFocusInWindow();
    }

    // ==========================================
    // GETTERS PARA CAMPOS E COMPONENTES
    // ==========================================

    public Long getProdutoIdAtual() {
        return produtoIdAtual;
    }

    public String getCodigoBarras() {
        return txtCodigoBarras.getText().trim();
    }

    public String getNome() {
        return txtNome.getText().trim();
    }

    public String getPrecoTexto() {
        return txtPreco.getText().trim();
    }

    public String getQuantidadeEstoqueTexto() {
        return txtQuantidadeEstoque.getText().trim();
    }

    public JButton getBtnSalvar() {
        return btnSalvar;
    }

    public JButton getBtnLimpar() {
        return btnLimpar;
    }

    public JButton getBtnVoltar() {
        return btnVoltar;
    }
}
