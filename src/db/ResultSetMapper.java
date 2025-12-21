package db;

import java.lang.reflect.Field;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class ResultSetMapper<T> {
    public List<T> mapResultSet(ResultSet rs, Class<T> clazz) {
        List<T> outputList = new ArrayList<>();
        try {
            while (rs.next()) {
                T dto = clazz.getDeclaredConstructor().newInstance();
                Field[] fields = clazz.getDeclaredFields();

                for (Field field : fields) {
                    field.setAccessible(true);
                    String colName = toSnakeCase(field.getName());

                    try {
                        Object value = rs.getObject(colName);
                        field.set(dto, value);
                    } catch (Exception e) {
                    }
                }
                outputList.add(dto);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return outputList;
    }

    private String toSnakeCase(String str) {
        String result = str.replaceAll("([a-z])([A-Z]+)", "$1_$2").toLowerCase();
        return result;
    }
}