package model;

import java.util.List;
import java.util.ArrayList;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.collections4.CollectionUtils;

public class FunctionBlock extends VarScope {

    private FunctionBlockType functionBlockType = FunctionBlockType.STRUCTURED_TEXT;

    private List<Action> tempActions = new ArrayList<>();

    private List<Action> globalActions = new ArrayList<>();

    private Map<String, Step> steps = new HashMap<>();

    private List<Step> currentSteps = new ArrayList<>();

    /**
     * first, transitions are inserted into this list, then in a second step
     * inserted into their respective source actions
     */
    private List<Transition> tempTransitions = new ArrayList<>();

    public void initialize() {
        
        // take the transitions out of the tempTransitions list and add
        // them into the steps they originate from
        for (final Transition transition : tempTransitions) {
            final String sourceStepName = transition.getSourceStepName();
            steps.get(sourceStepName).getTransistions().add(transition);
        }

        // add the initial steps into the list of currentSteps
        for (final Step step : steps.values()) {
            if (step.isInitial()) {
                currentSteps.add(step);
            }
        }
    }

    @Override
    public String toString() {
        throw new RuntimeException();
    }

    public String toString(final int indent) {

        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(super.toString(indent)).append("\n");

        addIndent(stringBuilder, indent);
        stringBuilder.append("TempActions: ");
        if (CollectionUtils.isEmpty(getTempActions())) {
            stringBuilder.append("No TempActions");
        } else {
            for (Action action : getTempActions()) {
                stringBuilder.append("\n");
                stringBuilder.append(action.toString(indent+1));
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
                // stringBuilder.append("\n");
                stringBuilder.append(transition.toString(indent+1));
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
                stringBuilder.append(step.toString(indent+1));
            }
        }
        stringBuilder.append("\n");

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

    public List<Action> getTempActions() {
        return tempActions;
    }

    public void setTempActions(List<Action> tempActions) {
        this.tempActions = tempActions;
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

    /**
     * <ol>
     * <li>all steps in the current step list are executed.</li>
     * <li>if the current step executes an action using the S qualifier, the action is added to the list
     * of global actions of the SequentialFunctionChart function block unless the action is added already.</li>
     * <li>the global actions of the SequentialFunctionChart function block are executed.</li>
     * <li>the local actions of all current steps are executed.</li>
     * <li>All transitions of the current steps are evaluated. For each transition that is activated
     * - the source state is removed from the current step list
     * - the target state is added to the current step list</li>
     * </ol>
     */
    public void executeStateMachine() {
        System.out.println("executeStateMachine()");
    }

}
