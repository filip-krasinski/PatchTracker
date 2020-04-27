package net.neszku.patch_tracker.helpers;

import java.util.LinkedHashMap;
import java.util.Map;

public class BBCode {

    private static final Map<String, String> PATTERNS = new LinkedHashMap<>();
    static {
        PATTERNS.put("\\[url=(.*?)](.*?)\\[/url]", "[$2]($1)");
        PATTERNS.put("\\[spoiler](?s:(.*?))\\[/spoiler]", "||$1||");
        PATTERNS.put("\\[strike](?s:(.*?))\\[/strike]", "~~$1~~");
        PATTERNS.put("\\[h[1-6]](?s:(.*?))\\[/h[1-6]]", "**$1**");
        PATTERNS.put("\\[u](?s:(.*?))\\[/u]", "__$1__");
        PATTERNS.put("\\[i](?s:(.*?))\\[/i]", "_$1_");
        PATTERNS.put("\\[b](?s:(.*?))\\[/b]", "**$1**");
        PATTERNS.put("\\[(/?|o?|/o)list]", "");
        PATTERNS.put("\\[\\*](.*?)", "*");
        PATTERNS.put("\\[quote=(.*?)](?s:(.*?))\\[/quote]", "```$1$2```");
        PATTERNS.put("\\[code](?s:(.*?))\\[/code]", "```$1```");
        PATTERNS.put("\\[img](.*?)\\[/img]", "");//we dont want images
        PATTERNS.put("\\[previewyoutube=(.*?)](.*?)\\[/previewyoutube]", "");//we dont want yt
    }

    public static String toMarkdown(String str) {
        for (Map.Entry<String, String> entry : PATTERNS.entrySet()) {
            str = str.replaceAll(entry.getKey(), entry.getValue());
        }
        return str.replaceAll("[\r\n][\r\n]+", "\r\n\r\n");
    }
}