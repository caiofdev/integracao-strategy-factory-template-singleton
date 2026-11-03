package pagamento.template;

import pagamento.dominio.FormaPagamento;
import pagamento.dominio.Pedido;
import pagamento.dominio.StatusTransacao;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class ProcessadorCartao extends ProcessadorPagamento {
    private static final BigDecimal TAXA = new BigDecimal("0.035");
    private static final BigDecimal LIMITE = new BigDecimal("5000");

    @Override
    protected BigDecimal calcularTaxa(BigDecimal valor) {
        return valor.multiply(TAXA).setScale(2, RoundingMode.HALF_UP);
    }

    @Override
    protected StatusTransacao autorizar(Pedido pedido, BigDecimal valorFinal) {
        return valorFinal.compareTo(LIMITE) <= 0 ? StatusTransacao.APROVADA : StatusTransacao.RECUSADA;
    }

    @Override
    protected FormaPagamento getFormaPagamento() {
        return FormaPagamento.CARTAO;
    }
}
