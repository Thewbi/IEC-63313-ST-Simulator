package model;

public class ActionAssociation {

    private String name;

    private String qualifier;

    public String toString(int indent) {

        StringBuilder stringBuilder = new StringBuilder();

        DataType.addIndent(stringBuilder, indent);
        stringBuilder.append("ActionAssociation").append("\n");

        DataType.addIndent(stringBuilder, indent+1);
        stringBuilder.append("name = " + name).append("\n");

        DataType.addIndent(stringBuilder, indent+1);
        stringBuilder.append("qualifier = " + qualifier).append("\n");

        return stringBuilder.toString();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getQualifier() {
        return qualifier;
    }

    public void setQualifier(String qualifier) {
        this.qualifier = qualifier;
    }

}
