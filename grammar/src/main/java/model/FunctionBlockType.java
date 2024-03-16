package model;

/**
 * <ul>
 * <li>Ladder diagram (LD), graphical</li>
 * <li>Function block diagram (FBD), graphical</li>
 * <li>Structured text (ST), textual</li>
 * <li>Instruction list (IL), textual (deprecated in 3rd edition of the
 * standard[3])</li>
 * <li>Sequential function chart (SFC), has elements to organize programs for
 * sequential and parallel control processing, graphical.</li>
 * </ul>
 */
public enum FunctionBlockType {

    FUNCTION_BLOCK_DIAGRAM,

    INSTRUCTION_LIST,

    LADDER_DIAGRAM,

    SEQUENTIAL_FUNCTION_CHART,

    STRUCTURED_TEXT

}
