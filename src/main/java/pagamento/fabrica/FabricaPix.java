package pagamento.fabrica;

import pagamento.template.ProcessadorPix;
import pagamento.template.ProcessadorPagamento;

public class FabricaPix extends FabricaProcessador {
    @Override
    protected ProcessadorPagamento criarProcessador() {
        return new ProcessadorPix();
    }
}
