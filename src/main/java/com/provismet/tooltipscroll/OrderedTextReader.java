package com.provismet.tooltipscroll;

import net.minecraft.text.CharacterVisitor;
import net.minecraft.text.OrderedText;
import net.minecraft.text.Style;

/*
 * OrderedText actually uses the "Visitor" design paradigm, which is why trying to read the contents seems extremely obtuse.
 * This class defines a CharacterVisitor to read the contents of the OrderedText back into a String.
 */
public class OrderedTextReader {
    private static class Visitor implements CharacterVisitor {
        private int finalIndex = -1;
        private final StringBuilder outputBuilder = new StringBuilder();

        public Visitor () {}

        public boolean accept (int index, Style style, int codePoint) {
            if (index > this.finalIndex) this.finalIndex = index;
            else return false; // Only possible if the same index is re-read.

            outputBuilder.append((char)codePoint);
            return true;
        }

        public String getString () {
            return outputBuilder.toString();
        }
    }

    public static String read (OrderedText text) {
        Visitor visitor = new Visitor();

        /*
         * I need to try and explain how OrderedText.accept works, both for my future self and anyone else who ends up here trying to figure this out.
         * The function signature says that it wants a CharacterVisitor, but elsewhere in the Minecraft source it instead receives a lambda function.
         * I couldn't find a concrete implementation for this method, but testing shows that it acts like a for-each loop over the text, calling the lambda.
         * 
         * The lambda takes the parameters index, style, and codePoint.
         *  index = The current index location in the text, obviously.
         *  style = A Style object, it's what Minecraft uses to describe which characters are bold, have a colour, are underlined, etc.
         *  codePoint = The ASCII code (an integer) that represents the current character. You can cast this to char to get the letter out of it.
         * 
         * If you try printing out the index, you'll find it simply incrementing by 1 each time.
         * 
         * I am not currently sure what the significance of the boolean output is on the execution of OrderedText.accept() if any.
         * I chose to have my CharacterVisitor return false when a repeated index occurred, just in case an infinite loop was possible.
         * 
         * There is an example of this at TextHandler.getStyleAt(OrderedText, int)
         */
        text.accept((index, style, codePoint) -> {
            return visitor.accept(index, style, codePoint);
        });

        return visitor.getString();
    }
}
