package mat.server.humanreadable.cql;

import java.util.Random;

public class HumanReadableExpressionModel {
    private String name;
    private String logic;
    private String id;

    public HumanReadableExpressionModel(String name, String logic) {
        this.name = name;
        this.logic = logic;
        this.id = idFromName(name);
    }

    private String idFromName(String name) {
        StringBuilder result = new StringBuilder();
        for (int i = 0; i < name.length(); i++) {
            char c = name.charAt(i);
            if (c >= 48 && c <= 57 || //0-9
                    c >= 65 && c <= 90 ||  //A-Z
                    c >= 97 && c <= 122 ||  //a-z
                    c == 95) { //_
                result.append(c);
            } else {
                result.append('_');
            }
        }
        return result.toString();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLogic() {
        return logic;
    }

    public void setLogic(String logic) {
        this.logic = logic;
    }

    public String getId() {
        return id;
    }
}
