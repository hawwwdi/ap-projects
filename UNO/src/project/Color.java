package project;

/**
 * this is colors enum
 */
public enum Color {
    RED, BLUE, GREEN, YELLOW, BLACK, WHITE, CYAN;
    private static final String ANSI_RESET = "\u001B[0m";
    private static final String ANSI_BLACK = "\u001B[30m";
    private static final String ANSI_RED = "\u001B[31m";
    private static final String ANSI_GREEN = "\u001B[32m";
    private static final String ANSI_YELLOW = "\u001B[33m";
    private static final String ANSI_BLUE = "\u001B[34m";
    private static final String ANSI_PURPLE = "\u001B[35m";
    private static final String ANSI_CYAN = "\u001B[36m";

    public static String changeColor(Color color) {
        if (color == RED)
            return ANSI_RED;
        else if (color == BLUE)
            return ANSI_BLUE;
        else if (color == GREEN)
            return ANSI_GREEN;
        else if (color == YELLOW)
            return ANSI_YELLOW;
        else if (color == BLACK)
            return ANSI_PURPLE;
        else if (color == WHITE)
            return ANSI_BLACK;
        else if (color == CYAN)
            return ANSI_CYAN;
        return ANSI_RESET;
    }

    public static char getChar(Color color) {
        if (color == Color.RED)
            return  '♥';
        else if (color == Color.BLUE)
            return  '♦';
        else if (color == Color.YELLOW)
            return  '♣';
        else
            return  '♠';
    }

}
