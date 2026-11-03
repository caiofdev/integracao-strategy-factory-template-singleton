package pagamento.estrategia;

import pagamento.dominio.Pedido;

import java.math.BigDecimal;

public class SemDesconto implements EstrategiaDesconto {
    @Override
    public BigDecimal calcularDesconto(Pedido pedido) {
        return BigDecimal.ZERO;
    }
}
