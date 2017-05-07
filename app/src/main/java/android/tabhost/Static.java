package android.tabhost;

/**
 * Created by Guilherme on 14/05/2016.
 */
public class Static
{

    static int id=0;
    static int idevento=0;
    static int idESTADO=0;
    static String ufESTADO;
    static String nomeCIDADE;
    static boolean face;
    static String latitude,longitude;
    static String Estado,Cidade;



    public static String getNomeCIDADE() {
        return nomeCIDADE;
    }

    public static void setNomeCIDADE(String nomeCIDADE) {
        Static.nomeCIDADE = nomeCIDADE;
    }

    public static String getUfESTADO() {
        return ufESTADO;
    }

    public static void setUfESTADO(String ufESTADO) {
        Static.ufESTADO = ufESTADO;
    }

    public static int getIdESTADO() {
        return idESTADO;
    }

    public static void setIdESTADO(int idESTADO) {
        Static.idESTADO = idESTADO;
    }

    public static String getEstado() {
        return Estado;
    }

    public static void setEstado(String estado) {
        Estado = estado;
    }

    public static String getCidade() {
        return Cidade;
    }

    public static void setCidade(String cidade) {
        Cidade = cidade;
    }



    public static int getIdevento() {return idevento;}
    public static void setIdevento(int idevento) {Static.idevento = idevento;}

    public static String getLatitude() {return latitude;}
    public static void setLatitude(String latitude) {Static.latitude = latitude;}

    public static String getLongitude() {return longitude; }
    public static void setLongitude(String longitude) {Static.longitude = longitude;}


    public static boolean isFace() {return face;}
    public static void setFace(boolean face) {Static.face = face;}


    public static int getId() {return id;}
    public static void setId(int id) {Static.id = id;}




    //region FACEBOOK
    static String nome;
    static String email;
    static String idface;

//endregion

}
