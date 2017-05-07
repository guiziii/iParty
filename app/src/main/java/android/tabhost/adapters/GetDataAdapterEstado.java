package android.tabhost.adapters;

/**
 * Created by Guilherme on 28/01/2017.
 */
public class GetDataAdapterEstado
{
    private String IdEst;
    private String nome;
    private String uf;
    private String pais;

    public String getIdEst() {
        return IdEst;
    }

    public void setIdEst(String idEst) {
        IdEst = idEst;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getUf() {
        return uf;
    }

    public void setUf(String uf) {
        this.uf = uf;
    }

    public String getPais() {
        return pais;
    }

    public void setPais(String pais) {
        this.pais = pais;
    }
}
