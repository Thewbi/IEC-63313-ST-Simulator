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

public class VariableInstance implements StatementContainer {

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
    public void executeStateMachine() {

        if (MapUtils.isEmpty(steps)) {
            return;
        }

        // System.out.println("executeStateMachine()");

        // 1. All actions of the entire SequentialFunctionChart are set to
        // isExecuted(false);
        for (Action action : allActions) {
            action.setExecuted(false);
        }

        // 2. the global actions of the SequentialFunctionChart function block are
        // executed.
        // After execution, isExecuted(true) is called.
        for (Action action : globalActions) {
            action.execute();
        }

        // 3. all steps in the current step list are executed.
        for (Step step : currentSteps) {
            System.out.println("Executing step: \"" + step.getName() + "\"");
            step.execute(this);
        }

        // 5. the global actions of the SequentialFunctionChart function block are
        // executed unless they have already
        // been executed: isExecuted(true).
        for (Action action : globalActions) {
            action.execute();
        }

        // 7. All transitions are evaluated. For each transition that is activated</li>
        // - the old state is removed from the current step list
        // - the new state is added to the current step list
        List<Step> stepsToAdd = new ArrayList<>();
        List<Step> stepsToRemove = new ArrayList<>();
        for (Step step : currentSteps) {
            for (Transition transition : step.getTransistions()) {

                boolean evalResult = transition.evaluate(this);

                // System.out.println("evalResult: " + evalResult);

                if (evalResult) {

                    // System.out.println("TRUE: " + evalResult);

                    // TODO
                    // - the old state is removed from the current step list
                    // - the new state is added to the current step list

                    stepsToRemove.add(step);
                    for (String targetStepName : transition.getTargetStepNames()) {
                        stepsToAdd.add(this.getSteps().get(targetStepName));
                    }
                }
            }
        }

        // System.out.println(stepsToRemove);
        currentSteps.removeAll(stepsToRemove);

        // System.out.println(stepsToAdd);
        currentSteps.addAll(stepsToAdd);

        // System.out.println("");
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

}
