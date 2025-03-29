class Dispositivo{
    protected String serial;
    protected String marca;
    protected Double precio;
    protected String nombreestu;
    protected Boolean disponible;
    public Dispositivo(String serial, String marca, Double precio) {
        this.serial = serial;
        this.marca = marca;
        this.precio = precio;
        this.nombreestu = null;
        this.disponible = true;
    }
    public void prestar(String nombreestu){
        if(disponible){
            this.nombreestu = nombreestu;
            this.disponible = false;
            System.out.println("dispositivo prestado a: " + nombreestu);
        } else {
            System.out.println("el dispositivo ya esta en uso");
        }

    }

    public void devolver(){
        if(!disponible) {
        System.out.println("el dispositivo fue devuelto por" + nombreestu);
        this.nombreestu = null;
        this.disponible = true;        
    } else {
        System.out.println("el dispositivo esta guardado");
    }
}
public void mostrarinfo(){
    System.out.println("serial" + serial + "Marca" + marca + "Precio" + precio + "Disponible" + (disponible ? "si " : "no ")+
    (disponible ?  "" : ", prestado a" + nombreestu));
}
}

    