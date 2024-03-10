package com.st.grammar;

import java.util.List;
import java.util.ArrayList;
import java.util.Stack;

import org.antlr.v4.runtime.tree.TerminalNode;
import org.apache.commons.lang3.StringUtils;

import com.st.grammar.StructuredTextParser.Data_sourceContext;
import com.st.grammar.StructuredTextParser.IntegerContext;

import model.AssignmentStatement;
import model.DataType;
import model.Expression;
import model.ExpressionType;
import model.FunctionBlock;
import model.GlobalScope;
import model.Program;
import model.ProgramConfiguration;
import model.RepeatStatement;
import model.Task;
import model.TypeScope;
import model.VarScope;
import model.Variable;
import model.Configuration;

public class ModelCreatorASTListener extends StructuredTextBaseListener {

    public Program program;

    public Variable variable;

    public DataType dataType;

    public DataType structureDataType;

    public List<Expression> expressionList = new ArrayList<>();

    public Expression comparisonExpression;

    public Stack<VarScope> scopeStack = new Stack<>();

    public GlobalScope globalVarScope = new GlobalScope();

    // private Stack<TypeScope> typeScopeStack = new Stack<>();

    public TypeScope globalTypeScope = new TypeScope();

    public boolean equalsDetected;

    public boolean notEqualsDetected;

    public Configuration configuration;

    public RepeatStatement repeatStatement;

    private FunctionBlock functionBlock;

    /**
     * ctor
     */
    public ModelCreatorASTListener() {

        scopeStack.push(globalVarScope);

        // BOOL
        DataType dataType = new DataType();
        dataType.setName("BOOL");
        globalTypeScope.addType(dataType.getName(), dataType);

        // SR FlipFlop (reset precedence)
        dataType = new DataType();
        dataType.setName("SR");
        globalTypeScope.addType(dataType.getName(), dataType);

        // TON Timer to ON (TRUE) (delays it's output going to HIGH/ON/TRUE for a
        // configurable amount of time adter the input goes high)
        dataType = new DataType();
        dataType.setName("TON");
        globalTypeScope.addType(dataType.getName(), dataType);

        // TIME
        dataType = new DataType();
        dataType.setName("TIME");
        globalTypeScope.addType(dataType.getName(), dataType);

        // typeScopeStack.push(globalTypeScope);
    }

    @Override
    public void enterFunction_block_declaration(StructuredTextParser.Function_block_declarationContext ctx) {

        final String typeName = ctx.IDENTIFIER().getText();

        // System.out.println(typeName);

        functionBlock = new FunctionBlock();
        functionBlock.setName(typeName);

        scopeStack.push(functionBlock);

    }

    @Override
    public void exitFunction_block_declaration(StructuredTextParser.Function_block_declarationContext ctx) {
        // System.out.println(functionBlock);

        globalTypeScope.addType(functionBlock.getName(), functionBlock);

        functionBlock = null;
    }

    // @Override
    // public void exitInput_declaration(StructuredTextParser.Input_declarationContext ctx) {

    //     System.out.println("");

    // }

    @Override
    public void enterStructure_type_declaration(StructuredTextParser.Structure_type_declarationContext ctx) {
        // System.out.println("");

        final String typeName = ctx.structure_type_name().getText();

        // System.out.println("typeName: " + typeName);

        structureDataType = new DataType();
        structureDataType.setName(typeName);

        // TypeScope typeScope = typeScopeStack.peek();
        // typeScope.addType(typeName, structureDataType);

        globalTypeScope.addType(typeName, structureDataType);
    }

    @Override
    public void exitStructure_type_declaration(StructuredTextParser.Structure_type_declarationContext ctx) {
        structureDataType = null;
    }

    @Override
    public void exitStructure_element_declaration(StructuredTextParser.Structure_element_declarationContext ctx) {
        // System.out.println("");

        String structureElementName = ctx.structure_element_name().getText();

        // System.out.println(dataType);

        structureDataType.addField(structureElementName, dataType);

        // System.out.println("");
    }

    @Override
    public void exitSimple_spec_init(StructuredTextParser.Simple_spec_initContext ctx) {
        // System.out.println(ctx.getClass().getSimpleName() + " " +
        // ctx.getStart().getText());

        // TypeScope typeScope = typeScopeStack.peek();
        TypeScope typeScope = globalTypeScope;

        String dataTypeAsString = ctx.getStart().getText();

        dataType = typeScope.retrieveDataTypeByTypeName(dataTypeAsString);
        if (dataType == null) {
            throw new RuntimeException("Type \"" + dataTypeAsString + "\" not defined!");
        }

        // dataType = DataType.valueOf(dataTypeAsString);
    }

    @Override
    public void enterProgram_declaration(StructuredTextParser.Program_declarationContext ctx) {
        // System.out.println(ctx.getClass().getSimpleName() + " " +
        // ctx.getStart().getText());

        program = new Program();
        scopeStack.push(program);
    }

    @Override
    public void exitProgram_declaration(StructuredTextParser.Program_declarationContext ctx) {

        // System.out.println(program);

        // program = null;
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

        VarScope topScope = scopeStack.peek();

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
    public void exitAssignment_statement(StructuredTextParser.Assignment_statementContext ctx) {

        AssignmentStatement assignmentStatement = new AssignmentStatement();
        scopeStack.peek().addStatement(assignmentStatement);

        // the expression is added into the statement in exitStatement
        // use the parse expression and copy it into the statement
        // assignmentStatement.setExpression(comparisonExpression);
        assignmentStatement.getExpressionList().addAll(expressionList);

        expressionList.clear();
    }

    @Override
    public void enterRepeat_statement(StructuredTextParser.Repeat_statementContext ctx) {
        // System.out.println(ctx.getClass().getSimpleName() + " " +
        // ctx.getStart().getText());

        repeatStatement = new RepeatStatement();

        // repeat statement is the new scope
        scopeStack.push(repeatStatement);
    }

    @Override
    public void exitRepeat_statement(StructuredTextParser.Repeat_statementContext ctx) {

        // RepeatStatement repeatStatement = (RepeatStatement) scopeStack.pop();

        repeatStatement.setTerminationCondition(comparisonExpression);
        comparisonExpression = null;

        scopeStack.pop();
        scopeStack.peek().addStatement(repeatStatement);

        repeatStatement = null;
    }

    @Override
    public void exitComparison(StructuredTextParser.ComparisonContext ctx) {
        // System.out.println(ctx.getClass().getSimpleName() + " " +
        // ctx.getStart().getText());

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
        // System.out.println("BooleanLiteral: " + ctx.getStart().getText() + " Line: "
        // + ctx.start.getLine() + " pos: "
        // + ctx.start.getCharPositionInLine());

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
        // System.out.println(ctx.getClass().getSimpleName() + " " +
        // ctx.getStart().getText());

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

    @Override
    public void enterConfiguration_declaration(StructuredTextParser.Configuration_declarationContext ctx) {
        configuration = new Configuration();
    }

    @Override
    public void exitConfiguration_name(StructuredTextParser.Configuration_nameContext ctx) {
        configuration.setName(ctx.getText());
    }

    @Override
    public void enterTask_configuration(StructuredTextParser.Task_configurationContext ctx) {
        // System.out.println(ctx.getClass().getSimpleName() + " " +
        // ctx.getStart().getText());

        Task task = new Task();

        configuration.setTask(task);

        String taskName = ctx.task_name().getText();
        task.setName(taskName);

        // System.out.println("taskName: " + taskName);
    }

    @Override
    public void exitTask_initialization(StructuredTextParser.Task_initializationContext ctx) {
        // System.out.println(ctx.getClass().getSimpleName() + " " +
        // ctx.getStart().getText());

        IntegerContext priorityIntegerContext = ctx.integer();
        // System.out.println("Priority: " + priorityIntegerContext.getText());
        configuration.getTask().setPriority(Integer.parseInt(priorityIntegerContext.getText()));

        List<Data_sourceContext> data_sourceContextList = (List<Data_sourceContext>) ctx.data_source();

        for (Data_sourceContext data_sourceContext : data_sourceContextList) {
            // System.out.println("INTERVAL: " + data_sourceContext.constant().getText());
            configuration.getTask().setInterval(data_sourceContext.constant().getText());
        }
    }

    @Override
    public void exitProgram_configuration(StructuredTextParser.Program_configurationContext ctx) {

        System.out.println("ProgramName: " + ctx.program_name().getText());
        System.out.println("TaskName: " + ctx.task_name().getText());
        System.out.println("ProgramType: " + ctx.program_type_name().getText());

        ProgramConfiguration programConfiguration = new ProgramConfiguration();
        programConfiguration.setProgramName(ctx.program_name().getText());
        programConfiguration.setTaskName(ctx.task_name().getText());
        programConfiguration.setProgramType(ctx.program_type_name().getText());

        configuration.setProgramConfiguration(programConfiguration);
    }

    @Override
    public void visitTerminal(TerminalNode node) {
        // System.out.println("TERMINAL: " + node.getText());

        if (StringUtils.equalsIgnoreCase("=", node.getText())) {
            equalsDetected = true;
        }
        if (StringUtils.equalsIgnoreCase("<>", node.getText())) {
            notEqualsDetected = true;
        }
    }
}
