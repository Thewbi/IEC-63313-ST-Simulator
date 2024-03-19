package model;

import java.util.List;
import java.util.Map;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;

import instance.TONVariableInstance;
import instance.VariableDescriptor;
import instance.VariableInstance;

public interface StatementContainer {

    default public void executeStatements(TypeScope globalTypeScope, VariableInstance contextVariableInstance,
            List<Statement> statements) {

        for (Statement statement : statements) {

            switch (statement.getStatementType()) {

                case ASSIGNMENT: {
                    AssignmentStatement assignmentStatement = (AssignmentStatement) statement;
                    String variableAsString = assignmentStatement.getVariable();

                    // find the variable that is assigned a new value
                    String[] variableSplit = variableAsString.split("\\.");

                    VariableInstance target = contextVariableInstance;
                    boolean isReturn = false;

                    // check if a value is assigned to the name of the function
                    // if a value is assigned to the name of a function, then this is equivalent
                    // to returning a value from the function
                    if (contextVariableInstance.getDataType() instanceof Function) {
                        if (StringUtils.equalsIgnoreCase(variableSplit[0], contextVariableInstance.getName())) {
                            // System.out.println("Return");
                            isReturn = true;
                        }
                    }

                    if (!isReturn) {

                        for (int i = 0; i < variableSplit.length; i++) {

                            VariableInstance tempTarget = target.getElement(variableSplit[i]);

                            if (tempTarget == null) {
                                String msg = "Cannot find \"" + variableSplit[i] + "\" in \"" + target.getName() + "\"";
                                System.out.println(msg);
                                throw new RuntimeException(msg);
                            }

                            target = tempTarget;
                        }

                    }

                    // System.out.println(target);

                    if (target == null) {
                        throw new RuntimeException("");
                    }

                    // // DEBUG
                    // System.out.println(target.getName());
                    // if (target.getName().equals("Stoerung")) {
                    // System.out.println("test");
                    // }

                    // find the value to assign
                    VariableInstance source = contextVariableInstance;
                    for (Expression expression : assignmentStatement.getExpressionList()) {
                        source = ExpressionUtil.evaluateExpression(globalTypeScope, source, expression);
                    }
                    // System.out.println(source);

                    // assign the value
                    target.setValue(source.getValue());

                    // System.out.println("source");
                }
                    break;

                case SUBPROGRAM_CONTROL: {
                    SubprogramControlStatement subprogramControlStatement = (SubprogramControlStatement) statement;

                    // find the function block
                    String subprogramNameAsString = subprogramControlStatement.getSubprogramName();
                    String[] subProgSplit = subprogramNameAsString.split("\\.");

                    VariableInstance functionBlock = contextVariableInstance;
                    for (int i = 0; i < subProgSplit.length; i++) {
                        functionBlock = functionBlock.getElement(subProgSplit[i]);
                    }

                    executeStatements(globalTypeScope, functionBlock, contextVariableInstance,
                            subprogramControlStatement.getParameterAssignments());
                }
                    break;

                default:
                    throw new RuntimeException(statement.getStatementType() + " Not implemented yet!");
            }
        }
    }

    public default void executeStatements(TypeScope globalTypeScope, VariableInstance variableInstance,
            VariableInstance parentVariableInstance,
            List<ParameterAssignment> parameterAssignments) {

        //
        // TODO: reset all values to their defaults / initial values
        //

        //
        // load input variables with values from the parent
        //

        if (parentVariableInstance != null && CollectionUtils.isNotEmpty(parameterAssignments)) {

            DataType dataType = variableInstance.getDataType();
            VarScope varScope = (VarScope) dataType;

            for (ParameterAssignment parameterAssignment : parameterAssignments) {

                Variable variableFromDataType = varScope.getVariableByName(parameterAssignment.getParameterName());

                if (variableFromDataType != null && variableFromDataType.isInOut()) {
                    // System.out.println("Reference Semantics!");

                    VariableInstance source = parentVariableInstance.getElement(parameterAssignment.getValue());

                    variableInstance.setElement(parameterAssignment.getParameterName(), source);
                } else {
                    // System.out.println("Copy Semantics!");
                    copySemantics(variableInstance, parentVariableInstance, parameterAssignment);
                }

            }
        }

        //
        // intrinsic blocks (TON, SR, RS, ...)
        //

        if (variableInstance.getDataType() != null) {

            String dataTypeAsString = variableInstance.getDataType().getName();

            if (StringUtils.equalsIgnoreCase(dataTypeAsString, "SR")) {

                boolean s1 = Boolean.parseBoolean(variableInstance.getElement("S1").getValue());
                boolean r = Boolean.parseBoolean(variableInstance.getElement("R").getValue());

                //boolean newValue = false;
                boolean newValue = Boolean.valueOf(variableInstance.getElement("Q1").getValue());

                // implement reset dominance by putting the reset operation last
                if (s1) {
                    newValue = true;
                }
                if (r) {
                    newValue = false;
                }

                variableInstance.getElement("Q1").setValue(Boolean.toString(newValue));

                // DEBUG output the flip flop state
                System.out.println(variableInstance.getName() + " [S1: " + s1 + "] [R: " + r + "] [value: " + newValue + "]");

            } else if (StringUtils.equalsIgnoreCase(dataTypeAsString, "RS")) {

                boolean s = Boolean.parseBoolean(variableInstance.getElement("S").getValue());
                boolean r1 = Boolean.parseBoolean(variableInstance.getElement("R1").getValue());

                // boolean newValue = false;
                boolean newValue = Boolean.valueOf(variableInstance.getElement("Q1").getValue());

                // implement set dominance by putting the set operation last
                if (r1) {
                    newValue = false;
                }
                if (s) {
                    newValue = true;
                }

                variableInstance.getElement("Q1").setValue(Boolean.toString(newValue));

                // DEBUG output the flip flop state
                // System.out.println(variableInstance.getName() + " S: " + s + " R1: " + r1 + " value: " + newValue);

            } else if (StringUtils.equalsIgnoreCase(dataTypeAsString, "TON")) {

                // System.out.println("TON: " + variableInstance.getName());

                String inValue = variableInstance.getElement("IN").getValue();
                // System.out.println("IN: " + inValue);

                // VariableInstance outputVariableInstance = variableInstance.getElement("Q");

                variableInstance.getElement("PT").getValue();

                VariableInstance currentValueVariableInstance = variableInstance.getElement("currentValue");
                String currentValue = currentValueVariableInstance.getValue();
                // System.out.println("currentValue: " + currentValue);

                boolean currentlyLow = StringUtils.isBlank(currentValue)
                        || StringUtils.equalsIgnoreCase(currentValue, "false");
                boolean requestedHigh = StringUtils.isNotBlank(inValue)
                        && StringUtils.equalsIgnoreCase(inValue, "true");

                boolean risingEdgeOnInput = currentlyLow && requestedHigh;
                // System.out.println("risingEdgeOnInput: " + risingEdgeOnInput);

                boolean fallingEdgeOnInput = !currentlyLow && !requestedHigh;
                // System.out.println("fallingEdgeOnInput: " + fallingEdgeOnInput);

                if (risingEdgeOnInput) {

                    // System.out.println("Starting TON timer!");

                    // DEBUG output the TON state
                    // System.out.println("TON \"" + variableInstance.getName() + "\" goes to true");

                    // the TON internally changes state to true
                    currentValueVariableInstance.setValue("true");

                    // // external value goes to true
                    // VariableInstance outputVariableInstance = variableInstance.getElement("Q");
                    // outputVariableInstance.setValue("false");

                    TONVariableInstance tonVariableInstance = (TONVariableInstance) variableInstance;
                    tonVariableInstance.startTimer();
                }

                if (fallingEdgeOnInput) {

                    // System.out.println("Stopping TON timer!");

                    // DEBUG output the TON state
                    // System.out.println("TON \"" + variableInstance.getName() + "\" goes to false");

                    // the TON internally changes state to false
                    currentValueVariableInstance.setValue("false");

                    // external value goes to false
                    VariableInstance outputVariableInstance = variableInstance.getElement("Q");
                    outputVariableInstance.setValue("false");

                    TONVariableInstance tonVariableInstance = (TONVariableInstance) variableInstance;
                    tonVariableInstance.stopTimer();
                }
            }
        }

        //
        // execute statements
        //

        variableInstance.executeStatements(globalTypeScope, variableInstance, variableInstance.getStatements());

        //
        // execute sequential function chart (SFC) state machine if the function block contains one
        //

        if (variableInstance.getDataType() instanceof FunctionBlock) {
            variableInstance.executeStateMachine(globalTypeScope);
        }
    }

    public default void copySemantics(VariableInstance variableInstance, VariableInstance parentVariableInstance,
            ParameterAssignment parameterAssignment) {

        String pathToObject = parameterAssignment.getValue();
        String[] pathToObjectSplit = StringUtils.split(pathToObject, "\\.");

        VariableInstance source = parentVariableInstance;
        for (String path : pathToObjectSplit) {

            // check for constant values
            if (path.startsWith("T#")) {

                // // convert to millis
                // long timeInMillis = TimeUtils.timeToMillis(path);

                source = new VariableInstance();
                // source.setValue(Long.toString(timeInMillis));
                source.setValue(path);
                break;
            }

            VariableInstance tempCurr = source.getElement(path);
            if (tempCurr == null) {
                throw new RuntimeException("Cannot find \"" + path + "\" in \""
                        + source.getName() + "\"");
            }

            source = tempCurr;
        }

        if (source == null) {
            throw new RuntimeException("Cannot find \"" + parameterAssignment.getValue() + "\" in \""
                    + parentVariableInstance.getName() + "\"");
        }

        VariableInstance target = variableInstance.getElement(parameterAssignment.getParameterName());
        target.setValue(source.getValue());

        for (Map.Entry<String, VariableDescriptor> entry : source.getElements().entrySet()) {

            VariableDescriptor sourceDescriptor = entry.getValue();
            VariableInstance sourceElement = sourceDescriptor.variableInstance;
            VariableInstance targetElement = target.getElement(sourceElement.getName());

            // copy the value
            targetElement.setValue(sourceElement.getValue());
        }
    }

    List<Statement> getStatements();

}
