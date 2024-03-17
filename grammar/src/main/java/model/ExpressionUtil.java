package model;

import org.apache.commons.lang3.NotImplementedException;

import instance.VariableInstance;

public class ExpressionUtil {

    /**
     * ctor
     */
    private ExpressionUtil() {
        // no instances of this class
    }
    
    public static VariableInstance evaluateExpression(VariableInstance source, Expression expression) {

        switch (expression.getExpressionType()) {

            case VARIABLE_NAME:
                String[] variableSplit = expression.getVariableNameValue().split("\\.");
                VariableInstance target = source;
                for (int i = 0; i < variableSplit.length; i++) {
                    VariableInstance oldTarget = target;
                    VariableInstance tempTarget = target.getElement(variableSplit[i]);
                    if (tempTarget == null) {
                        String msg = "Cannot find \"" + variableSplit[i] + "\" in " + oldTarget.getName()
                                + " " + oldTarget.getDataType();
                        System.out.println(msg);
                        throw new RuntimeException(msg);
                    }
                    target = tempTarget;
                }

                return target;

            case AND: {
                Expression temp = expression.getExpressionList().get(0);
                VariableInstance lhsVariableInstance = evaluateExpression(source, temp);
                temp = expression.getExpressionList().get(1);
                VariableInstance rhsVariableInstance = evaluateExpression(source, temp);

                // // DEBUG
                // System.out.println("AND");
                // System.out.println(lhsVariableInstance.getName() + " " +
                // lhsVariableInstance.getValue());
                // System.out.println(rhsVariableInstance.getName() + " " +
                // rhsVariableInstance.getValue());

                boolean result = Boolean.parseBoolean(lhsVariableInstance.getValue())
                        & Boolean.parseBoolean(rhsVariableInstance.getValue());

                // // DEBUG
                // System.out.println("Result: " + result);

                VariableInstance variableInstance = new VariableInstance();
                variableInstance.setValue(Boolean.toString(result));

                return variableInstance;
            }

            case OR: {
                Expression temp = expression.getExpressionList().get(0);
                VariableInstance lhsVariableInstance = evaluateExpression(source, temp);
                temp = expression.getExpressionList().get(1);
                VariableInstance rhsVariableInstance = evaluateExpression(source, temp);

                // System.out.println("OR");
                // System.out.println(lhsVariableInstance.getName() + " " +
                // lhsVariableInstance.getValue());
                // System.out.println(rhsVariableInstance.getName() + " " +
                // rhsVariableInstance.getValue());

                boolean result = Boolean.parseBoolean(lhsVariableInstance.getValue())
                        || Boolean.parseBoolean(rhsVariableInstance.getValue());

                // System.out.println("Result: " + result);

                VariableInstance variableInstance = new VariableInstance();
                variableInstance.setValue(Boolean.toString(result));

                return variableInstance;
            }

            case NOT: {
                Expression temp = expression.getExpressionList().get(0);
                VariableInstance lhsVariableInstance = evaluateExpression(source, temp);

                boolean result = Boolean.parseBoolean(lhsVariableInstance.getValue());
                result = !result;
                // System.out.println(result);

                VariableInstance variableInstance = new VariableInstance();
                variableInstance.setValue(Boolean.toString(result));

                return variableInstance;
            }

            default:
                throw new NotImplementedException("\"" +
                        expression.getExpressionType() + "\" not implemented yet!");
        }
    }
}
