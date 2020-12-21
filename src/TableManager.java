import java.lang.reflect.Field;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

public class TableManager<T> {
    private List<T> rows;
    private Class<T> classType;
    private DatabaseManager db;

    public TableManager(Class<T> classType, DatabaseManager db) {
        this.classType = classType;
        this.db = db;
        rows = new LinkedList<>();
        load();
    }

    public void updateRow(T row) {
        Field primaryColumnField = null;
        for (Field field : classType.getDeclaredFields()) {
            if (field.isAnnotationPresent(PrimaryColumn.class)) {
                primaryColumnField = field;
            }
        }
        if (primaryColumnField == null) {
            System.err.println("Table must have a primary column!");
            return;
        }
        try {
            if (primaryColumnField.getType().getName().equals("int")) {
                int argPrimaryValue = primaryColumnField.getInt(row);
                boolean updated = false;
                for (T lrow : this.rows) {
                    int primaryValue = primaryColumnField.getInt(lrow);
                    if (argPrimaryValue == primaryValue) {
                        for (Field field : classType.getDeclaredFields()) {
                            if (field.isAnnotationPresent(Column.class)) {
                                field.set(lrow, field.get(row));
                            }
                        }
                        updated = true;
                    }
                }
                if (!updated) {
                    rows.add(row);
                }
            }
        } catch (IllegalAccessException e) {
            System.err.println(e.getMessage());
            return;
        }
    }

    public void updateRows(T[] rows) {
        for (T row : rows) {
            this.rows.add(row);
        }
    }

    public void save() {
        try {
            Field[] fields = classType.getDeclaredFields();
            File file = new File(DatabaseManager.tableDirectory + "/" + classType.getName());
            DataOutputStream dos = new DataOutputStream(new FileOutputStream(file));

            for (T row : rows) {

                int bufferSize = 0;
                int bufferIndex = 0;
                byte buffer[];

                for (Field f : fields) {
                    if (f.isAnnotationPresent(Column.class) || f.isAnnotationPresent(PrimaryColumn.class)) {
                        switch (f.getType().getSimpleName()) {
                            case "byte": {
                                bufferSize += Byte.SIZE / 8;
                                break;
                            }
                            case "boolean": {
                                bufferSize++;
                                break;
                            }
                            case "short": {
                                bufferSize += Short.SIZE / 8;
                                break;
                            }
                            case "int": {
                                bufferSize += Integer.SIZE / 8;
                                break;
                            }
                            case "long": {
                                bufferSize += Long.SIZE / 8;
                                break;
                            }
                            case "char": {
                                bufferSize += Character.SIZE / 8;
                                break;
                            }
                            case "float": {
                                bufferSize += Float.SIZE / 8;
                                break;
                            }
                            case "double": {
                                bufferSize += Double.SIZE / 8;
                                break;
                            }
                            case "String": {
                                try {
                                    bufferSize += ((String) f.get(row)).length() + 1;
                                } catch (IllegalAccessException e) {
                                    System.err.println(e.getMessage());
                                    dos.close();
                                    return;
                                }
                                break;
                            }
                        }
                    }
                }

                buffer = new byte[bufferSize + 4];

                bufferIndex = BufferIO.writeBufferInt(buffer, bufferIndex, bufferSize);

                for (Field f : fields) {
                    if (f.isAnnotationPresent(Column.class) || f.isAnnotationPresent(PrimaryColumn.class)) {
                        try {
                            switch (f.getType().getSimpleName()) {
                                case "byte": {
                                    bufferIndex = BufferIO.writeBufferByte(buffer, bufferIndex, f.getByte(row));
                                    break;
                                }
                                case "boolean": {
                                    bufferIndex = BufferIO.writeBufferBoolean(buffer, bufferIndex, f.getBoolean(row));
                                    break;
                                }
                                case "short": {
                                    bufferIndex = BufferIO.writeBufferShort(buffer, bufferIndex, f.getShort(row));
                                    break;
                                }
                                case "int": {
                                    bufferIndex = BufferIO.writeBufferInt(buffer, bufferIndex, f.getInt(row));
                                    break;
                                }
                                case "long": {
                                    bufferIndex = BufferIO.writeBufferLong(buffer, bufferIndex, f.getLong(row));
                                    break;
                                }
                                case "char": {
                                    bufferIndex = BufferIO.writeBufferChar(buffer, bufferIndex, f.getChar(row));
                                    break;
                                }
                                case "float": {
                                    bufferIndex = BufferIO.writeBufferFloat(buffer, bufferIndex, f.getFloat(row));
                                    break;
                                }
                                case "double": {
                                    bufferIndex = BufferIO.writeBufferDouble(buffer, bufferIndex, f.getDouble(row));
                                    break;
                                }
                                case "String": {
                                    bufferIndex = BufferIO.writeBufferString(buffer, bufferIndex, (String) f.get(row));
                                    break;
                                }
                            }
                        } catch (IllegalAccessException e) {
                            System.err.println(e.getMessage());
                            dos.close();
                            return;
                        }
                    }
                }
                dos.write(buffer);
            }
            dos.close();
        } catch (IOException e) {
            System.err.println(e.getMessage());
            return;
        }
    }

    public void load() {
        try {
            Field[] fields = classType.getDeclaredFields();
            File file = new File(DatabaseManager.tableDirectory + "/" + classType.getName());
            DataInputStream dis = new DataInputStream(new FileInputStream(file));

            byte[] buffer = new byte[(int) file.length()];

            for (int i = 0; dis.available() > 0; i++) {
                buffer[i] = dis.readByte();
            }
            dis.close();
            int bufferIndex = 0;

            while (bufferIndex < buffer.length) {
                int rowSize = BufferIO.readBufferInt(buffer, bufferIndex);
                bufferIndex += Integer.SIZE / 8;

                T row = classType.getDeclaredConstructor().newInstance();

                for (Field f : fields) {
                    if (f.isAnnotationPresent(Column.class) || f.isAnnotationPresent(PrimaryColumn.class)) {
                        try {
                            switch (f.getType().getSimpleName()) {
                                case "byte": {
                                    f.setByte(row, BufferIO.readBufferByte(buffer, bufferIndex));
                                    bufferIndex += Byte.SIZE / 8;
                                    break;
                                }
                                case "boolean": {
                                    f.setBoolean(row, BufferIO.readBufferBoolean(buffer, bufferIndex));
                                    bufferIndex++;
                                    break;
                                }
                                case "short": {
                                    f.setShort(row, BufferIO.readBufferShort(buffer, bufferIndex));
                                    bufferIndex += Short.SIZE / 8;
                                    break;
                                }
                                case "int": {
                                    f.setInt(row, BufferIO.readBufferInt(buffer, bufferIndex));
                                    bufferIndex += Integer.SIZE / 8;
                                    break;
                                }
                                case "long": {
                                    f.setLong(row, BufferIO.readBufferLong(buffer, bufferIndex));
                                    bufferIndex += Long.SIZE / 8;
                                    break;
                                }
                                case "char": {
                                    f.setChar(row, BufferIO.readBufferChar(buffer, bufferIndex));
                                    bufferIndex += Character.SIZE / 8;
                                    break;
                                }
                                case "float": {
                                    f.setFloat(row, BufferIO.readBufferFloat(buffer, bufferIndex));
                                    bufferIndex += Float.SIZE / 8;
                                    break;
                                }
                                case "double": {
                                    f.setDouble(row, BufferIO.readBufferDouble(buffer, bufferIndex));
                                    bufferIndex += Double.SIZE / 8;
                                    break;
                                }
                                case "String": {
                                    String s = BufferIO.readBufferString(buffer, bufferIndex);
                                    f.set(row, s);
                                    bufferIndex += s.length() + 1;
                                    break;
                                }
                            }
                        } catch (Exception e) {
                            System.err.println(e.getMessage());
                            e.printStackTrace();
                            return;
                        }

                    }
                }
                rows.add(row);
            }

        } catch (Exception e) {
            System.err.println(e.getMessage());
            e.printStackTrace();
            return;
        }
    }

    public Class<T> getClassType() {
        return this.classType;
    }

    public List<T> getRows() {
        return this.rows;
    }
}
