package br.gov.sp.cps.fatec.pdv;

import br.gov.sp.cps.fatec.pdv.controller.ProdutoController;
import br.gov.sp.cps.fatec.pdv.model.entity.Produto;
import br.gov.sp.cps.fatec.pdv.model.repository.ProdutoRepository;
import br.gov.sp.cps.fatec.pdv.model.repository.ProdutoRepositoryImpl;
import br.gov.sp.cps.fatec.pdv.model.service.ProdutoService;
import br.gov.sp.cps.fatec.pdv.util.JPAUtil;
import br.gov.sp.cps.fatec.pdv.view.MainFrame;
import com.formdev.flatlaf.FlatLightLaf;

import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.math.BigDecimal;

/**
 * Ponto de Entrada Principal (Bootstrap) da Aplicação Desktop PDV-Swing-JPA.
 * <p>
 * <b>Conceito Pedagógico (Inversão de Controle & Thread Safety no Swing):</b>
 * <ul>
 * <li><b>Look & Feel Moderno:</b> Configura o tema FlatLaf para uma interface
 * moderna,
 * elegante e profissional, com fallback transparente para o tema nativo do
 * Sistema Operacional.</li>
 * <li><b>Event Dispatch Thread (EDT):</b> Toda criação e manipulação de
 * componentes gráficos
 * Swing deve ser executada obrigatoriamente dentro do método
 * {@link SwingUtilities#invokeLater(Runnable)},
 * garantindo segurança concorrente (Single-Thread Rule do Swing).</li>
 * <li><b>Injeção de Dependências Manual (Pure DI):</b> A classe {@code Main}
 * atua como
 * o <i>Composition Root</i>, instanciando o Repositório, o Serviço, a Visão e o
 * Controlador,
 * injetando as dependências de forma limpa sem a complexidade de frameworks
 * externos.</li>
 * <li><b>Ciclo de Vida Limpo:</b> Registra um
 * {@link java.awt.event.WindowListener} para encerrar
 * a conexão com o banco de dados H2 e o
 * {@link jakarta.persistence.EntityManagerFactory}
 * quando a janela for fechada pelo usuário.</li>
 * </ul>
 *
 * @author Cainã Antunes Silva
 */
public class Main {

    public static void main(String[] args) {
        // 1. Configuração do Look and Feel (FlatLaf Moderno ou Nativo do S.O.)
        configurarLookAndFeel();

        // 2. Inicialização da Interface Gráfica na Event Dispatch Thread (EDT)
        SwingUtilities.invokeLater(() -> {
            try {
                // Instanciação da camada de persistência (JPA / Hibernate / H2)
                ProdutoRepository repository = new ProdutoRepositoryImpl();

                // Carga inicial didática de dados caso o banco esteja vazio
                inicializarDadosDemonstracao(repository);

                // Instanciação da camada de serviço de negócio e do Observer
                ProdutoService service = new ProdutoService(repository);

                // Procura e aplica o tema Nimbus entre os temas instalados no Java
                for (UIManager.LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
                    if ("Nimbus".equals(info.getName())) {
                        UIManager.setLookAndFeel(info.getClassName());
                        break;
                    }
                }

                // Instanciação da Janela Principal (View com CardLayout)
                MainFrame mainFrame = new MainFrame();

                // Instanciação do Controlador conectando View e Model (MVC)
                ProdutoController controller = new ProdutoController(mainFrame, service);
                controller.inicializar();

                // Gerenciamento de encerramento de conexões
                mainFrame.addWindowListener(new WindowAdapter() {
                    @Override
                    public void windowClosing(WindowEvent e) {
                        System.out.println("[PDV-Swing] Encerrando recursos de banco de dados JPA/H2...");
                        JPAUtil.close();
                    }
                });

                // Exibe a janela para o usuário
                mainFrame.setVisible(true);

            } catch (Exception e) {
                System.err.println("[PDV-Swing] Erro fatal durante a inicialização da aplicação: " + e.getMessage());
                e.printStackTrace();
            }
        });
    }

    /**
     * Aplica o tema visual FlatLaf ou o Look and Feel do sistema operacional.
     */
    private static void configurarLookAndFeel() {
        try {
            FlatLightLaf.setup();
        } catch (Throwable t) {
            try {
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            } catch (Exception ex) {
                System.err.println("Não foi possível configurar Look and Feel do sistema: " + ex.getMessage());
            }
        }
    }

    /**
     * Popula o banco de dados com produtos de demonstração pedagógica se a base
     * estiver vazia.
     *
     * @param repository Repositório de produtos.
     */
    private static void inicializarDadosDemonstracao(ProdutoRepository repository) {
        try {
            if (repository.listarTodos().isEmpty()) {
                System.out.println("[PDV-Swing] Base vazia detectada. Inserindo dados de demonstração didática...");
                repository.salvar(new Produto("7891000100011", "Café Gourmet Especial Torrado 500g",
                        new BigDecimal("24.90"), 35));
                repository.salvar(
                        new Produto("7891000200022", "Açúcar Demerara Orgânico 1kg", new BigDecimal("6.50"), 80));
                repository.salvar(
                        new Produto("7891000300033", "Arroz Nobre Agulhinha Tipo 1 5kg", new BigDecimal("32.90"), 50));
                repository.salvar(
                        new Produto("7891000400044", "Feijão Carioca Selecionado 1kg", new BigDecimal("8.75"), 60));
                repository.salvar(new Produto("7891000500055", "Azeite de Oliva Extra Virgem 500ml",
                        new BigDecimal("41.50"), 25));
                System.out.println("[PDV-Swing] Carga inicial de demonstração concluída com sucesso!");
            }
        } catch (Exception e) {
            System.err.println("[PDV-Swing] Aviso ao popular dados de demonstração: " + e.getMessage());
        }
    }
}
