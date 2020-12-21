import java.lang.reflect.Field;
import java.util.LinkedList;
import java.util.List;

public class Repository<T> {

    Class<T> classType;
    DatabaseManager db;
    TableManager<T> table;

    public Repository(Class<T> classType, DatabaseManager db) {
        this.classType = classType;
        this.db = db;
    }

    public Repository(Class<T> classType, TableManager<T> table) {
        this.classType = classType;
        this.table = table;
    }

    public void save(T row) {
        table.updateRow(row);
        table.save();
    }

    public List<T> find() {
        return table.getRows();
    }

    public List<T> find(T target, String ...fields) {
        LinkedList<T> matching = new LinkedList<>();
        for(T row : table.getRows()) {
            boolean matches = fields.length > 0;
            for (String f : fields) {
                try {
                    Field field = classType.getField(f);
                    Object f1 = field.get(target);
                    Object f2 = field.get(row);
                    matches = !(f1 == null ^ f2 == null  || !f1.equals(f2));
                } catch (NoSuchFieldException e) {
                    System.err.println(e.getMessage());
                    continue;
                } catch (IllegalAccessException e) {
                    System.err.println(e.getMessage());
                    return null;
                }
            }
            if(matches) matching.add(row);
        }
        return matching;
    }

    public T findOne(T target, String ...fields) {
        for(T row : table.getRows()) {
            boolean matches = fields.length > 0;
            for (String f : fields) {
                try {
                    Field field = classType.getField(f);
                    if(!field.get(target).equals(field.get(row))) matches = false;
                } catch (NoSuchFieldException e) {
                    System.err.println(e.getMessage());
                    continue;
                } catch (IllegalAccessException e) {
                    System.err.println(e.getMessage());
                    return null;
                }
            }
            if(matches) return row;
        }
        return null;
    }

    public Class<T> getClassType() {
        return this.classType;
    }

    public String toString() {
        return "Repository of type: " + classType.getName();
    }
}
