package compadres.burgueria.app.compadres;

/**
 * Created by PEDRONETO on 20/06/2017.
 */

public class Hamburguer {
    private String nome;
    private String descricao;
    private int imagem; // vai armazenar o identificador do recurso

    public Hamburguer(String nome, String descricao, int imagem){
        this.nome = nome;
        this.descricao = descricao;
        this.imagem = imagem;
    }

    public String getNome() {
        return nome;
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
