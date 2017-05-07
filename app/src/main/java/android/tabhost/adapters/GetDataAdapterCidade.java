package android.tabhost.adapters;

/**
 * Created by Guilherme on 29/01/2017.
 */
public class GetDataAdapterCidade
{
    private String IdCity;
    private String nome;
    private String estado;

    public String getIdCity() {
        return IdCity;
    }

    public void setIdCity(String idCity) {
        IdCity = idCity;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }
}
