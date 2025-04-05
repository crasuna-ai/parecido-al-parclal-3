


el Departamentosistemas necesita tener un control de Inventario sobre los dispositivos electronicos para el prestamo
los pcs tienen atributos serial, marca, memoria ram, disco duro, precio, nombre estudiante, carnet, disponible.
las tablets tienen atributos serial, tamaño, marca, precio, nombre estudiante, disponible.

cree una solucion para este tema teber en cuenta que se pueden agregar dispositivos, prestar, devolver, y mostrar inventario y permitir
import javax.swing.*;
import java.io.*;
import java.util.LinkedList;
import java.util.Queue;

abstract class Dispositivo implements Serializable {
    protected String serial, marca, nombreEstudiante;
    protected double precio;
    protected boolean disponible;

    public Dispositivo(String serial, String marca, double precio) {
        this.serial = serial;
        this.marca = marca;
        this.precio = precio;
        this.disponible = true;
        this.nombreEstudiante = "";
    }

    public void prestar(String nombreEstudiante) {
        if (disponible) {
            this.nombreEstudiante = nombreEstudiante;
            this.disponible = false;
        }
    }

    public void devolver() {
        if (!disponible) {
            this.nombreEstudiante = "";
            this.disponible = true;
        }
    }

    public void editar() {
        this.marca = JOptionPane.showInputDialog("Ingrese la nueva marca:", this.marca);
        this.precio = solicitarPrecio();
    }

    protected double solicitarPrecio() {
        while (true) {
            try {
                return Double.parseDouble(JOptionPane.showInputDialog("Ingrese el nuevo precio:", this.precio));
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(null, "Por favor, ingrese un número válido.");
            }
        }
    }

    public abstract String mostrarInfo();
}

class PC extends Dispositivo {
    private String memoriaRAM, discoDuro;

    public PC(String serial, String marca, String memoriaRAM, String discoDuro, double precio) {
        super(serial, marca, precio);
        this.memoriaRAM = memoriaRAM;
        this.discoDuro = discoDuro;
    }

    @Override
    public void editar() {
        super.editar();
        this.memoriaRAM = JOptionPane.showInputDialog("Ingrese la nueva memoria RAM:", this.memoriaRAM);
        this.discoDuro = JOptionPane.showInputDialog("Ingrese el nuevo disco duro:", this.discoDuro);
    }

    @Override
    public String mostrarInfo() {
        return "PC - Serial: " + serial + "\nMarca: " + marca + "\nRAM: " + memoriaRAM + "\nDisco Duro: " + discoDuro +
                "\nPrecio: " + precio + "\nDisponible: " + (disponible ? "Sí" : "No") +
                (disponible ? "" : "\nPrestado a: " + nombreEstudiante);
    }
}

class Tablet extends Dispositivo {
    private String tamaño;

    public Tablet(String serial, String marca, String tamaño, double precio) {
        super(serial, marca, precio);
        this.tamaño = tamaño;
    }

    @Override
    public void editar() {
        super.editar();
        this.tamaño = JOptionPane.showInputDialog("Ingrese el nuevo tamaño:", this.tamaño);
    }

    @Override
    public String mostrarInfo() {
        return "Tablet - Serial: " + serial + "\nMarca: " + marca + "\nTamaño: " + tamaño +
                "\nPrecio: " + precio + "\nDisponible: " + (disponible ? "Sí" : "No") +
                (disponible ? "" : "\nPrestado a: " + nombreEstudiante);
    }
}

class Inventario {
    private Queue<Dispositivo> dispositivos;
    private final String archivo = "inventario.dat";

    public Inventario() {
        dispositivos = cargarInventario();
    }

    public void agregarDispositivo(Dispositivo dispositivo) {
        dispositivos.add(dispositivo);
        guardarInventario();
        JOptionPane.showMessageDialog(null, "Dispositivo agregado: " + dispositivo.serial);
    }

    public void mostrarInventario() {
        if (dispositivos.isEmpty()) {
            JOptionPane.showMessageDialog(null, "El inventario está vacío.");
            return;
        }
        StringBuilder inventarioTexto = new StringBuilder("Inventario:\n");
        for (Dispositivo d : dispositivos) {
            inventarioTexto.append(d.mostrarInfo()).append("\n---------------------\n");
        }
        JOptionPane.showMessageDialog(null, inventarioTexto.toString());
    }

    public void prestarDispositivo() {
        Dispositivo dispositivo = seleccionarDispositivo("Seleccione el dispositivo a prestar:");
        if (dispositivo != null && dispositivo.disponible) {
            String nombreEstudiante = JOptionPane.showInputDialog("Ingrese el nombre del estudiante:");
            dispositivo.prestar(nombreEstudiante);
            guardarInventario();
            JOptionPane.showMessageDialog(null, "Dispositivo prestado.");
        }
    }

    public void devolverDispositivo() {
        Dispositivo dispositivo = seleccionarDispositivo("Seleccione el dispositivo a devolver:");
        if (dispositivo != null && !dispositivo.disponible) {
            dispositivo.devolver();
            guardarInventario();
            JOptionPane.showMessageDialog(null, "Dispositivo devuelto.");
        }
    }

    public void editarDispositivo() {
        Dispositivo dispositivo = seleccionarDispositivo("Seleccione el dispositivo a editar:");
        if (dispositivo != null) {
            dispositivo.editar();
            guardarInventario();
            JOptionPane.showMessageDialog(null, "Dispositivo actualizado.");
        }
    }

    private Dispositivo seleccionarDispositivo(String mensaje) {
        if (dispositivos.isEmpty()) {
            JOptionPane.showMessageDialog(null, "No hay dispositivos disponibles.");
            return null;
        }
        String[] opciones = new String[dispositivos.size()];
        int i = 0;
        for (Dispositivo d : dispositivos) {
            opciones[i++] = d.serial + " - " + d.marca;
        }
        String seleccion = (String) JOptionPane.showInputDialog(null, mensaje, "Seleccionar Dispositivo",
                JOptionPane.QUESTION_MESSAGE, null, opciones, opciones[0]);
        if (seleccion == null) return null;
        for (Dispositivo d : dispositivos) {
            if (seleccion.startsWith(d.serial)) {
                return d;
            }
        }
        return null;
    }

    private void guardarInventario() {
        try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(archivo))) {
            out.writeObject(dispositivos);
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "Error al guardar el inventario: " + e.getMessage());
        }
    }

    private Queue<Dispositivo> cargarInventario() {
        try (ObjectInputStream in = new ObjectInputStream(new FileInputStream(archivo))) {
            return (Queue<Dispositivo>) in.readObject();
        } catch (IOException | ClassNotFoundException e) {
            return new LinkedList<>();
        }
    }
}

public class Main {
    public static void main(String[] args) {
        Inventario inventario = new Inventario();
        boolean salir = false;

        while (!salir) {
            String opcion = JOptionPane.showInputDialog("Menú:\n1. Agregar PC\n2. Agregar Tablet\n3. Mostrar Inventario\n4. Editar Dispositivo\n5. Prestar Dispositivo\n6. Devolver Dispositivo\n7. Salir");

            switch (opcion) {
                case "1":
                    String serialPC = JOptionPane.showInputDialog("Serial:");
                    String marcaPC = JOptionPane.showInputDialog("Marca:");
                    String ram = JOptionPane.showInputDialog("RAM:");
                    String disco = JOptionPane.showInputDialog("Disco Duro:");
                    double precioPC = solicitarPrecio();
                    inventario.agregarDispositivo(new PC(serialPC, marcaPC, ram, disco, precioPC));
                    break;
                case "2":
                    String serialT = JOptionPane.showInputDialog("Serial:");
                    String marcaT = JOptionPane.showInputDialog("Marca:");
                    String tamaño = JOptionPane.showInputDialog("Tamaño:");
                    double precioT = solicitarPrecio();
                    inventario.agregarDispositivo(new Tablet(serialT, marcaT, tamaño, precioT));
                    break;
                case "3": inventario.mostrarInventario(); break;
                case "4": inventario.editarDispositivo(); break;
                case "5": inventario.prestarDispositivo(); break;
                case "6": inventario.devolverDispositivo(); break;
                case "7": salir = true; break;
                default: JOptionPane.showMessageDialog(null, "Opción no válida.");
            }
        }
    }

    private static double solicitarPrecio() {
        while (true) {
            try {
                return Double.parseDouble(JOptionPane.showInputDialog("Ingrese el precio:"));
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(null, "Por favor, ingrese un número válido.");
            }
        }
    }
}
