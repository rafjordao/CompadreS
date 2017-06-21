package compadres.burgueria.app.compadres;

/**
 * Created by PEDRONETO on 20/06/2017.
 */

public class Bebida {
    private String nome;
    private String descricao;
    private float preco;
    private int imagem; // vai armazenar o identificador do recurso

    public Bebida(String nome, String descricao, float preco,int imagem){
        this.nome = nome;
        this.descricao = descricao;
        this.preco = preco;
        this.imagem = imagem;
    }

    public String getNome() {
        return nome;
    }

    public float getPreco() {
        return preco;
    }

    public void setPreco(float preco) {
        this.preco = preco;
    }

    public void setNome(String nome) {
        this.nome = nome;

    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public int getImagem() {
        return imagem;
    }

    public void setImagem(int imagem) {
        this.imagem = imagem;
    }
}
