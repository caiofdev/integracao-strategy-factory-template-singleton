package pagamento.estrategia;

import pagamento.dominio.Pedido;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class DescontoPercentual implements EstrategiaDesconto {
    private final BigDecimal percentual;

    public DescontoPercentual(BigDecimal percentual) {
        this.percentual = percentual;
    }

    @Override
    public BigDecimal calcularDesconto(Pedido pedido) {
        return pedido.getValor()
                .multiply(percentual)
                .divide(BigDecimal.valueOf(100), 2, RoundingMode.HALF_UP);
    }
}
