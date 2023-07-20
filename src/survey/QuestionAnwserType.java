package survey;

/**
 * Specifies type of field used by question answers and thus 
 * question type itself. 
 * 
 * @author ArtiFixal
 */
public enum QuestionAnwserType {
    
    /**
     * Multi choice question.
     */
    CHECKBOX(0),
    
    /**
     * Single choice question.
     */
    RADIO(1),
    
    /**
     * Open-ended question.
     */
    INPUT(2),
    
    /**
     * Single choice question.
     */
    COMBO_BOX(3);
    
    final byte questionType;
    
    private QuestionAnwserType(int questionType) {
        this.questionType =(byte) questionType;
    }
    
    public byte getQuestionTypeID()
    {
        return questionType;
    }
    
    @Override
    public String toString()
    {
        return switch (questionType) {
            case 0 -> "checkbox";
            case 1 -> "radio";
            case 2 -> "input";
            case 3 -> "combo box";
            default -> throw new IllegalArgumentException("Illegal question type of ID: "+questionType);
        };
    }
    
    /**
     * 
     * @param questionType Value of enum.
     * @return Enum of given value. 
     */
    public static final QuestionAnwserType createEnumElement(int questionType)
    {
	return switch (questionType) {
            case 0 -> CHECKBOX;
            case 1 -> RADIO;
            case 2 -> INPUT;
            case 3 -> COMBO_BOX;
            default -> throw new IllegalArgumentException("Illegal question type of ID: "+questionType);
        };
    }
    
    public static final String[] getStringValues()
    {
        final String[] values={"Multi choice question",
        "Single choice question",
        "Open ended question",
        "Single choice question (combo box)"
        };
        return values;
    }
}
