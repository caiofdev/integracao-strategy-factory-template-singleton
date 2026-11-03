package pagamento.dominio;

import pagamento.estrategia.EstrategiaDesconto;
import pagamento.estrategia.SemDesconto;

import java.math.BigDecimal;
import java.util.Objects;

/**
 * Contexto do padrão Strategy: delega o cálculo do desconto
 * para a estratégia configurada.
 */
public class Pedido {
    private final String codigo;
    private final Cliente cliente;
    private final BigDecimal valor;
    private EstrategiaDesconto estrategiaDesconto = new SemDesconto();

    public Pedido(String codigo, Cliente cliente, BigDecimal valor) {
        this.codigo = codigo;
        this.cliente = cliente;
        this.valor = valor;
    }

    public void setEstrategiaDesconto(EstrategiaDesconto estrategiaDesconto) {
        this.estrategiaDesconto = Objects.requireNonNull(estrategiaDesconto);
    }

    public BigDecimal calcularValorComDesconto() {
        return valor.subtract(estrategiaDesconto.calcularDesconto(this));
    }

    public String getCodigo() { return codigo; }
    public Cliente getCliente() { return cliente; }
    public BigDecimal getValor() { return valor; }
}
