package net.neszku.patch_tracker.helpers;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.util.jar.JarFile;

public class IOHelper {

    public static File getJar() {
        return new File(IOHelper.class.getProtectionDomain().getCodeSource().getLocation().getFile());
    }

    public static File getJarDirectory() {
        return getJar().getParentFile();
    }

    public static void saveResource(String resource, File to) {
        if (to.exists()) return;
        try (JarFile jar = new JarFile(getJar());
             InputStream stream = jar.getInputStream(jar.getJarEntry(resource))) {

            Files.write(to.toPath(), readBytes(stream));
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static byte[] readBytes(File file) {
        try (InputStream stream = Files.newInputStream(file.toPath())) {
            return readBytes(stream);
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        return new byte[0];
    }

    public static byte[] readBytes(InputStream is) throws IOException {
        try (ByteArrayOutputStream os = new ByteArrayOutputStream()) {
            byte[] buffer = new byte[0xFFFF];
            for (int len = is.read(buffer); len != -1; len = is.read(buffer)) {
                os.write(buffer, 0, len);
            }
            return os.toByteArray();
        }
    }

}
