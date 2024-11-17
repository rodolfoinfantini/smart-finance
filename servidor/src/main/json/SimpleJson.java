package main.json;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

/**
 * A (not so) simple JSON parser that can parse JSON strings into Java objects
 * and vice versa.
 *
 * @author Rodolfo Infantini
 * @version 2.1
 * @since 1.0
 */
public class SimpleJson {
    /**
     * Parses a JSON List string into a Java List object.
     *
     * @param json  The JSON string to parse
     * @param clazz The class of the objects in the list
     * @param <T>   The type of the objects in the list
     * @return A List object with the parsed objects
     * @throws Exception If the JSON string is invalid or if the class is invalid
     * @see #parse(String, Class)
     */
    @SuppressWarnings("unchecked")
    public static <T> List<T> parseList(final String json, final Class<T> clazz) throws Exception {
        if (json == null)
            throw new Exception("Json is null");
        if (json.equals("[]"))
            return new ArrayList<>();
        if (!json.startsWith("[") || !json.endsWith("]"))
            throw new Exception("Invalid JSON");

        final var list = new ArrayList<T>();

        final var lastIndex = json.length() - 1;

        var jsonBuilder = new StringBuilder();
        int bracesInside = 0;
        for (int i = 1; i < json.length(); i++) {
            if (json.charAt(i) == '{' || json.charAt(i) == '[')
                bracesInside++;

            if (json.charAt(i) == '}' || json.charAt(i) == ']')
                bracesInside--;

            if ((json.charAt(i) == ',' && bracesInside == 0) || i == lastIndex) {
                var value = jsonBuilder.toString();
                list.add((T) parseValue(clazz, value));
                jsonBuilder = new StringBuilder();
                continue;
            }

            jsonBuilder.append(json.charAt(i));
        }

        return list;
    }

    /**
     * Parses a JSON string into a Java object.
     *
     * @param json  The JSON string to parse
     * @param clazz The class of the object
     * @param <T>   The type of the object
     * @return A Java object with the parsed data
     * @throws Exception If the JSON string is invalid or if the class is a List
     * @see #parseList(String, Class)
     */
    public static <T> T parse(final String json, final Class<T> clazz) throws Exception {
        if (json == null)
            throw new Exception("main.json is null");
        if (clazz == List.class)
            throw new Exception("Invalid class, use parseList instead");
        if (!json.startsWith("{") || !json.endsWith("}"))
            throw new Exception("Invalid JSON");

        final var instance = clazz.getDeclaredConstructor().newInstance();

        final var lastIndex = json.length() - 1;

        var propBuilder = new StringBuilder();
        var valueBuilder = new StringBuilder();
        boolean checkingProp = true;
        int bracesInside = 0;
        boolean insideQuotes = false;
        for (int i = 1; i < json.length(); i++) {
            final var currentLetter = json.charAt(i);

            if (currentLetter == '{' || currentLetter == '[')
                bracesInside++;

            if (currentLetter == '}' || currentLetter == ']')
                bracesInside--;

            if (currentLetter == '"') {
                if (checkingProp)
                    continue;
                if (json.charAt(i - 1) != '\\') // Escaping quotes with \
                    insideQuotes = !insideQuotes;
            }

            if (currentLetter == ':' && bracesInside == 0 && !insideQuotes) {
                checkingProp = false;
                valueBuilder = new StringBuilder();
                continue;
            }

            if ((currentLetter == ',' && bracesInside == 0 && !insideQuotes) || i == lastIndex) {
                try {
                    final var field = clazz.getDeclaredField(propBuilder.toString());
                    field.setAccessible(true);
                    field.set(instance, parseValue(field, valueBuilder.toString()));
                } catch (final NoSuchFieldException ignored) {
                } catch (final Exception e) {
                    throw new Exception("Invalid JSON", e);
                }

                checkingProp = true;
                propBuilder = new StringBuilder();
                continue;
            }

            if (checkingProp) {
                propBuilder.append(currentLetter);
            } else {
                valueBuilder.append(currentLetter);
            }
        }

        return instance;
    }

    private static Object parseValue(final Class<?> type, final String value) throws Exception {
        return parseValue(type, null, value);
    }

    private static Object parseValue(final Field field, final String value) throws Exception {
        return parseValue(field.getType(), field.getGenericType(), value);
    }

    private static Object parseValue(final Class<?> type, final Type genericType, final String value) throws Exception {
        if (value.equals("null")) // Does not count if it is a string with the value "null"
            return null;
        if (type == String.class)
            return value.substring(1, value.length() - 1); // Removing quotes
        if (type == Integer.class || type == int.class)
            return Integer.parseInt(value);
        if (type == Double.class || type == double.class)
            return Double.parseDouble(value);
        if (type == Long.class || type == long.class)
            return Long.parseLong(value);
        if (type == Boolean.class || type == boolean.class)
            return value.equals("true");
        if (type == Character.class || type == char.class)
            return value.charAt(1); // Removing quotes
        if (type == Float.class || type == float.class)
            return Float.parseFloat(value);
        if (type == Short.class || type == short.class)
            return Short.parseShort(value);
        if (type == Byte.class || type == byte.class)
            return Byte.parseByte(value);
        if (type == List.class) {
            final var listType = (ParameterizedType) genericType;
            return parseList(value, (Class<?>) listType.getActualTypeArguments()[0]);
        }

        return parse(value, type);
    }

    /**
     * Converts a Java object into a JSON string.
     *
     * @param object The Java object to convert
     * @return A JSON string with the object data
     * @throws IllegalAccessException If the object is invalid
     */
    public static String toJson(final Object object) throws IllegalAccessException {
        if (object instanceof final List<?> list) {
            return listToJson(list);
        } else {
            return objToJson(object);
        }
    }

    private static String objToJson(final Object object) throws IllegalAccessException {
        if (object == null)
            return null;

        final var json = new StringBuilder("{");

        final var fields = object.getClass().getDeclaredFields();

        final var lastIndex = fields.length - 1;
        for (int i = 0; i < fields.length; i++) {
            final var field = fields[i];
            field.setAccessible(true);

            final var name = field.getName();
            final var value = field.get(object);

            json.append('"').append(name).append('"')
                    .append(":")
                    .append(getJsonValue(value));

            if (i != lastIndex)
                json.append(",");
        }

        return json.append("}").toString();
    }

    private static String listToJson(final List<?> list) throws IllegalAccessException {
        if (list == null)
            return null;

        final var json = new StringBuilder("[");

        final var lastIndex = list.size() - 1;
        for (int i = 0; i < list.size(); i++) {
            json.append(getJsonValue(list.get(i)));

            if (i != lastIndex)
                json.append(",");
        }

        return json.append("]").toString();
    }

    private static String getJsonValue(final Object value) throws IllegalAccessException {
        if (value == null)
            return "null";

        if (value instanceof String || value instanceof Character)
            return '"' + value.toString() + '"';

        if (value instanceof Integer || value instanceof Double || value instanceof Long
                || value instanceof Boolean || value instanceof Float || value instanceof Short
                || value instanceof Byte)
            return value.toString();

        return toJson(value);
    }
}
