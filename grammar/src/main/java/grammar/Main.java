package grammar;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.tree.ParseTreeWalker;
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
import model.BooleanDataType;
import model.DataType;
import model.Field;
import model.FunctionBlock;
import model.Program;
import model.Step;
import model.Struct;
import model.TimeDataType;
import model.TypeScope;
import model.UIntDataType;
import model.Variable;
import virtual_devices.Cylinder;
import virtual_devices.CylinderCallback;

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

        // String pathAsString =
        // "grammar\\src\\test\\resources\\iec61131_structuredtext\\large_program_3.st";

        // String pathAsString =
        // "grammar\\src\\test\\resources\\iec61131_structuredtext\\large_program_4.st";

        // String pathAsString =
        // "grammar\\src\\test\\resources\\iec61131_structuredtext\\large_program_5.st";

        // String pathAsString =
        // "grammar\\src\\test\\resources\\iec61131_structuredtext\\function_call.st";

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

        // String pathAsString =
        // "grammar\\src\\test\\resources\\iec61131_structuredtext\\traffic_light.st";

        // String pathAsString =
        // "grammar\\src\\test\\resources\\iec61131_structuredtext\\function.st";

        String pathAsString = "grammar\\src\\test\\resources\\iec61131_structuredtext\\maybe.st";

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

        // configuration
        ConfigurationInstance configurationInstance = new ConfigurationInstance();
        execute(root, configurationInstance);

        System.out.println("");
    }

    private static void debugOutputAST(Compilation_unitContext root, ConfigurationInstance configurationInstance) {
        ASTListener listener = new ASTListener();

        // DefaultStructuredTextListener listener = new DefaultStructuredTextListener();

        // Create a generic parse tree walker that can trigger callbacks
        final ParseTreeWalker walker = new ParseTreeWalker();
        // Walk the tree created during the parse, trigger callbacks
        walker.walk(listener, root);
    }

    private static void execute(Compilation_unitContext root, ConfigurationInstance configurationInstance) {

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

        listener.globalTypeScope.initialize();

        outputItems(listener);

        executeParsedObjects(globalTypeScope, configurationInstance, listener);
    }

    private static void outputItems(ModelCreatorASTListener listener) {

        int indent = 0;

        System.out.println("\nTypes\n");
        System.out.println(listener.globalTypeScope.toString(indent + 1));

        System.out.println("\nProgram\n");
        System.out.println(listener.program.toString(indent + 1));

        System.out.println("\nConfiguration\n");
        System.out.println(listener.configuration.toString(indent + 1));
    }

    private static void executeParsedObjects(TypeScope globalTypeScope, ConfigurationInstance configurationInstance,
            ModelCreatorASTListener listener) {

        //
        // Instantiation
        //

        TypeScope types = listener.globalTypeScope;

        // global_status : S_FA_STATUS;
        DataType sFaStatusDataType = types.get("S_FA_STATUS");
        if (sFaStatusDataType != null) {

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

            configurationInstance.addElement(globalStatusVariableInstance);
        }

        // program
        Program program = listener.program;

        ProgramInstance programInstance = new ProgramInstance();
        programInstance.setName(program.getName());

        // instantiate the variables in the program
        for (Variable entry : program.getVariables()) {

            if (entry.isExternal()) {

                // take external variables from the configuration

                boolean retain = false;
                boolean external = true;

                String elementName = entry.getName();
                VariableInstance elementVariableInstance = configurationInstance.getElement(elementName);
                programInstance.addElement(elementVariableInstance, retain, external);

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
        // virtual hardware
        //

        CylinderCallback cylinderCallback = new CylinderCallback() {

            @Override
            public void position1Exited() {

                System.out.println("position 1 Exited()");

                VariableInstance senP1 = programInstance.getElement("SEN_P1_T_HMI");
                senP1.setValue("false");
            }

            @Override
            public void position1Reached() {
                System.out.println("position 1 Reached()");

                VariableInstance senP1 = programInstance.getElement("SEN_P1_T_HMI");
                senP1.setValue("true");

                // // turn off the button
                // VariableInstance hmi = programInstance.getElement("Zyl1_T_P1_HMI");
                // hmi.setValue("false");
            }

            @Override
            public void position2Exited() {
                System.out.println("position 2 Exited()");
                VariableInstance senP2 = programInstance.getElement("SEN_P2_T_HMI");
                senP2.setValue("false");
            }

            @Override
            public void position2Reached() {
                System.out.println("position 2 Reached()");

                VariableInstance senP2 = programInstance.getElement("SEN_P2_T_HMI");
                senP2.setValue("true");

                // // turn off the button
                // VariableInstance hmi = programInstance.getElement("Zyl1_T_P2_HMI");
                // hmi.setValue("false");
            }

        };

        Cylinder cylinder = new Cylinder();
        cylinder.setCylinderCallback(cylinderCallback);

        // here you can simulate the cylinder never reaching some position
        //
        // cylinder.setHasErrorNeverReachesPosition1(true);
        // cylinder.setHasErrorNeverReachesPosition2(true);

        //
        // GUI
        //

        //
        // in HasErrorNeverReachesPosition1/2 state the cylinder will only move to
        // the center location and remain located there, it will not reach the other
        // position and it will not trigger the lightbar sensor! This will case the
        // runtime duration check to trigger and cause a system error. The system
        // error is persistent/retained and only goes away when error is acknowledged
        // (Quittiert)
        //
        // If you remove the HasErrorNeverReachesPosition1/2 state, the cylinder will
        // restart movement and eventually arrive at the other location and trigger
        // the light bar. Potential existing errors remain since their are
        // persistent/retained.
        // Errors only go away when error is acknowledged (Quittiert)
        //

        JPanel panel = new JPanel();

        JButton automatikBetriebJButton = new JButton(
                "Automatikbetrieb (" + programInstance.getElement("FA_BART_Auto_S_HMI").getValue() + ")");
        automatikBetriebJButton.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {

                // toggle automatic operation
                VariableInstance variableInstance = programInstance.getElement("FA_BART_Auto_S_HMI");
                boolean valueAsBoolean = StringUtils.equalsIgnoreCase(variableInstance.getValue(), "true");
                if (valueAsBoolean) {
                    variableInstance.setValue("false");
                } else {
                    variableInstance.setValue("true");
                }

                automatikBetriebJButton.setText(
                        "Automatikbetrieb (" + programInstance.getElement("FA_BART_Auto_S_HMI").getValue() + ")");

                // toggle manual operation off
                variableInstance = programInstance.getElement("FA_BART_Hand_T_HMI");
                valueAsBoolean = StringUtils.equalsIgnoreCase(variableInstance.getValue(), "true");
                if (valueAsBoolean) {
                    variableInstance.setValue("false");
                } else {
                    variableInstance.setValue("true");
                }
            }

        });
        panel.add(automatikBetriebJButton);

        JButton notHaltJButton = new JButton("NotHalt");
        notHaltJButton.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {

                VariableInstance status = programInstance.getElement("global_status");
                VariableInstance emergencyStop = status.getElement("NotHalt");
                emergencyStop.setValue("true");
            }

        });
        panel.add(notHaltJButton);

        // stoerung quittieren
        JButton resetErrorStateJButton = new JButton(
                "Quittieren (" + programInstance.getElement("FA_Stoer_Quit_T_HMI").getValue() + ")");
        resetErrorStateJButton.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {

                VariableInstance stoerQuitTasterHMI = programInstance.getElement("FA_Stoer_Quit_T_HMI");

                boolean valueAsBoolean = StringUtils.equalsIgnoreCase(stoerQuitTasterHMI.getValue(), "true");
                if (valueAsBoolean) {
                    stoerQuitTasterHMI.setValue("false");
                } else {
                    stoerQuitTasterHMI.setValue("true");
                }

                resetErrorStateJButton
                        .setText("Quittieren (" + programInstance.getElement("FA_Stoer_Quit_T_HMI").getValue() + ")");
            }

        });
        panel.add(resetErrorStateJButton);

        cylinder.setHasErrorNeverReachesPosition1(false);
        cylinder.setHasErrorNeverReachesPosition2(false);

        JButton toggleHasErrorNeverReachesPosition1JButton = new JButton(
                "ERR P1 (" + cylinder.isHasErrorNeverReachesPosition1() + ")");
        toggleHasErrorNeverReachesPosition1JButton.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {

                if (cylinder.isHasErrorNeverReachesPosition1()) {
                    cylinder.setHasErrorNeverReachesPosition1(false);

                } else {
                    cylinder.setHasErrorNeverReachesPosition1(true);
                }

                boolean newValue = cylinder.isHasErrorNeverReachesPosition1();
                toggleHasErrorNeverReachesPosition1JButton.setText("ERR P1 (" + newValue + ")");
            }

        });
        panel.add(toggleHasErrorNeverReachesPosition1JButton);

        JButton toggleHasErrorNeverReachesPosition2JButton = new JButton(
                "ERR P2 (" + cylinder.isHasErrorNeverReachesPosition2() + ")");
        toggleHasErrorNeverReachesPosition2JButton.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {

                if (cylinder.isHasErrorNeverReachesPosition2()) {
                    cylinder.setHasErrorNeverReachesPosition2(false);

                } else {
                    cylinder.setHasErrorNeverReachesPosition2(true);
                }

                boolean newValue = cylinder.isHasErrorNeverReachesPosition2();
                toggleHasErrorNeverReachesPosition2JButton.setText("ERR P2 (" + newValue + ")");
            }

        });
        panel.add(toggleHasErrorNeverReachesPosition2JButton);

        // Button to make the cylinder travel towards P1
        JButton buttonP1 = new JButton("P1");
        buttonP1.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {

                VariableInstance tasterPosition2 = programInstance.getElement("Zyl1_T_P2_HMI");
                tasterPosition2.setValue("false");

                // user has requested the cylinder move to P1
                VariableInstance tasterPosition1 = programInstance.getElement("Zyl1_T_P1_HMI");
                tasterPosition1.setValue("true");

                // cylinder.moveToPosition1();
            }

        });
        panel.add(buttonP1);

        JButton buttonSensorP1 = new JButton(
                "Sensor P1 (" + programInstance.getElement("SEN_P1_T_HMI").getValue() + ")");
        buttonSensorP1.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                // // pneumatic cylinder arrived at P1 and sensor P1 sends high signal
                // VariableInstance senP1 = programInstance.getElement("SEN_P1_T_HMI");
                // senP1.setValue("true");

                VariableInstance senP1 = programInstance.getElement("SEN_P1_T_HMI");

                boolean valueAsBoolean = StringUtils.equalsIgnoreCase(senP1.getValue(), "true");
                if (valueAsBoolean) {
                    senP1.setValue("false");
                } else {
                    senP1.setValue("true");
                }

                buttonSensorP1.setText("Sensor P1 (" + programInstance.getElement("SEN_P1_T_HMI").getValue() + ")");
            }

        });
        panel.add(buttonSensorP1);

        // Button to make the zylinder travel towards P2
        JButton buttonP2 = new JButton("P2");
        buttonP2.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {

                VariableInstance tasterPosition1 = programInstance.getElement("Zyl1_T_P1_HMI");
                tasterPosition1.setValue("false");

                // user has requested the cylinder move to P2
                VariableInstance tasterPosition2 = programInstance.getElement("Zyl1_T_P2_HMI");
                tasterPosition2.setValue("true");

                // cylinder.moveToPosition2();
            }

        });
        panel.add(buttonP2);

        JButton buttonSensorP2 = new JButton(
                "Sensor P2 (" + programInstance.getElement("SEN_P2_T_HMI").getValue() + ")");
        buttonSensorP2.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                // // pneumatic cylinder arrived at P2 and sensor P2 sends high signal
                // VariableInstance senP2 = programInstance.getElement("SEN_P2_T_HMI");
                // senP2.setValue("true");

                // buttonSensorP2.setText("Sensor P2 (" +
                // programInstance.getElement("SEN_P2_T_HMI").getValue() + ")");

                VariableInstance senP2 = programInstance.getElement("SEN_P2_T_HMI");

                boolean valueAsBoolean = StringUtils.equalsIgnoreCase(senP2.getValue(), "true");
                if (valueAsBoolean) {
                    senP2.setValue("false");
                } else {
                    senP2.setValue("true");
                }

                buttonSensorP2.setText("Sensor P2 (" + programInstance.getElement("SEN_P2_T_HMI").getValue() + ")");
            }

        });
        panel.add(buttonSensorP2);

        JLabel jLabel = new JLabel();
        jLabel.setText("Stoerung");

        panel.add(jLabel);

        JFrame mainJFrame = new JFrame();
        mainJFrame.setTitle("IEC 61131-3 Simulator HMI (Version: 0.0.0)");
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

        for (int executionIteration = 0; executionIteration < 500; executionIteration++) {

            System.out.println("");
            // System.out.println("executionIteration " + executionIteration);

            // // DEBUG
            // if (executionIteration >= 1) {
            // VariableInstance senP2 = programInstance.getElement("FA_BART_Hand_T_HMI");
            // if (senP2 != null) {
            // senP2.setValue("FaLsE_" + executionIteration);
            // }
            // }

            // // DEBUG - prevent TON timeout
            // if (executionIteration == 3) {
            // System.out.println("TON turn off");
            // VariableInstance senP2 = programInstance.getElement("SEN_P2_T_HMI");
            // senP2.setValue("true");
            // }

            programInstance.executeStatements(globalTypeScope, programInstance, null, null);

            // // DEBUG output all variable values
            // Collection<VariableDescriptor> variables =
            // programInstance.getElements().values();
            // for (VariableDescriptor variableDescriptor : variables) {
            // System.out.println(variableDescriptor.toString(0));
            // }

            // DEBUG
            VariableInstance variableInstance = programInstance.getElement("Zyl1_AUTO_P2_HMI");
            System.out.println(variableInstance.getName() + " " + variableInstance.getValue());

            // DEBUG output global status struct
            VariableInstance globalStatusVariableInstance = configurationInstance.getElement("global_status");
            //VariableInstance stoerung = globalStatusVariableInstance.getElement("Stoerung");
            //System.out.println(stoerung.getName() + " " + stoerung.getValue());
            VariableInstance bArtAuto = globalStatusVariableInstance.getElement("BART_Auto");
            System.out.println(bArtAuto.getName() + " " + bArtAuto.getValue());

            VariableInstance akt52MV0 = programInstance.getElement("AKT_5_2_MV0");
            //System.out.println(akt52MV0.toString(0));
            VariableInstance autoP2 = akt52MV0.getElement("Auto_P2");
            System.out.println(autoP2.getName() + " " + autoP2.getValue());

            VariableInstance s_MV_P2 = akt52MV0.getElement("s_MV_P2");
            System.out.println(s_MV_P2.getName() + " " + s_MV_P2.getValue());

            //
            // GUI (update the gui for example for errors)
            //

            // update the stoerung value in the GUI
            VariableInstance diag = programInstance.getElement("Diag");
            if (diag != null) {
                VariableInstance diagStoerung = diag.getElement("Stoerung");
                jLabel.setText("Stoerung: " + diagStoerung.getValue());
            }

            VariableInstance position1Requested = programInstance.getElement("s_MV_P1_FF");
            // System.out.println(position1Requested);
            if (null != position1Requested) {
                if (StringUtils.equalsIgnoreCase(position1Requested.getValue(), "true")) {
                    cylinder.moveToPosition1();
                }
            }

            VariableInstance position2Requested = programInstance.getElement("s_MV_P2_FF");
            // System.out.println(position2Requested);
            if (null != position2Requested) {
                if (StringUtils.equalsIgnoreCase(position2Requested.getValue(), "true")) {
                    cylinder.moveToPosition2();
                }
            }

            VariableInstance goToPosition2 = programInstance.getElement("s_MV_P2");
            // System.out.println(position1Requested);
            if (null != goToPosition2) {

                boolean gotoPos2 = StringUtils.equalsIgnoreCase(goToPosition2.getValue(), "true");

                if (gotoPos2) {
                    cylinder.moveToPosition2();
                } else {
                    cylinder.moveToPosition1();
                }
            }

            // DEBUG
            // VariableInstance magnetVentil_5_2 =
            // programInstance.getElement("AKT_5_2_MV0");
            // VariableInstance sr0 = magnetVentil_5_2.getElement("SR0");
            // System.out.println(sr0);

            try {
                Thread.sleep(3000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        }
    }

    private static VariableInstance instantiateStruct(Struct structToCreate, TypeScope globalTypeScope,
            ProgramInstance programInstance) {

        VariableInstance variableInstance = new VariableInstance();
        variableInstance.setDataType(structToCreate);

        for (Map.Entry<String, Field> mapEntry : structToCreate.getFields().entrySet()) {

            Field entry = mapEntry.getValue();

            if (entry.getDataType() instanceof FunctionBlock) {

                VariableInstance fbVariableInstance = instantiateFunctionBlock((FunctionBlock) entry.getDataType(),
                        globalTypeScope, variableInstance);
                fbVariableInstance.setName(entry.getName());
                variableInstance.addElement(fbVariableInstance);

            } else {

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

                // normal variables (non-inout) are instantiated as local variables

                VariableInstance simpleVariableInstance = new VariableInstance();
                simpleVariableInstance.setName(entry.getName());
                simpleVariableInstance.setDataType(entry.getDataType());

                if (entry.getDataType() instanceof Struct) {
                    instantiateStruct(entry, simpleVariableInstance);
                }

                // initial / default value
                if (StringUtils.isNotBlank(entry.getInitialValue())) {
                    simpleVariableInstance.setValue(entry.getInitialValue());
                } else {
                    simpleVariableInstance.setValue((String) entry.getDataType().getDefaultValue());
                }

                variableInstance.addElement(simpleVariableInstance);
            }
        }

        //
        // Instantiate objects belonging to a 'sequential function chart' language
        // FunctionBlock
        //

        // copy actions into the instance
        variableInstance.getAllActions().addAll(functionBlock.getAllActions());

        // copy transitions into the instance
        variableInstance.getTempTransitions().addAll(functionBlock.getTempTransitions());

        // copy steps into the instance
        Map<String, Step> newMap = new HashMap<String, Step>();
        newMap.putAll(functionBlock.getSteps());
        variableInstance.setSteps(newMap);

        // initialize the instance (establish connections between steps, actions,
        // transitions)
        variableInstance.initialize();

        return variableInstance;
    }

    public static void instantiateStruct(Variable entry, VariableInstance simpleVariableInstance) {
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
