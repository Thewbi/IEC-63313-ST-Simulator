package model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections4.CollectionUtils;

public class FunctionBlock extends VarScope {

    private FunctionBlockType functionBlockType = FunctionBlockType.STRUCTURED_TEXT;

    private List<Action> allActions = new ArrayList<>();

    private List<Action> globalActions = new ArrayList<>();

    private Map<String, Step> steps = new HashMap<>();

    /**
     * first, transitions are inserted into this list, then in a second step
     * inserted into their respective source actions
     */
    private List<Transition> tempTransitions = new ArrayList<>();

    @Override
    public String toString() {
        throw new RuntimeException();
    }

    public String toString(final int indent) {

        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(super.toString(indent)).append("\n");

        addIndent(stringBuilder, indent);
        stringBuilder.append("TempActions: ");
        if (CollectionUtils.isEmpty(getAllActions())) {
            stringBuilder.append("No TempActions");
        } else {
            for (Action action : getAllActions()) {
                stringBuilder.append("\n");
                stringBuilder.append(action.toString(indent + 1));
            }
        }
        stringBuilder.append("\n");

        addIndent(stringBuilder, indent);
        stringBuilder.append("TempTransitions: ");
        if (CollectionUtils.isEmpty(tempTransitions)) {
            stringBuilder.append("No TempTransitions");
        } else {
            stringBuilder.append("\n");
            for (Transition transition : tempTransitions) {
                stringBuilder.append(transition.toString(indent + 1));
            }
        }
        stringBuilder.append("\n");

        addIndent(stringBuilder, indent);
        stringBuilder.append("Steps: ");
        if (CollectionUtils.isEmpty(steps.values())) {
            stringBuilder.append("No Steps");
        } else {
            stringBuilder.append("\n");
            for (Step step : steps.values()) {
                stringBuilder.append(step.toString(indent + 1));
            }
        }
        // stringBuilder.append("\n");

        return stringBuilder.toString();
    }

    public void addStep(final String name, final Step step) {
        steps.put(name, step);
    }

    public FunctionBlockType getFunctionBlockType() {
        return functionBlockType;
    }

    public void setFunctionBlockType(FunctionBlockType functionBlockType) {
        this.functionBlockType = functionBlockType;
    }

    public List<Action> getAllActions() {
        return allActions;
    }

    public void setAllActions(List<Action> allActions) {
        this.allActions = allActions;
    }

    public List<Action> getGlobalActions() {
        return globalActions;
    }

    public void setGlobalActions(List<Action> globalActions) {
        this.globalActions = globalActions;
    }

    public List<Transition> getTempTransitions() {
        return tempTransitions;
    }

    public void setTempTransitions(List<Transition> tempTransitions) {
        this.tempTransitions = tempTransitions;
    }

    public Map<String, Step> getSteps() {
        return steps;
    }
}
