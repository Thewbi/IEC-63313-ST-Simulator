package com.st.grammar;

import java.util.List;
import java.util.ArrayList;
import java.util.Stack;

import org.antlr.v4.runtime.tree.TerminalNode;
import org.apache.commons.lang3.StringUtils;

import com.st.grammar.StructuredTextParser.VariableContext;

import model.ArithmeticStatement;
import model.AssignmentStatement;
import model.DataType;
import model.Expression;
import model.ExpressionType;
import model.Program;
import model.RepeatStatement;
import model.Scope;
import model.Statement;
import model.StatementType;
import model.Variable;

public class ModelCreatorASTListener extends StructuredTextBaseListener {

    private Program program;

    private Variable variable;

    private DataType dataType = DataType.UNKNOWN;

    // private Statement statement;

    private List<Expression> expressionList = new ArrayList<>();

    // private List<Statement> statementList = new ArrayList<>();

    private Expression variableExpression;

    private Expression assignmentExpression;

    private Expression comparisonExpression;

    private Stack<Scope> scopeStack = new Stack<>();

    private boolean equalsDetected;

    private boolean notEqualsDetected;

    // private boolean addOperatorFound;

    // private boolean subtractOperatorFound;

    @Override
    public void enterProgram_declaration(StructuredTextParser.Program_declarationContext ctx) {
        // System.out.println(ctx.getClass().getSimpleName() + " " +
        // ctx.getStart().getText());

        program = new Program();
        scopeStack.push(program);
    }

    @Override
    public void exitProgram_declaration(StructuredTextParser.Program_declarationContext ctx) {

        System.out.println(program);

        program = null;
        scopeStack.pop();
    }

    @Override
    public void enterProgram_type_name(StructuredTextParser.Program_type_nameContext ctx) {

        // System.out.println(ctx.getClass().getSimpleName() + " " +
        // ctx.getStart().getText());

        // this method is called for configurations and program_declarations!
        if (program != null) {
            program.setName(StringUtils.trim(ctx.getStart().getText()));
        }
    }

    @Override
    public void enterVar1_init_decl(StructuredTextParser.Var1_init_declContext ctx) {
        // System.out.println(ctx.getClass().getSimpleName() + " " +
        // ctx.getStart().getText());

        variable = null;
    }

    @Override
    public void exitVar1_init_decl(StructuredTextParser.Var1_init_declContext ctx) {

        Scope topScope = scopeStack.peek();

        // a new variable is defined
        variable = new Variable();
        topScope.getVariables().add(variable);

        variable.setName(ctx.getStart().getText());
        variable.setDataType(dataType);
        dataType = null;

        expressionList.clear();

        variable = null;
    }

    @Override
    public void exitSimple_spec_init(StructuredTextParser.Simple_spec_initContext ctx) {
        // System.out.println(ctx.getClass().getSimpleName() + " " +
        // ctx.getStart().getText());

        dataType = DataType.valueOf(ctx.getStart().getText());
        // variable.setDataType(dataType);
    }

    // @Override
    // public void exitStatement(StructuredTextParser.StatementContext ctx) {
    // System.out.println(ctx.getClass().getSimpleName() + " " +
    // ctx.getStart().getText());

    // if (statement instanceof AssignmentStatement) {
    // AssignmentStatement assignmentStatement = (AssignmentStatement) statement;

    // // use the parse expression and copy it into the statement
    // assignmentStatement.setExpression(expressionList.get(0));

    // assignmentStatement = null;
    // }

    // // clear the expression list so it is not accidently used by the next
    // statement
    // expressionList.clear();
    // }

    @Override
    public void exitAssignment_statement(StructuredTextParser.Assignment_statementContext ctx) {

        AssignmentStatement assignmentStatement = new AssignmentStatement();
        // statementList.add(assignmentStatement);
        // statement = assignmentStatement;
        scopeStack.peek().addStatement(assignmentStatement);

        // // insert variable name
        // System.out.println(ctx.variable());
        // VariableContext variableContext = ctx.variable();
        // final String varName = variableContext.getStart().getText();
        // assignmentStatement.setVariableQualifier(varName);

        // the expression is added into the statement in exitStatement
        // use the parse expression and copy it into the statement
        // assignmentStatement.setExpression(comparisonExpression);
        assignmentStatement.getExpressionList().addAll(expressionList);

        expressionList.clear();
    }

    // @Override
    // public void
    // exitAssignment_statement(StructuredTextParser.Assignment_statementContext
    // ctx) {
    // Scope topScope = scopeStack.peek();

    // AssignmentStatement assignmentStatement = new AssignmentStatement();
    // // statement = assignmentStatement;

    // assignmentStatement.setVariableQualifier(variableExpression.getVariableNameValue());
    // assignmentStatement.setExpression(assignmentExpression);

    // topScope.addStatement(assignmentStatement);
    // }

    // @Override
    // public void exitVariable(StructuredTextParser.VariableContext ctx) {

    // // AssignmentStatement assignmentStatement = (AssignmentStatement) statement;

    // // Expression expression = expressionList.get(0);
    // //
    // assignmentStatement.setVariableQualifier(expression.getVariableNameValue());

    // variableExpression = expressionList.get(0);

    // expressionList.clear();
    // }

    // @Override
    // public void
    // exitAssignment_statement(StructuredTextParser.Assignment_statementContext
    // ctx) {
    // System.out.println(ctx.getClass().getSimpleName() + " " +
    // ctx.getStart().getText());

    // Scope topScope = scopeStack.peek();

    // AssignmentStatement assignmentStatement = new AssignmentStatement();
    // statement = assignmentStatement;
    // topScope.addStatement(assignmentStatement);

    // // insert variable name
    // System.out.println(ctx.variable());
    // VariableContext variableContext = ctx.variable();
    // final String varName = variableContext.getStart().getText();
    // assignmentStatement.setVariableQualifier(varName);

    // // the expression is added into the statement in exitStatement
    // // use the parse expression and copy it into the statement
    // assignmentStatement.setExpression(expressionList.get(0));
    // expressionList.clear();
    // }

    @Override
    public void enterRepeat_statement(StructuredTextParser.Repeat_statementContext ctx) {
        // System.out.println(ctx.getClass().getSimpleName() + " " +
        // ctx.getStart().getText());

        Scope topScope = scopeStack.peek();

        RepeatStatement repeatStatement = new RepeatStatement();
        // statement = repeatStatement;
        // topScope.addStatement(repeatStatement);

        // repeat statement is the new scope
        scopeStack.push(repeatStatement);
    }

    @Override
    public void exitRepeat_statement(StructuredTextParser.Repeat_statementContext ctx) {

        RepeatStatement repeatStatement = (RepeatStatement) scopeStack.pop();

        // RepeatStatement repeatStatement = (RepeatStatement) statement;

        // repeatStatement.getStatements().addAll(statementList);
        // statementList.clear();

        repeatStatement.setTerminationCondition(comparisonExpression);
        comparisonExpression = null;

        scopeStack.peek().addStatement(repeatStatement);

    }

    // @Override
    // public void exitExpression(StructuredTextParser.ExpressionContext ctx) {
    // // System.out.println(ctx.getClass().getSimpleName() + " " +
    // // ctx.getStart().getText());

    // // System.out.println(expressionList);
    // // expressionList.clear();

    // // AssignmentStatement assignmentStatement = (AssignmentStatement) statement;

    // // assignmentExpression = expressionList.get(0);

    // // System.out.println(assignmentExpression);
    // // assignmentStatement.setExpression(expression);

    // // expressionList.clear();
    // }

    @Override
    public void exitComparison(StructuredTextParser.ComparisonContext ctx) {
        // System.out.println(ctx.getClass().getSimpleName() + " " +
        // ctx.getStart().getText());

        // // if the is only a single operand, no comparison node is required
        // if (expressionList.size() <= 1) {
        // return;
        // }

        if ((!equalsDetected) && (!notEqualsDetected)) {
            return;
        }

        comparisonExpression = new Expression();
        if (equalsDetected) {
            comparisonExpression.setExpressionType(ExpressionType.EQUALS_COMPARISON);
        }
        if (notEqualsDetected) {
            comparisonExpression.setExpressionType(ExpressionType.NOT_EQUALS_COMPARISON);
        }
        comparisonExpression.getExpressionList().addAll(expressionList);

        expressionList.clear();

        // expressionList.add(expression);

        // Scope topScope = scopeStack.peek();
        // System.out.println(topScope);

        // System.out.println(statement);

        // topScope.addStatement(statement);
    }

    @Override
    public void enterInteger(StructuredTextParser.IntegerContext ctx) {
        // System.out.println(ctx.getClass().getSimpleName() + " " +
        // ctx.getStart().getText());

        Expression expression = new Expression();
        expressionList.add(expression);

        expression.setExpressionType(ExpressionType.INTEGER_CONSTANT);
        expression.setIntegerValue(Integer.parseInt(ctx.getStart().getText()));
    }

    @Override
    public void enterBoolean_literal(StructuredTextParser.Boolean_literalContext ctx) {

        System.out.println("BooleanLiteral: " + ctx.getStart().getText() + " Line: " + ctx.start.getLine() + " pos: "
                + ctx.start.getCharPositionInLine());

        Expression expression = new Expression();
        expressionList.add(expression);

        expression.setExpressionType(ExpressionType.BOOLEAN_CONSTANT);
        expression.setBooleanValue(Boolean.parseBoolean(ctx.getStart().getText()));
    }

    @Override
    public void exitVariable_name(StructuredTextParser.Variable_nameContext ctx) {

        String varName = ctx.getStart().getText();

        Expression expression = new Expression();
        expressionList.add(expression);

        expression.setExpressionType(ExpressionType.VARIABLE_NAME);
        expression.setVariableNameValue(varName);
    }

    @Override
    public void exitAdd_expression(StructuredTextParser.Add_expressionContext ctx) {
        System.out.println(ctx.getClass().getSimpleName() + " " +
                ctx.getStart().getText());

        if (ctx.add_operator() == null) {
            return;
        }

        String operatorAsString = ctx.add_operator().getText();

        Expression arithmeticExpression = new Expression();

        if (StringUtils.equalsIgnoreCase(operatorAsString, "+")) {
            arithmeticExpression.setExpressionType(ExpressionType.ADD);
        } else if (StringUtils.equalsIgnoreCase(operatorAsString, "-")) {
            arithmeticExpression.setExpressionType(ExpressionType.SUBTRACT);
        }

        arithmeticExpression.getExpressionList().add(expressionList.get(1));
        expressionList.remove(1);

        arithmeticExpression.getExpressionList().add(expressionList.get(1));
        expressionList.remove(1);

        expressionList.add(arithmeticExpression);
    }

    // @Override
    // public void exitAdd_expression(StructuredTextParser.Add_expressionContext
    // ctx) {

    // // if the is only a single operand, no and node is required
    // if ((!addOperatorFound) && (!subtractOperatorFound)) {
    // return;
    // }

    // // build a new Add-Expression and copy all the expression parsed so far into
    // it
    // Expression expression = new Expression();
    // if (addOperatorFound) {
    // expression.setExpressionType(ExpressionType.ADD);
    // }
    // if (subtractOperatorFound) {
    // expression.setExpressionType(ExpressionType.SUBTRACT);
    // }
    // expression.getExpressionList().addAll(expressionList);

    // // remove the expressions parsed so far
    // expressionList.clear();

    // // place the new add expression into the list so it can be used by further
    // // expressions
    // expressionList.add(expression);

    // addOperatorFound = false;
    // subtractOperatorFound = false;
    // }

    // @Override
    // public void exitAdd_operator(StructuredTextParser.Add_operatorContext ctx) {

    // System.out.println(ctx.getStart().getText());

    // String operatorAsString = ctx.getStart().getText();

    // if (StringUtils.equalsIgnoreCase(operatorAsString, "+")) {
    // addOperatorFound = true;
    // } else if (StringUtils.equalsIgnoreCase(operatorAsString, "-")) {
    // subtractOperatorFound = true;
    // }
    // }

    @Override
    public void visitTerminal(TerminalNode node) {
        // System.out.println("TERMINAL: " + node.getText());

        // if (StringUtils.equalsIgnoreCase("UNTIL", node.getText())) {
        // Scope scope = scopeStack.peek();
        // if (scope instanceof RepeatStatement) {
        // RepeatStatement repeatStatement = (RepeatStatement) scope;
        // repeatStatement.setUntilDetected(true);
        // }
        // }

        if (StringUtils.equalsIgnoreCase("=", node.getText())) {
            equalsDetected = true;
        }
        if (StringUtils.equalsIgnoreCase("<>", node.getText())) {
            notEqualsDetected = true;
        }
    }

}
