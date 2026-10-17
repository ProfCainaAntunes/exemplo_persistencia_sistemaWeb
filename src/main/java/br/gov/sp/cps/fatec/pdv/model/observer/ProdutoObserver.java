package br.gov.sp.cps.fatec.pdv.model.observer;

import br.gov.sp.cps.fatec.pdv.model.entity.Produto;

import java.util.List;

/**
 * Interface do Padrão de Projeto Observer (GoF - Comportamental).
 * <p>
 * <b>Conceito Pedagógico (Padrão Observer / Publish-Subscribe):</b>
 * <ul>
 * <li>Define uma relação um-para-muitos entre objetos, de forma que quando o
 * estado
 * do sujeito monitorado (Subject/Model/Service) se altera, todos os
 * observadores
 * registrados são notificados e atualizados automaticamente.</li>
 * <li>Promove o <b>baixo acoplamento</b>: a camada de persistência e negócio
 * não precisa
 * conhecer os detalhes de implementação das telas Swing, emitindo apenas o
 * evento de dados modificados.</li>
 * </ul>
 *
 * @author Cainã Antunes Silva
 */
@FunctionalInterface
public interface ProdutoObserver {

    /**
     * Notificação disparada quando ocorre qualquer alteração no repositório de
     * produtos
     * (inserção, atualização ou exclusão bem-sucedida).
     *
     * @param produtos Lista atualizada e sincronizada de todos os produtos ativos.
     */
    void onProdutosChanged(List<Produto> produtos);
}
