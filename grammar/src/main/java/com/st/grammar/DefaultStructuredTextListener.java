package com.st.grammar;

public class DefaultStructuredTextListener extends StructuredTextBaseListener {

	@Override
	public void enterProgram_type_name(final StructuredTextParser.Program_type_nameContext ctx) {
		System.out.println("Found Program_type_name! Identifier: " + ctx.IDENTIFIER());
	}

	@Override
	public void enterVar1_init_decl(final StructuredTextParser.Var1_init_declContext ctx) {
		System.out.println(ctx);
	}

}
