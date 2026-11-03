package pagamento.template;

import pagamento.dominio.FormaPagamento;
import pagamento.dominio.Pedido;
import pagamento.dominio.StatusTransacao;

import java.math.BigDecimal;

public class ProcessadorPix extends ProcessadorPagamento {

    @Override
    protected BigDecimal calcularTaxa(BigDecimal valor) {
        return BigDecimal.ZERO;
    }

    @Override
    protected StatusTransacao autorizar(Pedido pedido, BigDecimal valorFinal) {
        return StatusTransacao.APROVADA;
    }

    @Override
    protected FormaPagamento getFormaPagamento() {
        return FormaPagamento.PIX;
    }
}
