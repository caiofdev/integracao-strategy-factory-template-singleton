package pagamento.fabrica;

import pagamento.template.ProcessadorCartao;
import pagamento.template.ProcessadorPagamento;

public class FabricaCartao extends FabricaProcessador {
    @Override
    protected ProcessadorPagamento criarProcessador() {
        return new ProcessadorCartao();
    }
}
