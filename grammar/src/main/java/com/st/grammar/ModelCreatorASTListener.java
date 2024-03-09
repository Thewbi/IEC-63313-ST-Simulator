package com.st.grammar;

import org.apache.commons.lang3.StringUtils;

import model.DataType;
import model.Program;
import model.Variable;

public class ModelCreatorASTListener extends StructuredTextBaseListener {

    private Program program = null;

    private Variable variable = null;

    @Override
    public void enterProgram_declaration(StructuredTextParser.Program_declarationContext ctx) {
        // System.out.println(ctx.getClass().getSimpleName() + " " +
        // ctx.getStart().getText());

        program = new Program();
    }

    @Override
    public void exitProgram_declaration(StructuredTextParser.Program_declarationContext ctx) {
        System.out.println(program);
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
    public void enterSimple_spec_init(StructuredTextParser.Simple_spec_initContext ctx) {
        System.out.println(ctx.getClass().getSimpleName() + " " + ctx.getStart().getText());

        DataType dataType = DataType.valueOf(ctx.getStart().getText());
        variable.setDataType(dataType);
    }

}
