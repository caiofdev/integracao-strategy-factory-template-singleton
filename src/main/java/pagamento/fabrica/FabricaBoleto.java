package pagamento.fabrica;

import pagamento.template.ProcessadorBoleto;
import pagamento.template.ProcessadorPagamento;

public class FabricaBoleto extends FabricaProcessador {
    @Override
    protected ProcessadorPagamento criarProcessador() {
        return new ProcessadorBoleto();
    }
}
