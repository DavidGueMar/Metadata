
import javax.xml.crypto.Data;
import javax.xml.transform.Result;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Scanner;

public class main {
    public static void main(String[] args) {

        Connection connection = null;
        //Preparamos la conexión
        try {
            //Cargamos los drivers de mysql
            Class.forName("com.mysql.jdbc.Driver");

            //Establecemos la conexión
            connection = DriverManager.getConnection("jdbc:mysql://iescierva.net:9305?useSSL=false", "lector",
                    "lector");
            System.out.println("Conexión establecida");

            //Empezamos la ejecución del programa
            System.out.println("Introduce el nombre de una tabla a buscar dentro de los catálogos-esquemas");
            //Estas trés líneas de código son para guardar en la varibable nombreTabla
            // lo que introduzca por pantalla y pasarlo a string y minúsculas
            String nombreTabla;
            Scanner sc = new Scanner(System.in);
            nombreTabla = sc.next().trim().toLowerCase();

            // obtenemos los nombres de los catalogos de la base de datos
            DatabaseMetaData dbmd = connection.getMetaData();
            ResultSet rs = dbmd.getCatalogs();

            List<String> nombresEsquemas = new ArrayList<>();

            System.out.println("Esquemas que contienen la tabla '" + nombreTabla + "'");
            while (rs.next()) {
                String nombreEsquema = rs.getString("TABLE_CAT");

                // Si existen, se obtienen las tablas pertenecientes el esquema actual
                ResultSet rsTablas = dbmd.getTables(nombreEsquema, null, nombreTabla, new String[]{"TABLE"});

                while (rsTablas.next()) {
                    // Se añade a la lista para más adelante y se imprime el nombre del esquema

                    nombresEsquemas.add(nombreEsquema);
                    System.out.println(nombreEsquema);

                }
            }


            // Ejercicio 2 - A
            //	            String esquema = null;
            //	            do {
            //	                System.out.println("Introduce uno de los esquemas anteriores:");
            //	                esquema = sc.next();
            //	            } while (!nombresEsquemas.contains(esquema) || !esquema.equals("all") );
            //
            //	            boolean buscamosTodos = esquema.equals("all");
            //
            //	            if (buscamosTodos){
            //	                for (String s: nombresEsquemas) {
            //	                    //Recuperar tablas del esquema
            //
            //	                    //Mostrar informacion: esquema, pks...
            //	                }
            //
            //	            } else {
            //	                ResultSet rsTablas = dbmd.getTables(esquema, null, null, new String[]{"TABLE"});
            //	                while (rsTablas.next()){
            //	                    //Mostrar informacion: esquema, pks..
            //
            //	                }
            //
            //	            }

            //Ejercicio 2 -B

            System.out.println("Pulse la tecla de la opción elegida:\n"
                    + "1. Mostrar todos los catálogos\n"
                    + "2. Introducir un catálogo específico\n");
            String opcion = new Scanner(System.in).nextLine();
            String catalogo = null;
            ResultSet rsc = null;
            switch (opcion) {
                case "1":
                    ResultSet resultSetCat = dbmd.getCatalogs();
                    ResultSet resultSetTables = null;
                    while (resultSetCat.next()) {
                        //System.out.println("Catalogo: " + resultSet.getString("TABLE_CAT"));
                        resultSetTables = dbmd.getTables(resultSetCat.getString("TABLE_CAT"), null, nombreTabla, null);

                        while (resultSetTables.next()) {

                            System.out.println("Catalogo: " + resultSetCat.getString("TABLE_CAT"));
                            System.out.println("Tabla: " + resultSetTables.getString("TABLE_NAME"));

                            ResultSet resultSetPKey = dbmd.getPrimaryKeys(resultSetCat.getString("TABLE_CAT"), resultSetCat.getString("TABLE_CAT"), resultSetTables.getString("TABLE_NAME"));
                            rsc = dbmd.getColumns(resultSetCat.getString("TABLE_CAT"), null, nombreTabla, null);
                            while (rsc.next()) {
                                System.out.print("\nCampo: " + rsc.getString("COLUMN_NAME") + "  |  Tipo de dato: " + rsc.getString("TYPE_NAME") + "  |  Tamaño: " + rsc.getString("COLUMN_SIZE"));

                            }
                            while (resultSetPKey.next()) {
                                System.out.println("\nPrimary key: " + resultSetPKey.getString("COLUMN_NAME"));
                            }
                        }
                    }
                    resultSetTables.close();
                    resultSetCat.close();

                    break;
                case "2":
                    System.out.println("Introduce el catálogo que quieres mostrar\n");
                    catalogo = new Scanner(System.in).nextLine();
                    rsc = dbmd.getColumns(catalogo, null, nombreTabla, null);
                    ResultSet resultSetPK = null;
                    System.out.println("Catalogo: " + catalogo + "\n" +
                            "------Tabla: " + nombreTabla);
                    while (rsc.next()) {
                        System.out.print("\nCampo: " + rsc.getString("COLUMN_NAME") + "  |  Tipo de dato: " + rsc.getString("TYPE_NAME") + "  |  Tamaño: " + rsc.getString("COLUMN_SIZE"));
                        resultSetPK = dbmd.getPrimaryKeys(rsc.getString("TABLE_CAT"), rsc.getString("TABLE_CAT"), rsc.getString("TABLE_NAME"));
                    }
                    while (resultSetPK.next()) {
                        System.out.println("\nPrimary key: " + resultSetPK.getString("COLUMN_NAME"));
                    }

                    break;

                default:
                    System.out.println("No pertenece a ninguna opción de modo que se mostrarán todos\n");
                    break;
            }

            rsc.close();


        } catch (SQLException |
                ClassNotFoundException e) {
            e.printStackTrace();

        } finally {
            if (connection != null) {
                //closing connection
                try {
                    connection.close();
                } catch (SQLException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            } // if condition
        }// finally


    }
}