import java.io.*;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class AddressBook {
    //HashMap <Número, Nombre>
    private HashMap<String, String> contacts;
    private final String filename;

    //Formato
    public AddressBook(String filename) {
        this.contacts = new HashMap<>();
        this.filename = filename;
    }

    //Metodo para lectura y escritura

    //Lectura de contactos
    public void load() {
        File file = new File(filename);
        if (!file.exists()) {
            System.out.println("No se encontró un archivo de contactos, realiza la carga de contactos para crearlo");
            return;
        }

        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            contacts.clear(); // Limpiar el mapa actual antes de cargar
            while ((line = br.readLine()) != null) {
                //Revisar que la línea no esté vacía y contenga una coma
                if (!line.trim().isEmpty() && line.contains(",")) {
                    String[] parts = line.split(",", 2);
                    String phone = parts[0].trim();
                    String name = parts[1].trim();
                    contacts.put(phone, name);
                }
            }
            System.out.println("Contactos desde archivo");
        } catch (IOException e) {
            System.out.println("Error al cargar el archivo: " + e.getMessage());
        }
    }

    //Guardado de contactos
    public void save() {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(filename))) {
            for (Map.Entry<String, String> entry : contacts.entrySet()) {
                bw.write(entry.getKey() + "," + entry.getValue());
                bw.newLine();
            }
            System.out.println("Cambios guardados en el archivo correctamente");
        } catch (IOException e) {
            System.out.println("Error al guardar en el archivo: " + e.getMessage());
        }
    }

    //Metodo de agenda

    // list: Muestra los contactos
    public void list() {
        System.out.println("\nContactos:");
        if (contacts.isEmpty()) {
            System.out.println("(La agenda está vacía)");
            return;
        }
        for (Map.Entry<String, String> entry : contacts.entrySet()) {
            System.out.println(entry.getKey() + " : " + entry.getValue());
        }
    }

    // create: Añade un nuevo contacto
    public void create(String phone, String name) {
        if (phone.isEmpty() || name.isEmpty()) {
            System.out.println("Error: El número y el nombre no pueden estar vacíos.");
            return;
        }

        if (contacts.containsKey(phone)) {
            System.out.println("Aviso: El número ya existe. Se actualizará el nombre de " + contacts.get(phone) + " a " + name);
        }

        contacts.put(phone, name);
        System.out.println("Contacto agregado con éxito.");
    }

    // delete: Elimina un contacto por su número
    public void delete(String phone) {
        if (contacts.containsKey(phone)) {
            String removedName = contacts.remove(phone);
            System.out.println("Contacto eliminado: " + removedName + " (" + phone + ")");
        } else {
            System.out.println("Error: El número telefónico \"" + phone + "\" no existe en la agenda.");
        }
    }

    //Menu de agenda
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        // Nombre del archivo CSV donde se guardará la información
        AddressBook agenda = new AddressBook("agenda.CSV");

        // Cargar contactos automáticamente al iniciar el programa
        agenda.load();

        int option = 0;
        do {
            System.out.println("\nMENÚ AGENDA DE CONTACTOS TELEFONICOS");
            System.out.println("1. Mostrar contactos");
            System.out.println("2. Crear contacto");
            System.out.println("3. Borrar contacto");
            System.out.println("4. Guardar cambios");
            System.out.println("5. Salir");
            System.out.print("Seleccione una opción: ");

            try {
                option = Integer.parseInt(scanner.nextLine());
            } catch (NumberFormatException e) {
                System.out.println("Por favor, ingrese un número válido del 1 al 5.");
                continue;
            }

            switch (option) {
                case 1:
                    agenda.list();
                    break;
                case 2:
                    System.out.print("Ingrese el número telefónico: ");
                    String phone = scanner.nextLine().trim();
                    System.out.print("Ingrese el nombre del contacto: ");
                    String name = scanner.nextLine().trim();
                    agenda.create(phone, name);
                    break;
                case 3:
                    System.out.print("Ingrese el número telefónico a eliminar: ");
                    String phoneToDelete = scanner.nextLine().trim();
                    agenda.delete(phoneToDelete);
                    break;
                case 4:
                    agenda.save();
                    break;
                case 5:
                    // Funcionalidad Extra: Guardado automático preventivo al salir
                    System.out.println("Guardando datos automáticamente antes de salir...");
                    agenda.save();
                    System.out.println("¡Gracias por usar la Agenda Telefónica! Saliendo...");
                    break;
                default:
                    System.out.println("Opción no válida. Intente de nuevo.");
            }
        } while (option != 5);

        scanner.close();
    }
}