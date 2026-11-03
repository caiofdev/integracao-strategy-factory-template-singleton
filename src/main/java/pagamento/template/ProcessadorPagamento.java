package pagamento.template;

import pagamento.dominio.FormaPagamento;
import pagamento.dominio.Pedido;
import pagamento.dominio.StatusTransacao;
import pagamento.dominio.Transacao;
import pagamento.singleton.RegistroTransacoes;

import java.math.BigDecimal;

/**
 * Template Method: define o esqueleto do processamento de pagamento.
 * As subclasses implementam apenas os passos variáveis.
 */
public abstract class ProcessadorPagamento {

    /** Método template — final para que a ordem dos passos não seja alterada. */
    public final Transacao processar(Pedido pedido) {
        validar(pedido);
        BigDecimal valorComDesconto = pedido.calcularValorComDesconto();
        BigDecimal valorFinal = valorComDesconto.add(calcularTaxa(valorComDesconto));
        StatusTransacao status = autorizar(pedido, valorFinal);

        Transacao transacao = new Transacao(pedido, getFormaPagamento(), valorFinal, status);
        RegistroTransacoes.getInstancia().registrar(transacao);
        aposProcessar(transacao);
        return transacao;
    }

    protected void validar(Pedido pedido) {
        if (pedido.getValor() == null || pedido.getValor().signum() <= 0) {
            throw new IllegalArgumentException("Valor do pedido deve ser positivo");
        }
    }

    protected abstract BigDecimal calcularTaxa(BigDecimal valor);

    protected abstract StatusTransacao autorizar(Pedido pedido, BigDecimal valorFinal);

    protected abstract FormaPagamento getFormaPagamento();

    /** Hook opcional executado ao final do processamento. */
    protected void aposProcessar(Transacao transacao) {
    }
}
