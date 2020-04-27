package net.neszku.patch_tracker;

import net.neszku.patch_tracker.helpers.IOHelper;
import org.json.JSONObject;

import java.io.File;

public class Config {

    public static String BOT_TOKEN;
    public static String COMMAND_PREFIX;

    public static String MYSQL_HOST;
    public static int    MYSQL_PORT;
    public static String MYSQL_USER;
    public static String MYSQL_PASS;
    public static String MYSQL_BASE;
    public static boolean USE_SSL;

    static {
        byte[] bytes = IOHelper.readBytes(new File(IOHelper.getJarDirectory(), "config.json"));
        JSONObject object = new JSONObject(new String(bytes));
        JSONObject mysql  = object.getJSONObject("MYSQL");

        BOT_TOKEN      = object.getString("BOT_TOKEN");
        COMMAND_PREFIX = object.getString("COMMAND_PREFIX");
        MYSQL_HOST = mysql.getString("HOST");
        MYSQL_PORT = mysql.getInt("PORT");
        MYSQL_USER = mysql.getString("USER");
        MYSQL_PASS = mysql.getString("PASS");
        MYSQL_BASE = mysql.getString("BASE");
        USE_SSL    = mysql.getBoolean("USE_SSL");
    }

}
