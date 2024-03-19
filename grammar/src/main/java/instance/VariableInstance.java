package instance;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.StringUtils;

import model.Action;
import model.DataType;
import model.Statement;
import model.StatementContainer;
import model.Step;
import model.Transition;
import model.TypeScope;

public class VariableInstance implements StatementContainer {

    private static final boolean EXECUTE_ACTIONS_EACH_CYCLE = true;

    private String name;

    private DataType dataType;

    private String value;

    private Map<String, VariableDescriptor> elements = new HashMap<>();

    private List<Statement> statements = new ArrayList<>();

    private List<Action> allActions = new ArrayList<>();

    private List<Action> globalActions = new ArrayList<>();

    private Map<String, Step> steps = new HashMap<>();

    private List<Step> currentSteps = new ArrayList<>();

    /**
     * first, transitions are inserted into this list, then in a second step
     * inserted into their respective source actions
     */
    private List<Transition> tempTransitions = new ArrayList<>();

    public void initialize() {

        // a action contains statements. When executing the action, all statements are
        // executed.
        // The variables that the statements execute on are contained not in the action
        // itself but in the variable instance that was created from the FunctionBlock!
        // To correctly execute the statements, the variable instance is required as a
        // context.
        // Here the context is set!
        for (Action action : allActions) {
            action.setParentVariableInstance(this);
        }

        // take the transitions out of the tempTransitions list and add
        // them into the steps they originate from
        for (final Transition transition : tempTransitions) {
            final String sourceStepName = transition.getSourceStepName();
            steps.get(sourceStepName).getTransistions().add(transition);
        }

        // DEBUG build transition names
        for (final Transition transition : tempTransitions) {

            StringBuilder stringBuilder = new StringBuilder();

            final String sourceStepName = transition.getSourceStepName();
            stringBuilder.append(sourceStepName);

            stringBuilder.append(" ---> ");

            int idx = 0;
            for (String targetStepName : transition.getTargetStepNames()) {

                if (idx > 0) {
                    stringBuilder.append(", ");
                }
                stringBuilder.append(targetStepName);

                idx++;
            }

            transition.setName(stringBuilder.toString());

        }

        // add the initial steps into the list of currentSteps
        for (final Step step : steps.values()) {
            if (step.isInitial()) {
                currentSteps.add(step);
            }
        }
    }

    public void addElement(VariableInstance element) {
        boolean retain = false;
        boolean external = false;
        addElement(element, retain, external);
    }

    public void addElement(VariableInstance element, boolean retain, boolean external) {
        if (elements.containsKey(element.getName())) {
            throw new RuntimeException("Element " + element.getName() + " contained already!");
        }

        VariableDescriptor variableDescriptor = new VariableDescriptor();
        variableDescriptor.variableInstance = element;
        variableDescriptor.retain = retain;
        variableDescriptor.external = external;

        elements.put(element.getName(), variableDescriptor);
    }

    public void setElement(String parameterName, VariableInstance variableInstance) {

        VariableDescriptor variableDescriptor = new VariableDescriptor();
        variableDescriptor.variableInstance = variableInstance;
        variableDescriptor.retain = false;
        variableDescriptor.external = false;

        elements.put(parameterName, variableDescriptor);
    }

    public VariableInstance getElement(String name) {
        if (!elements.containsKey(name)) {
            return null;
        }
        return elements.get(name).variableInstance;
    }

    /**
     * <ol>
     * <li>1. All actions of the entire SequentialFunctionChart are set to
     * isExecuted(false);</li>
     * <li>2. the global actions of the SequentialFunctionChart function block are
     * executed.
     * After execution, isExecuted(true) is called.</li>
     * <li>3. all steps in the current step list are executed.</li>
     * <li>4. if the current step executes an action using the S qualifier, the
     * action is added to the list</li>
     * of global actions of the SequentialFunctionChart function block unless the
     * action is added already.
     * <li>5. the global actions of the SequentialFunctionChart function block are
     * executed unless they have already
     * been executed: isExecuted(true).</li>
     * <li>6. the local actions of all current steps are executed.</li>
     * <li>7. All transitions are evaluated. For each transition that is
     * activated</li>
     * - the old state is removed from the current step list
     * -the new state is added to the current step list
     * </li>
     * </ol>
     * 
     * @param variableInstance
     */
    public void executeStateMachine(TypeScope globalTypeScope) {

        if (MapUtils.isEmpty(steps)) {
            return;
        }

        if (EXECUTE_ACTIONS_EACH_CYCLE) {
            // 1. All actions of the entire SequentialFunctionChart are set to
            // isExecuted(false);
            for (Action action : allActions) {
                action.setExecuted(false);
            }
        }

        // 2. the global actions of the SequentialFunctionChart function block are
        // executed. After execution, isExecuted(true) is called.
        for (Action action : globalActions) {
            action.execute(globalTypeScope);
        }

        // 3. all steps in the current step list are executed.
        for (Step step : currentSteps) {

            // DEBUG: output the current step
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("Current step: \"" + step.getName() + "\"");
            if (step.isInitial()) {
                stringBuilder.append(" (INITIAL STATE)");
            }
            stringBuilder.append(" willExecute: ").append(!step.isExecuted());
            System.out.println(stringBuilder.toString());

            // execute all actionassociations (N, R, S, ...)
            step.execute(this);
        }

        // 5. the global actions of the SequentialFunctionChart function block are
        // executed unless they have already been executed: isExecuted(true).
        for (Action action : globalActions) {
            action.execute(globalTypeScope);
        }

        // 7. All transitions are evaluated. For each transition that is activated</li>
        // - the old state is removed from the current step list
        // - the new state is added to the current step list
        List<Step> stepsToAdd = new ArrayList<>();
        List<Step> stepsToRemove = new ArrayList<>();

        for (Step step : currentSteps) {

            for (Transition transition : step.getTransistions()) {

                boolean evalResult = transition.evaluate(globalTypeScope, this);
                if (evalResult) {

                    System.out.println("Applying transition \"" + transition.getName() + "\"");

                    // - the old state is removed from the current step list
                    // - the new state is added to the current step list
                    stepsToRemove.add(step);
                    for (String targetStepName : transition.getTargetStepNames()) {

                        Step executeStep = getSteps().get(targetStepName);
                        stepsToAdd.add(executeStep);
                    }
                }
            }
        }

        // - the old state is removed from the current step list
        currentSteps.removeAll(stepsToRemove);
        for (Step step : stepsToRemove) {
            step.setExecuted(false);
        }

        // - the new state is added to the current step list
        currentSteps.addAll(stepsToAdd);
        for (Step step : stepsToAdd) {
            step.setExecuted(false);
        }
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public DataType getDataType() {
        return dataType;
    }

    public void setDataType(DataType dataType) {
        this.dataType = dataType;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        // DEBUG
        // if (StringUtils.equalsIgnoreCase(name, "Stoer_Quit") &&
        // StringUtils.equalsIgnoreCase(value, "true")) {
        // System.out.println(")");
        // }
        this.value = value;
    }

    public List<Statement> getStatements() {
        return statements;
    }

    public Map<String, VariableDescriptor> getElements() {
        return elements;
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

    public void setSteps(Map<String, Step> steps) {
        this.steps = steps;
    }

    public Action getActionByName(String name) {
        for (Action action : allActions) {
            if (StringUtils.equalsIgnoreCase(action.getName(), name)) {
                return action;
            }
        }
        return null;
    }

    @Override
    public String toString() {
        throw new RuntimeException();
    }

    public String toString(int indent) {
        // return "VariableInstance [name=" + name + ", dataType=" +
        // dataType.toString(indent) + ", value=" + value + "]";

        StringBuilder stringBuilder = new StringBuilder();

        DataType.addIndent(stringBuilder, indent);

        stringBuilder.append("VariableInstance [");
        stringBuilder.append(" name=\"" + name + "\"");
        stringBuilder.append(" dataType=\"" + dataType.getName() + "\"");
        stringBuilder.append(" value=\"" + value + "\"");

        for (VariableDescriptor elementVariableDescriptor : elements.values()) {
            stringBuilder.append("\n");
            stringBuilder.append(elementVariableDescriptor.toString(indent + 1));
        }

        stringBuilder.append(" ]");

        return stringBuilder.toString();
    }

}
