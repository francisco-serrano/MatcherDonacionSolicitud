package aplicacion.model.lematizador;

import java.sql.*;

public class RepositorioLemas {
    private Connection connection;
    private String dbDir;

    private static final String queryBajaTabla = "drop table if exists lematizaciones;";
    private static final String queryAltaTabla = "create table lematizaciones (\n"
            + "	lema text,\n"
            + "	palabra text primary key\n"
            + ");";
    private static final String queryBajaIndice = "drop index if exists idx_lemas;";
    private static final String queryAltaIndice = "create index idx_lemas on lematizaciones(palabra)";
    private static final String queryAltaFila = "insert or replace into lematizaciones (lema, palabra) values ('%s', '%s');";
    private static final String queryConsulta = "select *\n" +
            "from lematizaciones\n" +
            "where palabra = '%s'";

    public void crearTabla() {
        try {
            PreparedStatement statement = connection.prepareStatement(queryBajaTabla);
            statement.execute();

            statement = connection.prepareStatement(queryAltaTabla);
            statement.execute();

            statement = connection.prepareStatement(queryBajaIndice);
            statement.execute();

            statement = connection.prepareStatement(queryAltaIndice);
            statement.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void cargarTabla(String linea) {
        String[] aux = linea.split("\t");

        try {
            PreparedStatement statement = connection.prepareStatement(String.format(queryAltaFila, aux[0], aux[1]));
            statement.execute();
        } catch (SQLException e) {
            System.out.printf("%s %s\n", aux[0], aux[1]);
            e.printStackTrace();
        }
    }

    public String getLema(String palabra) {
        try {
            PreparedStatement statement = connection.prepareStatement(String.format(queryConsulta, palabra));
            ResultSet resultSet = statement.executeQuery();

            if (!resultSet.next())
                return palabra;

            return resultSet.getString("lema");
        } catch (SQLException e) {
            e.printStackTrace();
        }

        throw new RuntimeException("wololooo");
    }

    public RepositorioLemas(String dbDir) {
        this.dbDir = dbDir;
    }

    public void connect() {
        try {
            connection = DriverManager.getConnection(String.format("jdbc:sqlite::resource:%s", dbDir));
            connection.setAutoCommit(true);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void disconnect() {
        try {
            if (connection != null)
                connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}
