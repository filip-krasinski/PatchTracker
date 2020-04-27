package net.neszku.patch_tracker.helpers;

import net.neszku.patch_tracker.page.IPageCluster;
import net.neszku.patch_tracker.page.impl.PageClusterImpl;
import net.neszku.patch_tracker.page.impl.PageImpl;
import net.neszku.patch_tracker.patch.Format;

public class ParserHelper {

    public static String parse(Format src, Format dest, String str) {
        if (src == Format.BBCODE && dest == Format.MARKDOWN) {
            return BBCode.toMarkdown(str);
        }

        return str;
    }

    private static final int CHUNK_SIZE = 1600;
    private static final int MAX_LOOK_BEHIND_FOR_LINE_BREAK = 50;

    // do not ask
    public static IPageCluster<String> chunk(String s) {
        IPageCluster<String> pages = new PageClusterImpl<>();
        int chunksSize = (int) Math.ceil((double) s.length() / CHUNK_SIZE);
        int characters = s.length() / chunksSize;
        char[] chars = s.toCharArray();
        int start = 0;
        for (int i = 0; i < chunksSize; i++) {
            int end = (i + 1) * characters;
            int latestBreak = 0;
            for (int j = start; j < chars.length; j++) {
                if (chars[j] == '\n') {
                    latestBreak = j;
                }
                if (j >= end && chars[j] == ' ') {
                    if (j-latestBreak < MAX_LOOK_BEHIND_FOR_LINE_BREAK) {
                        pages.addPage(new PageImpl<>(pages, s.substring(start, latestBreak)));
                        start = latestBreak;
                    }
                    else {
                        pages.addPage(new PageImpl<>(pages, s.substring(start, j)));
                        start = j;
                    }
                    break;
                }
            }
        }
        String last = s.substring(start);
        if (last.trim().length() > 0) {
            pages.addPage(new PageImpl<>(pages, s.substring(start)));
        }
        return pages;
    }

}
