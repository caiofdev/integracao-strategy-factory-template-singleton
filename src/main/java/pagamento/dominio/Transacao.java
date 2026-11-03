package pagamento.dominio;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

public class Transacao {
    private final String id = UUID.randomUUID().toString();
    private final LocalDateTime dataHora = LocalDateTime.now();
    private final Pedido pedido;
    private final FormaPagamento formaPagamento;
    private final BigDecimal valorFinal;
    private final StatusTransacao status;

    public Transacao(Pedido pedido, FormaPagamento formaPagamento,
                     BigDecimal valorFinal, StatusTransacao status) {
        this.pedido = pedido;
        this.formaPagamento = formaPagamento;
        this.valorFinal = valorFinal;
        this.status = status;
    }

    public String getId() { return id; }
    public LocalDateTime getDataHora() { return dataHora; }
    public Pedido getPedido() { return pedido; }
    public FormaPagamento getFormaPagamento() { return formaPagamento; }
    public BigDecimal getValorFinal() { return valorFinal; }
    public StatusTransacao getStatus() { return status; }

    @Override
    public String toString() {
        return "Transacao[pedido=%s, forma=%s, valor=%s, status=%s]"
                .formatted(pedido.getCodigo(), formaPagamento, valorFinal, status);
    }
}
