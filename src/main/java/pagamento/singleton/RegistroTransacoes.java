package pagamento.singleton;

import pagamento.dominio.Transacao;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Singleton: ponto único de registro das transações processadas.
 * Usa o idiom "Initialization-on-demand holder" (lazy e thread-safe).
 */
public final class RegistroTransacoes {
    private final List<Transacao> transacoes = Collections.synchronizedList(new ArrayList<>());

    private RegistroTransacoes() {
    }

    private static class Holder {
        private static final RegistroTransacoes INSTANCIA = new RegistroTransacoes();
    }

    public static RegistroTransacoes getInstancia() {
        return Holder.INSTANCIA;
    }

    public void registrar(Transacao transacao) {
        transacoes.add(transacao);
    }

    public List<Transacao> listar() {
        synchronized (transacoes) {
            return List.copyOf(transacoes);
        }
    }
}
