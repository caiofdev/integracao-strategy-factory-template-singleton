package pagamento.dominio;

public class Cliente {
    private final String nome;
    private final String documento;
    private final boolean vip;

    public Cliente(String nome, String documento, boolean vip) {
        this.nome = nome;
        this.documento = documento;
        this.vip = vip;
    }

    public String getNome() { return nome; }
    public String getDocumento() { return documento; }
    public boolean isVip() { return vip; }
}
