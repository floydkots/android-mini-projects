package pro.kots.advancedstringmanipulationdemo;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class StringManipulation {
    public static void main(String[] args) {
        String river = "<div class=\"image\">\n" +
                "\t\t\t\t\t\t<img src=\"http://cdn.posh24.se/images/:profile/c/2696692\" alt=\"Benjamin Wahlgren Ingrosso\"/>\n" +
                "\t\t\t\t\t</div>";
        Pattern pattern = Pattern.compile("src=\"(.*?)\"");
        Matcher matcher = pattern.matcher(river);

        while (matcher.find()) {
            System.out.println(matcher.group(1));
        }
    }
}
