package pagamento;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.NullSource;
import org.junit.jupiter.params.provider.ValueSource;
import pagamento.dominio.Cliente;
import pagamento.dominio.FormaPagamento;
import pagamento.dominio.Pedido;
import pagamento.dominio.StatusTransacao;
import pagamento.dominio.Transacao;
import pagamento.estrategia.DescontoClienteVip;
import pagamento.estrategia.DescontoPercentual;
import pagamento.fabrica.FabricaBoleto;
import pagamento.fabrica.FabricaCartao;
import pagamento.fabrica.FabricaPix;
import pagamento.fabrica.FabricaProcessador;
import pagamento.singleton.RegistroTransacoes;
import pagamento.template.ProcessadorBoleto;
import pagamento.template.ProcessadorCartao;
import pagamento.template.ProcessadorPix;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;

class PagamentoTest {

    private static Pedido pedido(String documento, boolean vip, String valor) {
        return new Pedido("P1", new Cliente("Ana", documento, vip), valor == null ? null : new BigDecimal(valor));
    }

    private static Pedido pedido(String valor) {
        return pedido("123", false, valor);
    }

    @Nested
    class Strategy {

        @Test
        void aplicaEstrategiaDefinida() {
            Pedido pedido = pedido("100.00");
            pedido.setEstrategiaDesconto(new DescontoPercentual(BigDecimal.TEN));

            assertEquals(new BigDecimal("90.00"), pedido.calcularValorComDesconto());
        }

        @Test
        void rejeitaEstrategiaNula() {
            assertThrows(NullPointerException.class, () -> pedido("100.00").setEstrategiaDesconto(null));
        }

        @ParameterizedTest
        @ValueSource(strings = {"0", "100"})
        void aceitaPercentualNosLimites(String percentual) {
            assertDoesNotThrow(() -> new DescontoPercentual(new BigDecimal(percentual)));
        }

        @ParameterizedTest
        @ValueSource(strings = {"-0.01", "100.01"})
        void rejeitaPercentualForaDosLimites(String percentual) {
            assertThrows(IllegalArgumentException.class, () -> new DescontoPercentual(new BigDecimal(percentual)));
        }

        @ParameterizedTest
        @CsvSource({"false, 0", "true, 150.00"})
        void descontoVipDependeDoCliente(boolean vip, String esperado) {
            Pedido pedido = pedido("123", vip, "1000.00");

            assertEquals(new BigDecimal(esperado), new DescontoClienteVip().calcularDesconto(pedido));
        }
    }

    @Nested
    class TemplateMethod {

        private final RegistroTransacoes registro = RegistroTransacoes.getInstancia();

        @Test
        void rejeitaValorNulo() {
            assertThrows(IllegalArgumentException.class, () -> new ProcessadorPix().processar(pedido(null)));
        }

        @Test
        void rejeitaValorZeroSemRegistrar() {
            int antes = registro.listar().size();

            assertThrows(IllegalArgumentException.class, () -> new ProcessadorPix().processar(pedido("0.00")));
            assertEquals(antes, registro.listar().size());
        }

        @Test
        void processaMenorValorPositivoERegistra() {
            Transacao transacao = new ProcessadorPix().processar(pedido("0.01"));

            List<Transacao> transacoes = registro.listar();
            assertSame(transacao, transacoes.get(transacoes.size() - 1));
        }

        @ParameterizedTest
        @CsvSource({"4830.91, APROVADA", "4830.92, APROVADA", "4830.93, RECUSADA"})
        void cartaoAutorizaAteOLimiteComTaxa(String valor, StatusTransacao esperado) {
            assertEquals(esperado, new ProcessadorCartao().processar(pedido(valor)).getStatus());
        }

        @ParameterizedTest
        @NullSource
        @ValueSource(strings = " ")
        void boletoRejeitaDocumentoAusente(String documento) {
            assertThrows(IllegalArgumentException.class,
                    () -> new ProcessadorBoleto().processar(pedido(documento, false, "100.00")));
        }

        @Test
        void boletoFicaPendenteComTarifa() {
            Transacao transacao = new ProcessadorBoleto().processar(pedido("100.00"));

            assertEquals(new BigDecimal("103.50"), transacao.getValorFinal());
            assertEquals(StatusTransacao.PENDENTE, transacao.getStatus());
        }
    }

    @Nested
    class FactoryMethod {

        static Stream<Arguments> fabricas() {
            return Stream.of(
                    Arguments.of(new FabricaCartao(), FormaPagamento.CARTAO),
                    Arguments.of(new FabricaPix(), FormaPagamento.PIX),
                    Arguments.of(new FabricaBoleto(), FormaPagamento.BOLETO)
            );
        }

        @ParameterizedTest
        @MethodSource("fabricas")
        void pagaComProcessadorCorreto(FabricaProcessador fabrica, FormaPagamento esperado) {
            assertEquals(esperado, fabrica.pagar(pedido("10.00")).getFormaPagamento());
        }
    }

    @Nested
    class Singleton {

        @Test
        void retornaSempreAMesmaInstancia() {
            assertSame(RegistroTransacoes.getInstancia(), RegistroTransacoes.getInstancia());
        }

        @Test
        void listarRetornaCopiaImutavel() {
            assertThrows(UnsupportedOperationException.class,
                    () -> RegistroTransacoes.getInstancia().listar().add(null));
        }
    }
}
