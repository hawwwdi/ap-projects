package project;

/**
 * the interface use as marker for wild card
 */
public interface Wild {
    /**
     * this method use to select color when player put a wild card
     *
     * @param isUser to show players type
     */
    void changeColor(boolean isUser);

    /**
     * it use to reset the card color when its used
     */
    void resetColor();
}
