package json;

import java.util.HashMap;

public class SimpleJson {
    public static <T> T parse(final String json, final Class<T> clazz) throws Exception {
        if (json == null)
            throw new Exception("json is null");

        final var props = new HashMap<String, String>();

        final var lastIndex = json.length() - 1;

        StringBuilder propBuilder = null;
        StringBuilder valueBuilder = null;
        String prop = null;
        for (int i = 0; i < json.length(); i++) {
            if (i == 0) {
                if (json.charAt(i) != '{') throw new Exception("Invalid JSON");
                continue;
            }
            if (i == lastIndex) {
                if (json.charAt(i) != '}') throw new Exception("Invalid JSON");
                continue;
            }

            if (json.charAt(i) == '"') {
                if (propBuilder == null) {
                    propBuilder = new StringBuilder();
                    continue;
                }

                if (prop == null) {
                    prop = propBuilder.toString();
                    continue;
                }

                if (valueBuilder == null) {
                    valueBuilder = new StringBuilder();
                    continue;
                }

                final var value = valueBuilder.toString();
                props.put(prop, value);
                propBuilder = null;
                valueBuilder = null;
                prop = null;
                continue;
            }

            if (json.charAt(i) == ':' || json.charAt(i) == ',') {
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
        props.forEach((k, v) -> {
            try {
                final var field = clazz.getDeclaredField(k);
                field.setAccessible(true);
                field.set(instance, v);
            } catch (final Exception e) {
                System.err.println("Failed to set field: " + e.getMessage());
            }
        });
        return instance;
    }

    public static String toJson(final Object object) throws Exception {
        final var json = new StringBuilder();

        final var fields = object.getClass().getDeclaredFields();
        json.append("{");

        final var lastIndex = fields.length - 1;
        for (int i = 0; i < fields.length; i++) {
            final var field = fields[i];
            field.setAccessible(true);

            final var name = field.getName();
            final var value = field.get(object).toString();

            json
                .append("\"").append(name).append("\"")
                .append(":")
                .append("\"").append(value).append("\"");

            if (i != lastIndex) {
                json.append(",");
            }
        }

        json.append("}");

        return json.toString();
    }
}
