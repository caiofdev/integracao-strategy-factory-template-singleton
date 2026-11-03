package pagamento.template;

import pagamento.dominio.FormaPagamento;
import pagamento.dominio.Pedido;
import pagamento.dominio.StatusTransacao;
import pagamento.dominio.Transacao;

import java.math.BigDecimal;

public class ProcessadorBoleto extends ProcessadorPagamento {
    private static final BigDecimal TARIFA_EMISSAO = new BigDecimal("3.50");

    @Override
    protected void validar(Pedido pedido) {
        super.validar(pedido);
        if (pedido.getCliente().getDocumento() == null || pedido.getCliente().getDocumento().isBlank()) {
            throw new IllegalArgumentException("Boleto exige documento do cliente");
        }
    }

    @Override
    protected BigDecimal calcularTaxa(BigDecimal valor) {
        return TARIFA_EMISSAO;
    }

    @Override
    protected StatusTransacao autorizar(Pedido pedido, BigDecimal valorFinal) {
        return StatusTransacao.PENDENTE;
    }

    @Override
    protected FormaPagamento getFormaPagamento() {
        return FormaPagamento.BOLETO;
    }

    @Override
    protected void aposProcessar(Transacao transacao) {
        System.out.println("Boleto emitido para o pedido " + transacao.getPedido().getCodigo());
    }
}
