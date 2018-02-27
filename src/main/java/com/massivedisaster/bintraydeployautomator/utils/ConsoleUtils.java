package com.massivedisaster.bintraydeployautomator.utils;

public class ConsoleUtils {

    public static void DrawInConsoleBox(String s) {
        String ulCorner = "╔";
        String llCorner = "╚";
        String urCorner = "╗";
        String lrCorner = "╝";
        String vertical = "║";
        String horizontal = "═";

        String[] lines = s.split("[\r\n]");

        int longest = 0;
        for (String line : lines) {
            if (line.length() > longest) {
                longest = line.length();
            }
        }
        int width = longest + 2; // 1 space on each side

        // box top
        StringBuilder sb = new StringBuilder();
        sb.append(ulCorner);
        for (int i = 0; i < width; i++) {
            sb.append(horizontal);
        }
        sb.append(urCorner);
        sb.append("\n");

        // box contents
        for (String line : lines) {
            double dblSpaces = ((width - line.length()) / (double) 2);
            int iSpaces = (int) dblSpaces;

            // not an even amount of chars
            if (dblSpaces > iSpaces) {
                iSpaces += 1; // round up to next whole number
            }

            String beginSpacing = "";
            String endSpacing = "";
            for (int i = 0; i < iSpaces; i++) {
                beginSpacing += " ";

                // if there is an extra space somewhere, it should be in the beginning
                if (!(iSpaces > dblSpaces && i == iSpaces - 1)) {
                    endSpacing += " ";
                }
            }
            // add the text line to the box
            sb.append(vertical)
                    .append(beginSpacing)
                    .append(line)
                    .append(endSpacing)
                    .append(vertical)
                    .append("\n");
        }

        // box bottom
        sb.append(llCorner);
        for (int i = 0; i < width; i++) {
            sb.append(horizontal);
        }
        sb.append(lrCorner)
                .append("\n");

        // the finished box
        System.out.println(sb);
    }

}
