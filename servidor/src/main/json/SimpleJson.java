package main.json;

import java.lang.reflect.ParameterizedType;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * A (not so) simple JSON parser that can parse JSON strings into Java objects and vice versa.
 *
 * @author Rodolfo Infantini
 * @version 1.0
 * @since 0.1
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
        if (json == null) throw new Exception("Json is null");
        if (json.equals("[]")) return new ArrayList<>();

        final var list = new ArrayList<T>();

        final var lastIndex = json.length() - 1;

        boolean isJsonArray = json.charAt(1) == '{';

        var jsonBuilder = new StringBuilder();
        int bracesInside = 0;
        for (int i = 0; i < json.length(); i++) {
            if (i == 0) {
                if (json.charAt(i) != '[') throw new Exception("Invalid JSON");
                continue;
            }
            if (i == lastIndex) {
                if (json.charAt(i) != ']') throw new Exception("Invalid JSON");

                var value = jsonBuilder.toString();
                if (isJsonArray) {
                    list.add(parse(value, clazz));
                } else {
                    if (value.charAt(0) == '"' && value.charAt(value.length() - 1) == '"') {
                        value = value.substring(1);
                        value = value.substring(0, value.length() - 1);
                    }

                    if (value.equals("null")) {
                        list.add(null);
                    } else if (clazz == Integer.class || clazz == int.class) {
                        list.add((T) Integer.valueOf(value));
                    } else if (clazz == Double.class || clazz == double.class) {
                        list.add((T) Double.valueOf(value));
                    } else if (clazz == Long.class || clazz == long.class) {
                        list.add((T) Long.valueOf(value));
                    } else if (clazz == Boolean.class || clazz == boolean.class) {
                        list.add((T) Boolean.valueOf(value));
                    } else if (clazz == String.class) {
                        list.add((T) value);
                    } else if (clazz == Float.class || clazz == float.class) {
                        list.add((T) Float.valueOf(value));
                    } else if (clazz == Short.class || clazz == short.class) {
                        list.add((T) Short.valueOf(value));
                    } else if (clazz == Byte.class || clazz == byte.class) {
                        list.add((T) Byte.valueOf(value));
                    } else {
                        list.add(parse(value, clazz));
                    }
                }
                jsonBuilder = new StringBuilder();
                continue;
            }

            if (json.charAt(i) == '{' || json.charAt(i) == '[') {
                bracesInside++;
                jsonBuilder.append(json.charAt(i));
                continue;
            }

            if (json.charAt(i) == '}' || json.charAt(i) == ']') {
                bracesInside--;
                jsonBuilder.append(json.charAt(i));
                continue;
            }

            if (json.charAt(i) == ',' && bracesInside == 0) {
                var value = jsonBuilder.toString();
                if (isJsonArray) {
                    list.add(parse(value, clazz));
                } else {
                    if (value.charAt(0) == '"' && value.charAt(value.length() - 1) == '"') {
                        value = value.substring(1);
                        value = value.substring(0, value.length() - 1);
                    }

                    if (value.equals("null")) {
                        list.add(null);
                    } else if (clazz == Integer.class || clazz == int.class) {
                        list.add((T) Integer.valueOf(value));
                    } else if (clazz == Double.class || clazz == double.class) {
                        list.add((T) Double.valueOf(value));
                    } else if (clazz == Long.class || clazz == long.class) {
                        list.add((T) Long.valueOf(value));
                    } else if (clazz == Boolean.class || clazz == boolean.class) {
                        list.add((T) Boolean.valueOf(value));
                    } else if (clazz == String.class) {
                        list.add((T) value);
                    } else if (clazz == Float.class || clazz == float.class) {
                        list.add((T) Float.valueOf(value));
                    } else if (clazz == Short.class || clazz == short.class) {
                        list.add((T) Short.valueOf(value));
                    } else if (clazz == Byte.class || clazz == byte.class) {
                        list.add((T) Byte.valueOf(value));
                    } else {
                        list.add(parse(value, clazz));
                    }
                }
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

        final var props = new HashMap<String, Object>();

        final var lastIndex = json.length() - 1;

        StringBuilder propBuilder = null;
        StringBuilder valueBuilder = null;
        String prop = null;
        boolean checkingProp = true;
        int bracesInside = 0;
        for (int i = 0; i < json.length(); i++) {
            if (i == 0) {
                if (json.charAt(i) != '{') throw new Exception("Invalid JSON");
                continue;
            }
            if (i == lastIndex) {
                if (json.charAt(i) != '}') throw new Exception("Invalid JSON");

                if (valueBuilder != null) {
                    var value = valueBuilder.toString();
                    if (value.charAt(0) == '"' && value.charAt(value.length() - 1) == '"') {
                        value = value.substring(1);
                        value = value.substring(0, value.length() - 1);
                    }
                    props.put(prop, value);
                    valueBuilder = null;
                }

                continue;
            }

            if (json.charAt(i) == '{' || json.charAt(i) == '[') {
                bracesInside++;
                if (valueBuilder == null) {
                    valueBuilder = new StringBuilder();
                }
                valueBuilder.append(json.charAt(i));
                continue;
            }

            if (json.charAt(i) == '}' || json.charAt(i) == ']') {
                bracesInside--;
                if (valueBuilder == null) {
                    valueBuilder = new StringBuilder();
                }
                valueBuilder.append(json.charAt(i));
                continue;
            }

            if (json.charAt(i) == '"' && checkingProp) {
                if (propBuilder != null) {
                    prop = propBuilder.toString();
                } else {
                    propBuilder = new StringBuilder();
                    prop = null;
                }

                continue;
            }

            if (json.charAt(i) == ':' && bracesInside == 0) {
                checkingProp = false;
                valueBuilder = new StringBuilder();
                continue;
            }

            if (json.charAt(i) == ',' && bracesInside == 0) {
                checkingProp = true;
                if (valueBuilder != null) {
                    var value = valueBuilder.toString();
                    if (value.charAt(0) == '"' && value.charAt(value.length() - 1) == '"') {
                        value = value.substring(1);
                        value = value.substring(0, value.length() - 1);
                    }
                    props.put(prop, value);
                    prop = null;
                    propBuilder = null;
                    valueBuilder = null;
                }
                continue;
            }

            if (prop == null) {
                if (propBuilder != null)
                    propBuilder.append(json.charAt(i));
                continue;
            }

            if (valueBuilder != null)
                valueBuilder.append(json.charAt(i));
        }

        final var instance = clazz.getDeclaredConstructor().newInstance();
        for (final var key : props.keySet()) {
            final var value = props.get(key);
            try {
                final var field = clazz.getDeclaredField(key);
                field.setAccessible(true);
                if (value.equals("null")) {
                    field.set(instance, null);
                } else if (field.getType() == Integer.class || field.getType() == int.class) {
                    field.set(instance, Integer.parseInt((String) value));
                } else if (field.getType() == Double.class || field.getType() == double.class) {
                    field.set(instance, Double.parseDouble((String) value));
                } else if (field.getType() == Long.class || field.getType() == long.class) {
                    field.set(instance, Long.parseLong((String) value));
                } else if (field.getType() == Boolean.class || field.getType() == boolean.class) {
                    field.set(instance, value.equals("true"));
                } else if (field.getType() == String.class) {
                    field.set(instance, value);
                } else if (field.getType() == Float.class || field.getType() == float.class) {
                    field.set(instance, Float.parseFloat((String) value));
                } else if (field.getType() == Short.class || field.getType() == short.class) {
                    field.set(instance, Short.parseShort((String) value));
                } else if (field.getType() == Byte.class || field.getType() == byte.class) {
                    field.set(instance, Byte.parseByte((String) value));
                } else if (field.getType() == List.class) {
                    final var listType = (ParameterizedType) field.getGenericType();
                    field.set(instance, parseList((String) value, (Class<?>) listType.getActualTypeArguments()[0]));
                } else {
                    field.set(instance, parse((String) value, field.getType()));
                }
            }
            catch (final NoSuchFieldException ignored) {}
            catch (final Exception e) {
                System.err.println("Failed to set field: " + e.getMessage());
            }
        }
        return instance;
    }

    /**
     * Converts a Java object into a JSON string.
     *
     * @param object The Java object to convert
     * @return A JSON string with the object data
     * @throws IllegalAccessException If the object is invalid
     */
    public static String toJson(final Object object) throws IllegalAccessException {
        if (object instanceof List<?> list) {
            return listToJson(list);
        } else {
            return objToJson(object);
        }
    }

    private static String objToJson(final Object object) throws IllegalAccessException {
        if (object == null) return null;

        final var json = new StringBuilder("{");

        final var fields = object.getClass().getDeclaredFields();

        final var lastIndex = fields.length - 1;
        for (int i = 0; i < fields.length; i++) {
            final var field = fields[i];
            field.setAccessible(true);

            final var name = field.getName();
            final var value = field.get(object);

            final var valueBuilder = new StringBuilder();
            if (value == null) {
                valueBuilder.append("null");
            } else if (value instanceof String) {
                valueBuilder.append("\"").append(value).append("\"");
            } else if (value instanceof Integer || value instanceof Double || value instanceof Long || value instanceof Boolean || value instanceof Float || value instanceof Short || value instanceof Byte) {
                valueBuilder.append(value);
            } else {
                valueBuilder.append(toJson(value));
            }

            json
                    .append("\"").append(name).append("\"")
                    .append(":")
                    .append(valueBuilder);

            if (i != lastIndex) {
                json.append(",");
            }
        }

        json.append("}");

        return json.toString();
    }

    private static String listToJson(final List<?> list) throws IllegalAccessException {
        if (list == null) return null;

        final var json = new StringBuilder("[");

        final var lastIndex = list.size() - 1;
        for (int i = 0; i < list.size(); i++) {
            final var value = list.get(i);
            if (value == null) {
                json.append("null");
            } else if (value instanceof String) {
                json.append("\"").append(value).append("\"");
            } else if (value instanceof Integer || value instanceof Double || value instanceof Long || value instanceof Boolean || value instanceof Float || value instanceof Short || value instanceof Byte) {
                json.append(value);
            } else {
                json.append(toJson(value));
            }

            if (i != lastIndex) {
                json.append(",");
            }
        }

        json.append("]");

        return json.toString();
    }
}
