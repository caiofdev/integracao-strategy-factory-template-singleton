package pagamento.estrategia;

import pagamento.dominio.Pedido;

import java.math.BigDecimal;

/** Strategy: família de algoritmos de desconto intercambiáveis. */
public interface EstrategiaDesconto {
    BigDecimal calcularDesconto(Pedido pedido);
}
