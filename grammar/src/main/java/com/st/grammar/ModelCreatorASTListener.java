package com.st.grammar;

import java.util.List;
import java.util.ArrayList;

import org.apache.commons.lang3.StringUtils;

import com.st.grammar.StructuredTextParser.VariableContext;

import model.AssignmentStatement;
import model.DataType;
import model.Expression;
import model.ExpressionType;
import model.Program;
import model.StatementType;
import model.Variable;

public class ModelCreatorASTListener extends StructuredTextBaseListener {

    private Program program;

    private Variable variable;

    private AssignmentStatement statement;

    private List<Expression> expressionList = new ArrayList<>();

    @Override
    public void enterProgram_declaration(StructuredTextParser.Program_declarationContext ctx) {
        // System.out.println(ctx.getClass().getSimpleName() + " " +
        // ctx.getStart().getText());

        program = new Program();
    }

    @Override
    public void exitProgram_declaration(StructuredTextParser.Program_declarationContext ctx) {
        System.out.println(program);
        program = null;
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
        System.out.println(ctx.getClass().getSimpleName() + " " + ctx.getStart().getText());

        // a new variable is defined
        variable = new Variable();
        program.getVariables().add(variable);

        variable.setName(ctx.getStart().getText());
    }

    @Override
    public void exitVar1_init_decl(StructuredTextParser.Var1_init_declContext ctx) {
        variable = null;
    }

    @Override
    public void enterSimple_spec_init(StructuredTextParser.Simple_spec_initContext ctx) {
        System.out.println(ctx.getClass().getSimpleName() + " " + ctx.getStart().getText());

        DataType dataType = DataType.valueOf(ctx.getStart().getText());
        variable.setDataType(dataType);
    }

    @Override
    public void exitStatement(StructuredTextParser.StatementContext ctx) {
        System.out.println(ctx.getClass().getSimpleName() + " " + ctx.getStart().getText());

        if (statement instanceof AssignmentStatement) {
            AssignmentStatement assignmentStatement = (AssignmentStatement) statement;
            assignmentStatement.setExpression(expressionList.get(0));

            assignmentStatement = null;
        }

        expressionList.clear();
    }

    @Override
    public void enterAssignment_statement(StructuredTextParser.Assignment_statementContext ctx) {
        System.out.println(ctx.getClass().getSimpleName() + " " + ctx.getStart().getText());

        System.out.println(ctx.variable());
        VariableContext variableContext = ctx.variable();
        final String varName = variableContext.getStart().getText();

        statement = new AssignmentStatement();
        program.getStatements().add(statement);

        statement.setStatementType(StatementType.ASSIGNMENT);
        statement.setVariableQualifier(varName);
    }

    @Override
    public void enterInteger(StructuredTextParser.IntegerContext ctx) {
        System.out.println(ctx.getClass().getSimpleName() + " " + ctx.getStart().getText());

        Expression expression = new Expression();
        expressionList.add(expression);

        expression.setExpressionType(ExpressionType.INTEGER_CONSTANT);
        expression.setIntegerValue(Integer.parseInt(ctx.getStart().getText()));
    }

    @Override
    public void enterBoolean_literal(StructuredTextParser.Boolean_literalContext ctx) {

        Expression expression = new Expression();
        expressionList.add(expression);

        expression.setExpressionType(ExpressionType.BOOLEAN_CONSTANT);
        expression.setBooleanValue(Boolean.parseBoolean(ctx.getStart().getText()));
    }

    @Override
    public void exitAdd_expression(StructuredTextParser.Add_expressionContext ctx) {

        Expression expression = new Expression();
        expression.setExpressionType(ExpressionType.ADD);
        expression.getExpressionList().addAll(expressionList);

        expressionList.clear();
        expressionList.add(expression);
    }

}
