import java.util.LinkedList;
import org.reflections.Reflections;
import java.io.File;

public class DatabaseManager {
    private LinkedList<Repository<?>> repositories;
    private LinkedList<TableManager<?>> tables;
    public final static String tableDirectory = "Tables";

    public DatabaseManager() {
        repositories = new LinkedList<>();
        tables = new LinkedList<>();
        for (Class<?> c : readRepositories()) {
            TableManager<?> t = new TableManager<>(c, this);
            tables.add(t);
            repositories.add(new Repository(c, t));
        }

        File tableDir = new File(tableDirectory);
        if (!tableDir.exists()) {
            tableDir.mkdir();
        }
    }

    public <T> Repository<T> getRepository(Class<T> c) throws ClassNotFoundException {
        for (Repository r : repositories) {
            if (r.getClassType().equals(c)) {
                return r;
            }
        }
        throw new ClassNotFoundException(
                "Table class of type '" + c.getSimpleName() + "' not found! Make sure it has Table annotation");
    }

    public LinkedList<Class<?>> readRepositories() {
        LinkedList<Class<?>> ret = new LinkedList<>();
        Reflections reflections = new Reflections("");

        for (Class<?> c : reflections.getTypesAnnotatedWith(Table.class)) {
            ret.add(c);
        }
        return ret;
    }

    public LinkedList<TableManager<?>> getTables() {
        return this.tables;
    }
}
