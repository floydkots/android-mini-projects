package pro.kots.guessthecelebrity;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CelebNameImageFetcher {
    private String html;

    protected CelebNameImageFetcher(String html) {
        this.html = html;
    }

    private String getArticleContainer() {
        return html.split("<div class=\"sidebarContainer\">")[0];
    }

    protected Map<String, String> getNamesAndImages() {
        String article = getArticleContainer();
        Map<String, String> celebs = new HashMap<>();
        Pattern pattern = Pattern.compile("img src=\"(.*?)\" alt=\"(.*?)\"");
        Matcher matcher = pattern.matcher(article);
        while (matcher.find()) {
            String name = matcher.group(1);
            String src = matcher.group(2);
            celebs.put(src, name);
        }
        return celebs;
    }
}
