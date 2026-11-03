package pagamento.estrategia;

import pagamento.dominio.Pedido;

import java.math.BigDecimal;
import java.math.RoundingMode;

/** 15% para clientes VIP; demais clientes não recebem desconto. */
public class DescontoClienteVip implements EstrategiaDesconto {
    private static final BigDecimal PERCENTUAL_VIP = new BigDecimal("0.15");

    @Override
    public BigDecimal calcularDesconto(Pedido pedido) {
        if (!pedido.getCliente().isVip()) {
            return BigDecimal.ZERO;
        }
        return pedido.getValor().multiply(PERCENTUAL_VIP).setScale(2, RoundingMode.HALF_UP);
    }
}
