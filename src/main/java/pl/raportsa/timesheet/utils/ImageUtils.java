package pl.raportsa.timesheet.utils;

import org.apache.tomcat.util.codec.binary.Base64;
import pl.raportsa.timesheet.model.enums.ErrorMessage;
import pl.raportsa.timesheet.model.exceptions.SignatureNotFoundException;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

public class ImageUtils {

    public static String encodeFileToBase64Binary(String path) {
        if (path == null || path.isEmpty()) return null;
        FileInputStream fileInputStreamReader = null;
        try {
            File file = new File(FileUtils.CONSTANT_PATH + path);
            fileInputStreamReader = new FileInputStream(file);
            byte[] bytes = new byte[(int) file.length()];
            fileInputStreamReader.read(bytes);
            return new String(Base64.encodeBase64(bytes), StandardCharsets.UTF_8);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
            //throw new SignatureNotFoundException(ErrorMessage.SIGN_NOT_FOUND.getMessage());
        } finally {
            try {
                if (fileInputStreamReader != null)
                    fileInputStreamReader.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

}
