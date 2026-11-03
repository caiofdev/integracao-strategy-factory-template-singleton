package pagamento.fabrica;

import pagamento.dominio.Pedido;
import pagamento.dominio.Transacao;
import pagamento.template.ProcessadorPagamento;

/**
 * Factory Method: o Creator declara o método fábrica e o utiliza,
 * sem conhecer qual processador concreto será instanciado.
 */
public abstract class FabricaProcessador {

    protected abstract ProcessadorPagamento criarProcessador();

    public Transacao pagar(Pedido pedido) {
        ProcessadorPagamento processador = criarProcessador();
        return processador.processar(pedido);
    }
}
