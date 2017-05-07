package android.tabhost.adapters;

public class GetDataAdapter
{

    public String Id_evento;
    public String HoraImage;
    public String ImageServerUrl;
    public String ImageTitleName;
    public String DataImage;
    public String LongitudeEvento;
    public String LatitudeEvento;
    public String Distancia;
    public String Preco;
    //region GETTERS AND SETTERS
    public String getPreco() {
        return Preco;
    }

    public void setPreco(String preco) {
        Preco = preco;
    }



    public String getDistancia() {
        return Distancia;
    }

    public void setDistancia(String distancia) {
        Distancia = distancia;
    }




    public String getLatitudeEvento() {
    return LatitudeEvento;
}

    public void setLatitudeEvento(String latitudeEvento) {
        LatitudeEvento = latitudeEvento;
    }



    public String getLongitudeEvento() {
        return LongitudeEvento;
    }

    public void setLongitudeEvento(String longitudeEvento) {
        LongitudeEvento = longitudeEvento;
    }

    public String getHoraImage() {
        return HoraImage;
    }

    public void setHoraImage(String horaImage) {
        HoraImage = horaImage;
    }
    public String getDataImage() {
        return DataImage;
    }

    public void setDataImage(String dataImage) {
        DataImage = dataImage;
    }


    public String getImageServerUrl() {
        return ImageServerUrl;
    }

    public void setImageServerUrl(String imageServerUrl) {
        this.ImageServerUrl = imageServerUrl;
    }

    public String getImageTitleName() {
        return ImageTitleName;
    }

    public void setImageTitleNamee(String Imagetitlename) {
        this.ImageTitleName = Imagetitlename;
    }

    public String getId_evento() {
        return Id_evento;
    }

        public void setId_evento(String id_evento) {
        this.Id_evento = id_evento;
    }
//endregion
}