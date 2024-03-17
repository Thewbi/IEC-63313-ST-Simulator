package model;

import java.util.List;

import instance.VariableInstance;

import java.util.ArrayList;

public class Step {

    private String name;

    private boolean initial;

    private List<ActionAssociation> actionAssociations = new ArrayList<>();

    /**
     * local actions
     */
    private List<Action> actions = new ArrayList<>();

    /**
     * transitions that start from this node
     */
    private List<Transition> transistions = new ArrayList<>();

    public void execute(VariableInstance functionBlockVariableInstance) {

        // System.out.println("");

        for (ActionAssociation actionAssociation : actionAssociations) {

            // System.out.println(actionAssociation);

            String qualifierAsString = actionAssociation.getQualifier();
            final ActionQualifier actionQualifier = ActionQualifier.valueOf(qualifierAsString);

            switch (actionQualifier) {

                case N: {
                    System.out.println("Setting " + actionAssociation.getName());
                    VariableInstance varInstance = functionBlockVariableInstance
                            .getElement(actionAssociation.getName());
                    varInstance.setValue("true");
                }
                    break;

                case S: {
                    Action action = functionBlockVariableInstance.getActionByName(actionAssociation.getName());
                    if (!functionBlockVariableInstance.getGlobalActions().contains(action)) {
                        System.out.println("Adding S-Type Action \"" + actionAssociation.getName() + "\" to global action list");
                        functionBlockVariableInstance.getGlobalActions().add(action);
                    }
                }
                    break;

                default:
                    throw new RuntimeException("Not implemented: \"" + qualifierAsString + "\"");
            }
        }
    }

    public String toString(int indent) {

        StringBuilder stringBuilder = new StringBuilder();
        DataType.addIndent(stringBuilder, indent);
        stringBuilder.append("Step").append("\n");

        DataType.addIndent(stringBuilder, indent + 1);
        stringBuilder.append("name = " + name).append("\n");

        DataType.addIndent(stringBuilder, indent + 1);
        stringBuilder.append("initial = " + initial).append("\n");

        DataType.addIndent(stringBuilder, indent + 1);
        stringBuilder.append("actionAssociations = ");
        stringBuilder.append("\n");
        for (ActionAssociation actionAssociation : actionAssociations) {
            stringBuilder.append(actionAssociation.toString(indent + 2));
        }
        // stringBuilder.append("\n");

        DataType.addIndent(stringBuilder, indent + 1);
        stringBuilder.append("actions = ");
        for (Action action : actions) {
            stringBuilder.append(action.toString(indent + 2));
        }
        stringBuilder.append("\n");

        return stringBuilder.toString();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isInitial() {
        return initial;
    }

    public void setInitial(boolean initial) {
        this.initial = initial;
    }

    public List<ActionAssociation> getActionAssociations() {
        return actionAssociations;
    }

    public void setActionAssociations(List<ActionAssociation> actionAssociations) {
        this.actionAssociations = actionAssociations;
    }

    public List<Action> getActions() {
        return actions;
    }

    public void setActions(List<Action> actions) {
        this.actions = actions;
    }

    public List<Transition> getTransistions() {
        return transistions;
    }

    public void setTransistions(List<Transition> transistions) {
        this.transistions = transistions;
    }

}
