package grammar;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.Map;
import java.util.List;

import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.tree.ParseTreeWalker;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.NotImplementedException;
import org.apache.commons.lang3.StringUtils;

import com.st.grammar.ASTListener;
import com.st.grammar.DefaultStructuredTextListener;
import com.st.grammar.ModelCreatorASTListener;
import com.st.grammar.StructuredTextLexer;
import com.st.grammar.StructuredTextParser;
import com.st.grammar.StructuredTextParser.Assignment_statementContext;
import com.st.grammar.StructuredTextParser.Case_statementContext;
import com.st.grammar.StructuredTextParser.Compilation_unitContext;
import com.st.grammar.StructuredTextParser.Data_type_declarationContext;
import com.st.grammar.StructuredTextParser.Function_block_declarationContext;
import com.st.grammar.StructuredTextParser.Interface_declarationContext;
import com.st.grammar.StructuredTextParser.Program_declarationContext;

import instance.ConfigurationInstance;
import instance.ProgramInstance;
import instance.RSVariableInstance;
import instance.SRVariableInstance;
import instance.TONVariableInstance;
import instance.VariableDescriptor;
import instance.VariableInstance;
import model.AssignmentStatement;
import model.BooleanDataType;
import model.DataType;
import model.Expression;
import model.Field;
import model.FunctionBlock;
import model.ParameterAssignment;
import model.Program;
import model.Statement;
import model.Struct;
import model.SubprogramControlStatement;
import model.TimeDataType;
import model.TypeScope;
import model.UIntDataType;
import model.VarScope;
import model.Variable;

import javax.swing.*;

public class Main {

    public static void main(String[] args) throws IOException {

        System.out.println("Start");

        application();

        /*
         * assignment();
         * binaryInteger();
         * functionProgram();
         * functionInterface();
         * functionBlock();
         * //functionBlock2();
         * //functionBlock3();
         * //functionBlock4();
         * case_test();
         * tcunit();
         * type();
         * expressions();
         * //eventLog();
         */

        System.out.println("End");
    }

    private static void application() throws IOException {
        // System.out.println("application");

        // String pathAsString =
        // "grammar\\src\\test\\resources\\iec61131_structuredtext\\assignment.st";

        // String pathAsString =
        // "C:/Users/U5353/Documents/OpenPLC/OpenPLC_Editor_TestProject/generated_st_code.st";

        // String pathAsString =
        // "grammar\\src\\test\\resources\\iec61131_structuredtext\\self_contained_program.st";

        // String pathAsString =
        // "grammar\\src\\test\\resources\\iec61131_structuredtext\\small_program.st";

        // String pathAsString =
        // "grammar\\src\\test\\resources\\iec61131_structuredtext\\large_program.st";

        // String pathAsString =
        // "grammar\\src\\test\\resources\\iec61131_structuredtext\\large_program_2.st";

        //String pathAsString = "grammar\\src\\test\\resources\\iec61131_structuredtext\\large_program_3.st";

        String pathAsString = "grammar\\src\\test\\resources\\iec61131_structuredtext\\large_program_4.st";

        // String pathAsString =
        // "grammar\\src\\test\\resources\\iec61131_structuredtext\\configuration.st";

        // String pathAsString =
        // "grammar\\src\\test\\resources\\iec61131_structuredtext\\configuration_with_variables.st";

        // String pathAsString =
        // "grammar\\src\\test\\resources\\iec61131_structuredtext\\type_bool_struct.st";

        // String pathAsString =
        // "grammar\\src\\test\\resources\\iec61131_structuredtext\\function_block_simple.st";
        // String pathAsString =
        // "grammar\\src\\test\\resources\\iec61131_structuredtext\\function_block_inout_var.st";

        final CharStream charStream = CharStreams
                .fromFileName(pathAsString);

        final StructuredTextLexer lexer = new StructuredTextLexer(charStream);

        // create a buffer of tokens pulled from the lexer
        final CommonTokenStream tokens = new CommonTokenStream(lexer);

        final StructuredTextParser parser = new StructuredTextParser(tokens);

        // parse
        // Function_block_declarationContext root = parser.function_block_declaration();
        // Assignment_statementContext root = parser.assignment_statement();
        Compilation_unitContext root = parser.compilation_unit();

        // debugOutputAST(root);
        execute(root);

        System.out.println("");
    }

    private static void debugOutputAST(Compilation_unitContext root) {
        ASTListener listener = new ASTListener();

        // DefaultStructuredTextListener listener = new DefaultStructuredTextListener();

        // Create a generic parse tree walker that can trigger callbacks
        final ParseTreeWalker walker = new ParseTreeWalker();
        // Walk the tree created during the parse, trigger callbacks
        walker.walk(listener, root);
    }

    private static void execute(Compilation_unitContext root) {

        TypeScope globalTypeScope = new TypeScope();

        // BOOL
        DataType boolDataType = new BooleanDataType();
        boolDataType.setName("BOOL");
        globalTypeScope.addType(boolDataType.getName(), boolDataType);

        // UINT
        DataType uIntDataType = new UIntDataType();
        uIntDataType.setName("UINT");
        globalTypeScope.addType(uIntDataType.getName(), uIntDataType);

        // SR FlipFlop (reset precedence)
        DataType srDataType = new FunctionBlock();
        srDataType.setName("SR");
        globalTypeScope.addType(srDataType.getName(), srDataType);

        // SR FlipFlop (reset precedence)
        DataType rsDataType = new FunctionBlock();
        rsDataType.setName("RS");
        globalTypeScope.addType(rsDataType.getName(), rsDataType);

        // TON Timer to ON (TRUE) (delays it's output going to HIGH/ON/TRUE for a
        // configurable amount of time adter the input goes high)
        DataType tonDataType = new FunctionBlock();
        tonDataType.setName("TON");
        globalTypeScope.addType(tonDataType.getName(), tonDataType);

        // TIME
        DataType timeDataType = new TimeDataType();
        timeDataType.setName("TIME");
        globalTypeScope.addType(timeDataType.getName(), timeDataType);

        ModelCreatorASTListener listener = new ModelCreatorASTListener();
        listener.setGlobalTypeScope(globalTypeScope);

        // Create a generic parse tree walker that can trigger callbacks
        final ParseTreeWalker walker = new ParseTreeWalker();
        // Walk the tree created during the parse, trigger callbacks
        walker.walk(listener, root);

        outputItems(listener);

        executeParsedObjects(globalTypeScope, listener);
    }

    private static void outputItems(ModelCreatorASTListener listener) {

        System.out.println("\nTypes");
        System.out.println(listener.globalTypeScope);

        System.out.println("\nProgram");
        System.out.println(listener.program);

        System.out.println("\nConfiguration");
        System.out.println(listener.configuration);
    }

    private static void executeParsedObjects(TypeScope globalTypeScope, ModelCreatorASTListener listener) {

        //
        // Instantiation
        //

        TypeScope types = listener.globalTypeScope;

        // global_status : S_FA_STATUS;
        DataType sFaStatusDataType = types.get("S_FA_STATUS");
        VariableInstance globalStatusVariableInstance = new VariableInstance();
        globalStatusVariableInstance.setName("global_status");
        globalStatusVariableInstance.setDataType(sFaStatusDataType);

        // instantiate function blocks in S_FA_STATUS
        // set fields of S_FA_STATUS to default / initial values
        for (Map.Entry<String, Field> entry : sFaStatusDataType.getFields().entrySet()) {

            Field field = entry.getValue();
            DataType fieldDataType = field.getDataType();

            VariableInstance fieldVariableInstance = null;

            if (StringUtils.equalsIgnoreCase(fieldDataType.getName(), "SR")) {
                fieldVariableInstance = new SRVariableInstance();
            } else if (StringUtils.equalsIgnoreCase(fieldDataType.getName(), "RS")) {
                fieldVariableInstance = new RSVariableInstance();
            } else if (StringUtils.equalsIgnoreCase(fieldDataType.getName(), "TON")) {
                fieldVariableInstance = new TONVariableInstance();
            } else {
                fieldVariableInstance = new VariableInstance();
            }

            fieldVariableInstance.setName(field.getName());
            fieldVariableInstance.setDataType(field.getDataType());

            // initial / default value
            if (StringUtils.isNotBlank(field.getInitialValue())) {
                fieldVariableInstance.setValue(field.getInitialValue());
            } else {
                fieldVariableInstance.setValue((String) field.getDataType().getDefaultValue());
            }

            globalStatusVariableInstance.addElement(fieldVariableInstance);
        }

        // configuration
        ConfigurationInstance configurationInstance = new ConfigurationInstance();
        configurationInstance.addElement(globalStatusVariableInstance);

        // program
        Program program = listener.program;

        ProgramInstance programInstance = new ProgramInstance();
        programInstance.setName(program.getName());

        for (Variable entry : program.getVariables()) {

            if (entry.isExternal()) {

                boolean retain = false;
                boolean external = true;
                programInstance.addElement(configurationInstance.getElement(entry.getName()), retain, external);

            } else {

                if (entry.getDataType() instanceof FunctionBlock) {

                    VariableInstance variableInstance = instantiateFunctionBlock((FunctionBlock) entry.getDataType(),
                            globalTypeScope, programInstance);
                    variableInstance.setName(entry.getName());
                    programInstance.addElement(variableInstance);

                } else if (entry.getDataType() instanceof Struct) {

                    VariableInstance variableInstance = instantiateStruct((Struct) entry.getDataType(),
                            globalTypeScope, programInstance);
                    variableInstance.setName(entry.getName());
                    programInstance.addElement(variableInstance);

                } else {

                    VariableInstance variableInstance = new VariableInstance();
                    variableInstance.setName(entry.getName());
                    variableInstance.setDataType(entry.getDataType());

                    // initial / default value
                    if (StringUtils.isNotBlank(entry.getInitialValue())) {
                        variableInstance.setValue(entry.getInitialValue());
                    } else {
                        variableInstance.setValue((String) entry.getDataType().getDefaultValue());
                    }

                    programInstance.addElement(variableInstance);
                }
            }
        }

        // copy statements into the program
        programInstance.getStatements().addAll(listener.program.getStatements());

        //
        // GUI
        //

        JFrame mainJFrame = new JFrame();
        mainJFrame.setTitle("IEC 61131-3 Simulator HMI (Version: 0.0.0)");
        JPanel panel = new JPanel();

        // JButton mit Text "Drück mich" wird erstellt
        JButton buttonP2 = new JButton("P2");
        buttonP2.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                // user has requested the cylinder move to P2
                VariableInstance buttonP2 = programInstance.getElement("Zyl1_T_P2_HMI");
                buttonP2.setValue("true");
            }

        });

        panel.add(buttonP2);

        JButton buttonSensorP2 = new JButton("Sensor P2");
        buttonSensorP2.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                // pneumatic cylinder arrived at P2 and sensor P2 sends high signal
                VariableInstance senP2 = programInstance.getElement("SEN_P2_T_HMI");
                senP2.setValue("true");
            }

        });

        // JButton wird dem Panel hinzugefügt
        panel.add(buttonSensorP2);

        JLabel jLabel = new JLabel();
        jLabel.setText("Stoerung");

        panel.add(jLabel);

        mainJFrame.add(panel);

        // Fenstergröße wird so angepasst, dass
        // der Inhalt reinpasst
        // mainJFrame.pack();
        mainJFrame.setPreferredSize(new Dimension(600, 200));
        mainJFrame.pack();
        mainJFrame.setLocationRelativeTo(null);

        mainJFrame.setVisible(true);

        //
        // execution
        //

        for (int step = 0; step < 500; step++) {

            System.out.println("step " + step);

            // // DEBUG - prevent TON timeout
            // if (step == 3) {
            // System.out.println("TON turn off");
            // VariableInstance senP2 = programInstance.getElement("SEN_P2_T_HMI");
            // senP2.setValue("true");
            // }

            executeStatements(programInstance, null, null);

            // DEBUG output global status struct
            VariableInstance stoerung = globalStatusVariableInstance.getElement("Stoerung");
            System.out.println(stoerung.getName() + " " + stoerung.getValue());

            //
            // GUI (update the gui for example for errors)
            //

            VariableInstance diag = programInstance.getElement("Diag");
            VariableInstance diagStoerung = diag.getElement("Stoerung");
            jLabel.setText("Stoerung: " + diagStoerung.getValue());

            try {
                Thread.sleep(3000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        }
    }

    private static void executeStatements(VariableInstance variableInstance, VariableInstance parentVariableInstance,
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
                    System.out.println("Reference Semantics!");

                    VariableInstance source = parentVariableInstance.getElement(parameterAssignment.getValue());

                    variableInstance.setElement(parameterAssignment.getParameterName(), source);
                } else {
                    System.out.println("Copy Semantics!");
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

                boolean newValue = false;

                // implement reset dominance by putting the reset operation last
                if (s1) {
                    newValue = true;
                }
                if (r) {
                    newValue = false;
                }

                variableInstance.getElement("Q1").setValue(Boolean.toString(newValue));

            } else if (StringUtils.equalsIgnoreCase(dataTypeAsString, "RS")) {

                boolean s = Boolean.parseBoolean(variableInstance.getElement("S").getValue());
                boolean r1 = Boolean.parseBoolean(variableInstance.getElement("R1").getValue());

                boolean newValue = false;

                // implement set dominance by putting the set operation last
                if (r1) {
                    newValue = false;
                }
                if (s) {
                    newValue = true;
                }

                variableInstance.getElement("Q1").setValue(Boolean.toString(newValue));

            } else if (StringUtils.equalsIgnoreCase(dataTypeAsString, "TON")) {

                // System.out.println(variableInstance.getClass().getSimpleName());

                // TON0(IN := _TMP_AND15_OUT, PT := const_T_Laufzeit_P2);

                String inValue = variableInstance.getElement("IN").getValue();
                // System.out.println("IN: " + inValue);

                VariableInstance outputVariableInstance = variableInstance.getElement("Q");

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

                    // the TON internally changes state to true
                    currentValueVariableInstance.setValue("true");

                    TONVariableInstance tonVariableInstance = (TONVariableInstance) variableInstance;
                    tonVariableInstance.startTimer();
                }

                if (fallingEdgeOnInput) {

                    // System.out.println("Stopping TON timer!");

                    // the TON internally changes state to false
                    currentValueVariableInstance.setValue("false");

                    TONVariableInstance tonVariableInstance = (TONVariableInstance) variableInstance;
                    tonVariableInstance.stopTimer();
                }
            }
        }

        //
        // execute statements
        //

        for (Statement statement : variableInstance.getStatements()) {

            switch (statement.getStatementType()) {

                case ASSIGNMENT: {
                    AssignmentStatement assignmentStatement = (AssignmentStatement) statement;
                    String variableAsString = assignmentStatement.getVariable();

                    // find the variable that is assigned a new value
                    // System.out.println("" + variableSplit);
                    String[] variableSplit = variableAsString.split("\\.");
                    VariableInstance target = variableInstance;
                    for (int i = 0; i < variableSplit.length; i++) {
                        VariableInstance tempTarget = target.getElement(variableSplit[i]);

                        if (tempTarget == null) {
                            String msg = "Cannot find \"" + variableSplit[i] + "\" in \"" + target.getName() + "\"";
                            System.out.println(msg);
                            throw new RuntimeException(msg);
                        }

                        target = tempTarget;
                    }
                    // System.out.println(target);

                    if (target == null) {
                        throw new RuntimeException("");
                    }

                    // DEBUG
                    System.out.println(target.getName());
                    if (target.getName().equals("Stoerung")) {
                        System.out.println("test");
                    }

                    // find the value to assign
                    VariableInstance source = variableInstance;
                    for (Expression expression : assignmentStatement.getExpressionList()) {
                        source = evaluateExpression(source, expression);
                    }
                    // System.out.println(source);

                    // assign the value
                    target.setValue(source.getValue());
                }
                    break;

                case SUBPROGRAM_CONTROL: {
                    SubprogramControlStatement subprogramControlStatement = (SubprogramControlStatement) statement;

                    // find the function block
                    String subprogramNameAsString = subprogramControlStatement.getSubprogramName();
                    String[] subProgSplit = subprogramNameAsString.split("\\.");

                    VariableInstance functionBlock = variableInstance;
                    for (int i = 0; i < subProgSplit.length; i++) {
                        functionBlock = functionBlock.getElement(subProgSplit[i]);
                    }

                    // System.out.println(subprogramControlStatement);

                    executeStatements(functionBlock, variableInstance,
                            subprogramControlStatement.getParameterAssignments());
                }
                    break;

                default:
                    throw new RuntimeException(statement.getStatementType() + " Not implemented yet!");
            }

        }
    }

    private static void copySemantics(VariableInstance variableInstance, VariableInstance parentVariableInstance,
            ParameterAssignment parameterAssignment) {
        // System.out.println(parameterAssignment);

        VariableInstance target = variableInstance.getElement(parameterAssignment.getParameterName());
        // System.out.println(target);

        String pathToObject = parameterAssignment.getValue();
        String[] pathToObjectSplit = StringUtils.split(pathToObject, "\\.");

        VariableInstance source = parentVariableInstance;
        for (String path : pathToObjectSplit) {
            VariableInstance tempCurr = source.getElement(path);

            if (tempCurr == null) {
                System.out.println(tempCurr);
    
                throw new RuntimeException("Cannot find \"" + path + "\" in \""
                        + source.getName() + "\"");
            }

            source = tempCurr;
        }

        //VariableInstance source = curr.getElement();
        // System.out.println(source);

        if (source == null) {
            System.out.println(source);

            throw new RuntimeException("Cannot find \"" + parameterAssignment.getValue() + "\" in \""
                    + parentVariableInstance.getName() + "\"");
        }

        target.setValue(source.getValue());

        for (Map.Entry<String, VariableDescriptor> entry : source.getElements().entrySet()) {

            VariableDescriptor sourceDescriptor = entry.getValue();
            VariableInstance sourceElement = sourceDescriptor.variableInstance;
            // System.out.println(sourceElement);

            VariableInstance targetElement = target.getElement(sourceElement.getName());
            // System.out.println(targetElement);

            // copy the value
            targetElement.setValue(sourceElement.getValue());
        }
    }

    private static VariableInstance evaluateExpression(VariableInstance source, Expression expression) {

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

                // System.out.println(lhsVariableInstance.getValue());
                // System.out.println(rhsVariableInstance.getValue());

                boolean result = Boolean.parseBoolean(lhsVariableInstance.getValue())
                        & Boolean.parseBoolean(rhsVariableInstance.getValue());
                // System.out.println(result);

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

    private static VariableInstance instantiateStruct(Struct structToCreate, TypeScope globalTypeScope,
            ProgramInstance programInstance) {
        
        VariableInstance variableInstance = new VariableInstance();
        variableInstance.setDataType(structToCreate);

        // variableInstance.getStatements().addAll(functionBlock.getStatements());

        for (Map.Entry<String, Field> mapEntry : structToCreate.getFields().entrySet()) {

            Field entry = mapEntry.getValue();

            // if (entry.isExternal()) {
            //     throw new RuntimeException("Not implemented yet!");
            // }

            // if (entry.isInOut()) {
            //     System.out.println("isInOut");
            // }

            if (entry.getDataType() instanceof FunctionBlock) {

                VariableInstance fbVariableInstance = instantiateFunctionBlock((FunctionBlock) entry.getDataType(),
                        globalTypeScope, variableInstance);
                fbVariableInstance.setName(entry.getName());
                variableInstance.addElement(fbVariableInstance);

            } else {

                // if (entry.isInOut()) {

                // // Variable is an inout variable and as such has reference semantics.
                // // It is taken as a reference from the outer scope

                // VariableInstance inOutRef =
                // parentVariableInstance.getElement(entry.getName());
                // variableInstance.addElement(inOutRef);

                // } else {

                // normal variables (non-inout) are instantiated as local variables

                VariableInstance simpleVariableInstance = new VariableInstance();
                simpleVariableInstance.setName(entry.getName());
                simpleVariableInstance.setDataType(entry.getDataType());

                if (entry.getDataType() instanceof Struct) {

                    Struct struct = (Struct) entry.getDataType();
                    for (Map.Entry<String, Field> mapEntry2 : struct.getFields().entrySet()) {

                        Field field = mapEntry2.getValue();

                        VariableInstance structField = new VariableInstance();
                        structField.setName(field.getName());
                        structField.setDataType(field.getDataType());
                        if (StringUtils.isNotBlank(field.getInitialValue())) {
                            structField.setValue(field.getInitialValue());
                        }

                        simpleVariableInstance.addElement(structField);
                    }

                }

                // initial / default value
                if (StringUtils.isNotBlank(entry.getInitialValue())) {
                    simpleVariableInstance.setValue(entry.getInitialValue());
                } else {
                    simpleVariableInstance.setValue((String) entry.getDataType().getDefaultValue());
                }

                variableInstance.addElement(simpleVariableInstance);

            }
            // }
        }

        return variableInstance;
    }

    private static VariableInstance instantiateFunctionBlock(FunctionBlock functionBlock, TypeScope globalTypeScope,
            VariableInstance parentVariableInstance) {

        if (StringUtils.equalsIgnoreCase(functionBlock.getName(), "SR")) {

            VariableInstance variableInstance = new SRVariableInstance();
            variableInstance.setDataType(functionBlock);

            // the stored value from the last iteration
            VariableInstance currentValueVariableInstance = new VariableInstance();
            currentValueVariableInstance.setName("Q1");
            currentValueVariableInstance.setDataType(globalTypeScope.get("BOOL"));
            boolean retain = false;
            boolean external = false;
            variableInstance.addElement(currentValueVariableInstance, retain, external);

            // the value presented at the set input during the current iteration
            VariableInstance setValueVariableInstance = new VariableInstance();
            setValueVariableInstance.setName("S1");
            setValueVariableInstance.setDataType(globalTypeScope.get("BOOL"));
            variableInstance.addElement(setValueVariableInstance);

            // the value presented at the reset input during the current iteration
            VariableInstance resetValueVariableInstance = new VariableInstance();
            resetValueVariableInstance.setName("R");
            resetValueVariableInstance.setDataType(globalTypeScope.get("BOOL"));
            variableInstance.addElement(resetValueVariableInstance);

            return variableInstance;
        }

        if (StringUtils.equalsIgnoreCase(functionBlock.getName(), "RS")) {

            VariableInstance variableInstance = new RSVariableInstance();
            variableInstance.setDataType(functionBlock);

            // the stored value from the last iteration
            VariableInstance currentValueVariableInstance = new VariableInstance();
            currentValueVariableInstance.setName("Q1");
            currentValueVariableInstance.setDataType(globalTypeScope.get("BOOL"));
            boolean retain = false;
            boolean external = false;
            variableInstance.addElement(currentValueVariableInstance, retain, external);

            // the value presented at the set input during the current iteration
            VariableInstance setValueVariableInstance = new VariableInstance();
            setValueVariableInstance.setName("S");
            setValueVariableInstance.setDataType(globalTypeScope.get("BOOL"));
            variableInstance.addElement(setValueVariableInstance);

            // the value presented at the reset input during the current iteration
            VariableInstance resetValueVariableInstance = new VariableInstance();
            resetValueVariableInstance.setName("R1");
            resetValueVariableInstance.setDataType(globalTypeScope.get("BOOL"));
            variableInstance.addElement(resetValueVariableInstance);

            return variableInstance;
        }

        if (StringUtils.equalsIgnoreCase(functionBlock.getName(), "TON")) {

            VariableInstance variableInstance = new TONVariableInstance();
            variableInstance.setDataType(functionBlock);

            // the stored value from the last iteration
            VariableInstance currentValueVariableInstance = new VariableInstance();
            currentValueVariableInstance.setName("currentValue");
            currentValueVariableInstance.setDataType(globalTypeScope.get("BOOL"));
            boolean retain = false;
            boolean external = false;
            variableInstance.addElement(currentValueVariableInstance, retain, external);

            // the value presented at the input during the current iteration
            VariableInstance inputValueVariableInstance = new VariableInstance();
            inputValueVariableInstance.setName("IN");
            inputValueVariableInstance.setDataType(globalTypeScope.get("BOOL"));
            variableInstance.addElement(inputValueVariableInstance);

            // the output
            VariableInstance outputValueVariableInstance = new VariableInstance();
            outputValueVariableInstance.setName("Q");
            outputValueVariableInstance.setDataType(globalTypeScope.get("BOOL"));
            variableInstance.addElement(outputValueVariableInstance);

            // the threshold time
            VariableInstance thresholdTimeValueVariableInstance = new VariableInstance();
            thresholdTimeValueVariableInstance.setName("PT");
            thresholdTimeValueVariableInstance.setDataType(globalTypeScope.get("TIME"));
            variableInstance.addElement(thresholdTimeValueVariableInstance);

            return variableInstance;
        }

        VariableInstance variableInstance = new VariableInstance();
        variableInstance.setDataType(functionBlock);

        variableInstance.getStatements().addAll(functionBlock.getStatements());

        for (Variable entry : functionBlock.getVariables()) {

            if (entry.isExternal()) {
                throw new RuntimeException("Not implemented yet!");
            }

            if (entry.isInOut()) {
                System.out.println("isInOut");
            }

            if (entry.getDataType() instanceof FunctionBlock) {

                VariableInstance fbVariableInstance = instantiateFunctionBlock((FunctionBlock) entry.getDataType(),
                        globalTypeScope, variableInstance);
                fbVariableInstance.setName(entry.getName());
                variableInstance.addElement(fbVariableInstance);

            } else {

                // if (entry.isInOut()) {

                // // Variable is an inout variable and as such has reference semantics.
                // // It is taken as a reference from the outer scope

                // VariableInstance inOutRef =
                // parentVariableInstance.getElement(entry.getName());
                // variableInstance.addElement(inOutRef);

                // } else {

                // normal variables (non-inout) are instantiated as local variables

                VariableInstance simpleVariableInstance = new VariableInstance();
                simpleVariableInstance.setName(entry.getName());
                simpleVariableInstance.setDataType(entry.getDataType());

                if (entry.getDataType() instanceof Struct) {

                    Struct struct = (Struct) entry.getDataType();
                    for (Map.Entry<String, Field> mapEntry : struct.getFields().entrySet()) {

                        Field field = mapEntry.getValue();

                        VariableInstance structField = new VariableInstance();
                        structField.setName(field.getName());
                        structField.setDataType(field.getDataType());
                        if (StringUtils.isNotBlank(field.getInitialValue())) {
                            structField.setValue(field.getInitialValue());
                        }

                        simpleVariableInstance.addElement(structField);
                    }

                }

                // initial / default value
                if (StringUtils.isNotBlank(entry.getInitialValue())) {
                    simpleVariableInstance.setValue(entry.getInitialValue());
                } else {
                    simpleVariableInstance.setValue((String) entry.getDataType().getDefaultValue());
                }

                variableInstance.addElement(simpleVariableInstance);

            }
            // }
        }

        return variableInstance;
    }

    private static void assignment() throws IOException {
        System.out.println("assignment");

        final CharStream charStream = CharStreams
                .fromFileName("src/test/resources/iec61131_structuredtext/assignment.st");

        final StructuredTextLexer lexer = new StructuredTextLexer(charStream);

        // create a buffer of tokens pulled from the lexer
        final CommonTokenStream tokens = new CommonTokenStream(lexer);

        final StructuredTextParser parser = new StructuredTextParser(tokens);

        // parse
        // Function_block_declarationContext root = parser.function_block_declaration();
        Assignment_statementContext root = parser.assignment_statement();

        ASTListener listener = new ASTListener();
        // DefaultStructuredTextListener listener = new DefaultStructuredTextListener();

        // // Create a generic parse tree walker that can trigger callbacks
        final ParseTreeWalker walker = new ParseTreeWalker();
        // Walk the tree created during the parse, trigger callbacks
        walker.walk(listener, root);

        // // dump output
        // Node rootNode = listener.getRootNode();
        // rootNode.print(0);

        System.out.println();
    }

    private static void binaryInteger() throws IOException {
        System.out.println("binaryInteger");

        final CharStream charStream = CharStreams
                .fromFileName("src/test/resources/iec61131_structuredtext/binary_integer.st");

        final StructuredTextLexer lexer = new StructuredTextLexer(charStream);

        // create a buffer of tokens pulled from the lexer
        final CommonTokenStream tokens = new CommonTokenStream(lexer);

        final StructuredTextParser parser = new StructuredTextParser(tokens);

        // parse
        // Function_block_declarationContext root = parser.function_block_declaration();
        Assignment_statementContext root = parser.assignment_statement();

        ASTListener listener = new ASTListener();
        // DefaultStructuredTextListener listener = new DefaultStructuredTextListener();

        // // Create a generic parse tree walker that can trigger callbacks
        final ParseTreeWalker walker = new ParseTreeWalker();
        // Walk the tree created during the parse, trigger callbacks
        walker.walk(listener, root);

        // // dump output
        // Node rootNode = listener.getRootNode();
        // rootNode.print(0);

        System.out.println();
    }

    private static void eventLog() throws IOException {

        System.out.println("EventLog");

        final CharStream charStream = CharStreams
                .fromFileName("src/test/resources/iec61131_structuredtext/event_log.st");

        final StructuredTextLexer lexer = new StructuredTextLexer(charStream);

        // create a buffer of tokens pulled from the lexer
        final CommonTokenStream tokens = new CommonTokenStream(lexer);

        final StructuredTextParser parser = new StructuredTextParser(tokens);

        // parse
        // Function_block_declarationContext root = parser.function_block_declaration();
        Compilation_unitContext root = parser.compilation_unit();

        debugOutputAST(root);

        // // dump output
        // Node rootNode = listener.getRootNode();
        // rootNode.print(0);

        System.out.println();
    }

    private static void type() throws IOException {

        System.out.println("Test Type");

        final CharStream charStream = CharStreams
                // .fromFileName("src/test/resources/iec61131_structuredtext/function_selection.st");
                // .fromFileName("src/test/resources/iec61131_structuredtext/program.st");
                // .fromFileName("src/test/resources/iec61131_structuredtext/interface.st");
                // .fromFileName("src/test/resources/iec61131_structuredtext/function_block.st");
                .fromFileName("src/test/resources/iec61131_structuredtext/type.st");

        final StructuredTextLexer lexer = new StructuredTextLexer(charStream);

        // create a buffer of tokens pulled from the lexer
        final CommonTokenStream tokens = new CommonTokenStream(lexer);

        final StructuredTextParser parser = new StructuredTextParser(tokens);

        // parse a type declaration
        Data_type_declarationContext root = parser.data_type_declaration();

        ASTListener listener = new ASTListener();
        // DefaultStructuredTextListener listener = new DefaultStructuredTextListener();

        // // Create a generic parse tree walker that can trigger callbacks
        final ParseTreeWalker walker = new ParseTreeWalker();
        // Walk the tree created during the parse, trigger callbacks
        walker.walk(listener, root);

        // // dump output
        // Node rootNode = listener.getRootNode();
        // rootNode.print(0);

        System.out.println();
    }

    private static void case_test() throws IOException {

        System.out.println("case_test");

        final CharStream charStream = CharStreams
                .fromFileName("src/test/resources/iec61131_structuredtext/case.st");

        final StructuredTextLexer lexer = new StructuredTextLexer(charStream);

        // create a buffer of tokens pulled from the lexer
        final CommonTokenStream tokens = new CommonTokenStream(lexer);

        final StructuredTextParser parser = new StructuredTextParser(tokens);

        // parse a case declaration
        Case_statementContext root = parser.case_statement();

        ASTListener listener = new ASTListener();

        // // Create a generic parse tree walker that can trigger callbacks
        final ParseTreeWalker walker = new ParseTreeWalker();
        // Walk the tree created during the parse, trigger callbacks
        walker.walk(listener, root);

        // // dump output
        // Node rootNode = listener.getRootNode();
        // rootNode.print(0);

        System.out.println();
    }

    private static void functionProgram() throws IOException {

        System.out.println("Test Program");

        final CharStream charStream = CharStreams
                // .fromFileName("src/test/resources/iec61131_structuredtext/function_selection.st");
                .fromFileName("src/test/resources/iec61131_structuredtext/program.st");
        // .fromFileName("src/test/resources/iec61131_structuredtext/interface.st");
        // .fromFileName("src/test/resources/iec61131_structuredtext/function_block.st");

        final StructuredTextLexer lexer = new StructuredTextLexer(charStream);

        // create a buffer of tokens pulled from the lexer
        final CommonTokenStream tokens = new CommonTokenStream(lexer);

        final StructuredTextParser parser = new StructuredTextParser(tokens);

        // parse a program
        final Program_declarationContext root = parser.program_declaration();

        // parse an interface
        // final Interface_declarationContext root = parser.interface_declaration();

        // function block
        // Function_block_declarationContext root = parser.function_block_declaration();

        // // Create a generic parse tree walker that can trigger callbacks
        final ParseTreeWalker walker = new ParseTreeWalker();
        // Walk the tree created during the parse, trigger callbacks
        walker.walk(new DefaultStructuredTextListener(), root);

        // // dump output
        // Node rootNode = listener.getRootNode();
        // rootNode.print(0);

        System.out.println();
    }

    private static void functionInterface() throws IOException {

        System.out.println("Test Interface");

        final CharStream charStream = CharStreams
                // .fromFileName("src/test/resources/iec61131_structuredtext/function_selection.st");
                // .fromFileName("src/test/resources/iec61131_structuredtext/program.st");
                .fromFileName("src/test/resources/iec61131_structuredtext/interface.st");
        // .fromFileName("src/test/resources/iec61131_structuredtext/function_block.st");

        final StructuredTextLexer lexer = new StructuredTextLexer(charStream);

        // create a buffer of tokens pulled from the lexer
        final CommonTokenStream tokens = new CommonTokenStream(lexer);

        final StructuredTextParser parser = new StructuredTextParser(tokens);

        // parse a program
        // final Program_delcarationContext root = parser.program_delcaration();

        // parse an interface
        final Interface_declarationContext root = parser.interface_declaration();

        // function block
        // Function_block_declarationContext root = parser.function_block_declaration();

        // // Create a generic parse tree walker that can trigger callbacks
        final ParseTreeWalker walker = new ParseTreeWalker();
        // Walk the tree created during the parse, trigger callbacks
        walker.walk(new DefaultStructuredTextListener(), root);

        // // dump output
        // Node rootNode = listener.getRootNode();
        // rootNode.print(0);

        System.out.println();
    }

    private static void functionBlock() throws IOException {

        System.out.println("functionBlock()");

        final CharStream charStream = CharStreams
                .fromFileName("src/test/resources/iec61131_structuredtext/function_block.st");

        final StructuredTextLexer lexer = new StructuredTextLexer(charStream);

        // create a buffer of tokens pulled from the lexer
        final CommonTokenStream tokens = new CommonTokenStream(lexer);

        final StructuredTextParser parser = new StructuredTextParser(tokens);

        // parse a program
        // final Program_delcarationContext root = parser.program_delcaration();

        // parse an interface
        // final Interface_declarationContext root = parser.interface_declaration();

        // function block
        Function_block_declarationContext root = parser.function_block_declaration();

        // DefaultStructuredTextListener listener = new DefaultStructuredTextListener();
        ASTListener listener = new ASTListener();

        // // Create a generic parse tree walker that can trigger callbacks
        final ParseTreeWalker walker = new ParseTreeWalker();
        // Walk the tree created during the parse, trigger callbacks
        walker.walk(listener, root);

        // // dump output
        // Node rootNode = listener.getRootNode();
        // rootNode.print(0);

        System.out.println();
    }

    private static void functionBlock2() throws IOException {

        System.out.println("functionBlock2()");

        final CharStream charStream = CharStreams
                .fromFileName("src/test/resources/iec61131_structuredtext/function_block_2.st");

        final StructuredTextLexer lexer = new StructuredTextLexer(charStream);

        // create a buffer of tokens pulled from the lexer
        final CommonTokenStream tokens = new CommonTokenStream(lexer);

        final StructuredTextParser parser = new StructuredTextParser(tokens);

        // parse a program
        // final Program_delcarationContext root = parser.program_delcaration();

        // parse an interface
        // final Interface_declarationContext root = parser.interface_declaration();

        // function block
        Function_block_declarationContext root = parser.function_block_declaration();

        // DefaultStructuredTextListener listener = new DefaultStructuredTextListener();
        ASTListener listener = new ASTListener();

        // // Create a generic parse tree walker that can trigger callbacks
        final ParseTreeWalker walker = new ParseTreeWalker();
        // Walk the tree created during the parse, trigger callbacks
        walker.walk(listener, root);

        // // dump output
        // Node rootNode = listener.getRootNode();
        // rootNode.print(0);

        System.out.println();
    }

    private static void functionBlock3() throws IOException {

        System.out.println("functionBlock3()");

        final CharStream charStream = CharStreams
                .fromFileName("src/test/resources/iec61131_structuredtext/function_block_3.st");

        final StructuredTextLexer lexer = new StructuredTextLexer(charStream);

        // create a buffer of tokens pulled from the lexer
        final CommonTokenStream tokens = new CommonTokenStream(lexer);

        final StructuredTextParser parser = new StructuredTextParser(tokens);

        // parse a program
        // final Program_delcarationContext root = parser.program_delcaration();

        // parse an interface
        // final Interface_declarationContext root = parser.interface_declaration();

        // function block
        Function_block_declarationContext root = parser.function_block_declaration();

        // DefaultStructuredTextListener listener = new DefaultStructuredTextListener();
        ASTListener listener = new ASTListener();

        // // Create a generic parse tree walker that can trigger callbacks
        final ParseTreeWalker walker = new ParseTreeWalker();
        // Walk the tree created during the parse, trigger callbacks
        walker.walk(listener, root);

        // // dump output
        // Node rootNode = listener.getRootNode();
        // rootNode.print(0);

        System.out.println();
    }

    private static void functionBlock4() throws IOException {

        System.out.println("functionBlock4()");

        final CharStream charStream = CharStreams
                .fromFileName("src/test/resources/iec61131_structuredtext/function_block_4.st");

        final StructuredTextLexer lexer = new StructuredTextLexer(charStream);

        // create a buffer of tokens pulled from the lexer
        final CommonTokenStream tokens = new CommonTokenStream(lexer);

        final StructuredTextParser parser = new StructuredTextParser(tokens);

        // parse a program
        // final Program_delcarationContext root = parser.program_delcaration();

        // parse an interface
        // final Interface_declarationContext root = parser.interface_declaration();

        // function block
        Function_block_declarationContext root = parser.function_block_declaration();

        // DefaultStructuredTextListener listener = new DefaultStructuredTextListener();
        ASTListener listener = new ASTListener();

        // // Create a generic parse tree walker that can trigger callbacks
        final ParseTreeWalker walker = new ParseTreeWalker();
        // Walk the tree created during the parse, trigger callbacks
        walker.walk(listener, root);

        // // dump output
        // Node rootNode = listener.getRootNode();
        // rootNode.print(0);

        System.out.println();
    }

    private static void tcunit() throws IOException {

        System.out.println("tcunit()");

        final CharStream charStream = CharStreams
                // .fromFileName("src/test/resources/iec61131_structuredtext/function_selection.st");
                // .fromFileName("src/test/resources/iec61131_structuredtext/program.st");
                // .fromFileName("src/test/resources/iec61131_structuredtext/interface.st");
                .fromFileName(
                        "src/test/resources/iec61131_structuredtext/tcunit/FB_AdjustAssertFailureMessageToMax253CharLength.TcPOU");

        final StructuredTextLexer lexer = new StructuredTextLexer(charStream);

        // create a buffer of tokens pulled from the lexer
        final CommonTokenStream tokens = new CommonTokenStream(lexer);

        final StructuredTextParser parser = new StructuredTextParser(tokens);

        // parse a program
        // final Program_delcarationContext root = parser.program_delcaration();

        // parse an interface
        // final Interface_declarationContext root = parser.interface_declaration();

        // function block
        Function_block_declarationContext root = parser.function_block_declaration();

        // DefaultStructuredTextListener listener = new DefaultStructuredTextListener();
        ASTListener listener = new ASTListener();

        // // Create a generic parse tree walker that can trigger callbacks
        final ParseTreeWalker walker = new ParseTreeWalker();
        // Walk the tree created during the parse, trigger callbacks
        walker.walk(listener, root);

        // // dump output
        // Node rootNode = listener.getRootNode();
        // rootNode.print(0);

        System.out.println();
    }

    private static void expressions() throws IOException {

        System.out.println("expressions()");

        final CharStream charStream = CharStreams
                // .fromFileName("src/test/resources/iec61131_structuredtext/function_selection.st");
                // .fromFileName("src/test/resources/iec61131_structuredtext/program.st");
                // .fromFileName("src/test/resources/iec61131_structuredtext/interface.st");
                .fromFileName("src/test/resources/iec61131_structuredtext/expressions.st");

        final StructuredTextLexer lexer = new StructuredTextLexer(charStream);

        // create a buffer of tokens pulled from the lexer
        final CommonTokenStream tokens = new CommonTokenStream(lexer);

        final StructuredTextParser parser = new StructuredTextParser(tokens);

        // parse a program
        Program_declarationContext root = parser.program_declaration();

        // DefaultStructuredTextListener listener = new DefaultStructuredTextListener();
        ASTListener listener = new ASTListener();

        // Create a generic parse tree walker that can trigger callbacks
        final ParseTreeWalker walker = new ParseTreeWalker();
        // Walk the tree created during the parse, trigger callbacks
        walker.walk(listener, root);

        // // dump output
        // Node rootNode = listener.getRootNode();
        // rootNode.print(0);

        System.out.println();
    }

}
