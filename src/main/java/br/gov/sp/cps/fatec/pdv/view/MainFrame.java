package br.gov.sp.cps.fatec.pdv.view;

import javax.swing.JFrame;
import javax.swing.JPanel;
import java.awt.CardLayout;
import java.awt.Dimension;

/**
 * Janela Principal da Aplicação Swing ({@link JFrame}).
 * <p>
 * <b>Conceito Pedagógico (Padrão CardLayout & Interface de Janela Única -
 * SDI):</b>
 * <ul>
 * <li>Em vez de abrir múltiplas janelas flutuantes desordenadas (MDI ou
 * múltiplos JFrames),
 * o {@link CardLayout} gerencia diferentes "páginas" ou painéis de visão em uma
 * única área
 * de exibição como se fossem cartas empilhadas.</li>
 * <li>A orquestração e a decisão de qual tela deve ser exibida é controlada
 * pelo
 * {@code ProdutoController}, garantindo a independência das visões entre
 * si.</li>
 * </ul>
 *
 * @author Cainã Antunes Silva
 */
public class MainFrame extends JFrame {

    public static final String TELA_LISTA = "CARD_LISTA";
    public static final String TELA_FORMULARIO = "CARD_FORMULARIO";

    private final CardLayout cardLayout;
    private final JPanel painelPrincipal;

    private final ProdutoListView listView;
    private final ProdutoFormView formView;

    public MainFrame() {
        super("PDV - Gestão de Produtos (FATEC - Técnicas de Programação II)");

        this.cardLayout = new CardLayout();
        this.painelPrincipal = new JPanel(cardLayout);

        this.listView = new ProdutoListView();
        this.formView = new ProdutoFormView();

        // Adiciona as telas ao container gerenciado por CardLayout
        this.painelPrincipal.add(listView, TELA_LISTA);
        this.painelPrincipal.add(formView, TELA_FORMULARIO);

        setContentPane(painelPrincipal);

        // Configurações da Janela
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setMinimumSize(new Dimension(850, 600));
        setPreferredSize(new Dimension(950, 650));
        pack();
        setLocationRelativeTo(null); // Centraliza a janela na tela
    }

    /**
     * Alterna a visualização para a tela de listagem de produtos.
     */
    public void mostrarLista() {
        cardLayout.show(painelPrincipal, TELA_LISTA);
    }

    /**
     * Alterna a visualização para a tela de formulário (cadastro/edição).
     */
    public void mostrarFormulario() {
        cardLayout.show(painelPrincipal, TELA_FORMULARIO);
    }

    public ProdutoListView getListView() {
        return listView;
    }

    public ProdutoFormView getFormView() {
        return formView;
    }
}
