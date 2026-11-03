package pagamento.estrategia;

import pagamento.dominio.Pedido;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class DescontoPercentual implements EstrategiaDesconto {
    private static final BigDecimal CEM = BigDecimal.valueOf(100);

    private final BigDecimal percentual;

    public DescontoPercentual(BigDecimal percentual) {
        if (percentual.signum() < 0 || percentual.compareTo(CEM) > 0) {
            throw new IllegalArgumentException("Percentual deve estar entre 0 e 100");
        }
        this.percentual = percentual;
    }

    @Override
    public BigDecimal calcularDesconto(Pedido pedido) {
        return pedido.getValor()
                .multiply(percentual)
                .divide(CEM, 2, RoundingMode.HALF_UP);
    }
}
