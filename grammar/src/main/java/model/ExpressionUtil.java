package model;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;

import grammar.Main;
import instance.VariableDescriptor;
import instance.VariableInstance;

public class ExpressionUtil {

    // private final static boolean DEBUG_EXPRESSIONS = true;
    private final static boolean DEBUG_EXPRESSIONS = false;

    /**
     * ctor
     */
    private ExpressionUtil() {
        // no instances of this class
    }

    public static VariableInstance evaluateExpression(TypeScope globalTypeScope, VariableInstance source,
            Expression expression) {

        switch (expression.getExpressionType()) {

            case VARIABLE_NAME: {

                VariableInstance target = source;

                String[] variableSplit = expression.getVariableNameValue().split("\\.");

                // // TODO: REQUIRED FOR FUNCTION
                // if (variableSplit.length == 1) {
                // return target;
                // }

                for (int i = 0; i < variableSplit.length; i++) {

                    if (StringUtils.equalsIgnoreCase(target.getName(), variableSplit[i])) {
                        return target;
                    }

                    VariableInstance oldTarget = target;
                    VariableInstance tempTarget = target.getElement(variableSplit[i]);
                    if (tempTarget == null) {
                        String msg = "Cannot find \"" + variableSplit[i] + "\" in " + oldTarget.getName()
                                + " " + oldTarget.getDataType().toString(1);
                        System.out.println(msg);
                        throw new RuntimeException(msg);
                    }
                    target = tempTarget;
                }

                return target;
            }

            case AND: {
                Expression temp = expression.getExpressionList().get(0);
                VariableInstance lhsVariableInstance = evaluateExpression(globalTypeScope, source, temp);
                temp = expression.getExpressionList().get(1);
                VariableInstance rhsVariableInstance = evaluateExpression(globalTypeScope, source, temp);

                if (DEBUG_EXPRESSIONS) {
                    System.out.println("AND");
                    System.out.println("  " + lhsVariableInstance.getName() + " " +
                            lhsVariableInstance.getValue());
                    System.out.println("  " + rhsVariableInstance.getName() + " " +
                            rhsVariableInstance.getValue());
                }

                boolean result = Boolean.parseBoolean(lhsVariableInstance.getValue())
                        & Boolean.parseBoolean(rhsVariableInstance.getValue());

                if (DEBUG_EXPRESSIONS) {
                    System.out.println("  " + "Result: " + result);
                }

                VariableInstance variableInstance = new VariableInstance();
                variableInstance.setValue(Boolean.toString(result));

                return variableInstance;
            }

            case OR: {
                Expression temp = expression.getExpressionList().get(0);
                VariableInstance lhsVariableInstance = evaluateExpression(globalTypeScope, source, temp);
                temp = expression.getExpressionList().get(1);
                VariableInstance rhsVariableInstance = evaluateExpression(globalTypeScope, source, temp);

                if (DEBUG_EXPRESSIONS) {
                    System.out.println("OR");
                    System.out.println("  " + lhsVariableInstance.getName() + " " +
                            lhsVariableInstance.getValue());
                    System.out.println("  " + rhsVariableInstance.getName() + " " +
                            rhsVariableInstance.getValue());
                }

                boolean result = Boolean.parseBoolean(lhsVariableInstance.getValue())
                        || Boolean.parseBoolean(rhsVariableInstance.getValue());

                if (DEBUG_EXPRESSIONS) {
                    System.out.println("  " + "Result: " + result);
                }

                VariableInstance variableInstance = new VariableInstance();
                variableInstance.setValue(Boolean.toString(result));

                return variableInstance;
            }

            case NOT: {
                Expression temp = expression.getExpressionList().get(0);
                VariableInstance lhsVariableInstance = evaluateExpression(globalTypeScope, source, temp);

                boolean result = Boolean.parseBoolean(lhsVariableInstance.getValue());
                result = !result;

                if (DEBUG_EXPRESSIONS) {
                    System.out.println("NOT");
                    System.out.println("  " + lhsVariableInstance.getName() + " " +
                            lhsVariableInstance.getValue());
                }
                    

                VariableInstance variableInstance = new VariableInstance();
                variableInstance.setValue(Boolean.toString(result));

                if (DEBUG_EXPRESSIONS) {
                    System.out.println("  " + "Result: " + result);
                }

                return variableInstance;
            }

            case FUNCTION_CALL: {

                String varName = expression.getVariableNameValue();
                Function function = (Function) globalTypeScope.get(varName);

                List<ParameterAssignment> parameterAssignments = new ArrayList<>();

                if (CollectionUtils.isNotEmpty(expression.getParameterAssignments())) {
                    parameterAssignments = expression.getParameterAssignments();
                } else {

                    List<Expression> assignments = expression.getExpressionList();

                    int i = 0;
                    for (Expression assignmentExpression : assignments) {

                        ParameterAssignment parameterAssignment = new ParameterAssignment();
                        parameterAssignments.add(parameterAssignment);

                        Variable formalParameter = function.getVariables().get(i);
                        parameterAssignment.setParameterName(formalParameter.getName());
                        parameterAssignment.setValue(assignmentExpression.getVariableNameValue());

                        i++;
                    }
                }

                VariableInstance funcVariableInstance = new VariableInstance();
                funcVariableInstance.setName(function.getName());
                funcVariableInstance.setDataType(function);
                for (Variable varName2 : function.getVariables()) {

                    VariableInstance temp = new VariableInstance();
                    temp.setName(varName2.getName());
                    temp.setDataType(varName2.getDataType());

                    if (varName2.getDataType() instanceof Struct) {
                        Main.instantiateStruct(varName2, temp);
                    }

                    VariableDescriptor variabelDescriptor = new VariableDescriptor();
                    variabelDescriptor.variableInstance = temp;
                    funcVariableInstance.getElements().put(temp.getName(), variabelDescriptor);
                }
                funcVariableInstance.getStatements().addAll(function.getStatements());
                funcVariableInstance.executeStatements(globalTypeScope, funcVariableInstance, source,
                        parameterAssignments);

                VariableInstance variableInstance = new VariableInstance();
                variableInstance.setValue("RRRRRRRR");

                return variableInstance;
            }

            default:
                throw new RuntimeException("\"" +
                        expression.getExpressionType() + "\" not implemented yet!");
        }
    }
}
