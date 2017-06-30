package compadres.burgueria.app.compadres;

/**
 * Created by Pedro Neto on 30/06/2017.
 */

public class Produto {
    private String nome;
    private String tipo;
    private String descricao;
    private float preco;
    private int imagem; // vai armazenar o identificador do recurso

    public Produto(String nome, String tipo, String descricao,float preco,int imagem){
        this.nome = nome;
        this.tipo = tipo;
        this.descricao = descricao;
        this.preco = preco;
        this.imagem = imagem;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public float getPreco() {
        return preco;
    }

    public void setPreco(float preco) {
        this.preco = preco;
    }

    public int getImagem() {
        return imagem;
    }

    public void setImagem(int imagem) {
        this.imagem = imagem;
    }
}
