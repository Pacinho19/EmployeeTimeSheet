package pl.raportsa.timesheet.utils;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Date;

public class FileUtils {

    private final static String SLASH = "/";
    public final static String CONSTANT_PATH = "Timesheet\\";
    private final static String OUT_FORMAT = "png";

    public static String saveImage(String userName, String imgUrl) {
        String name = DateUtils.formatWithoutSeparators(new Date()) + "_" + imgUrl.hashCode() + "." + OUT_FORMAT;

        String fullPath =  CONSTANT_PATH + userName;
        new File(fullPath).mkdirs();
        String destPath = (fullPath + SLASH + name);

        try {
            String base64Image = imgUrl.split(",")[1];
            byte[] imageBytes = javax.xml.bind.DatatypeConverter.parseBase64Binary(base64Image);
            BufferedImage img = ImageIO.read(new ByteArrayInputStream(imageBytes));
            File outFile = new File(destPath);
            ImageIO.write(img, OUT_FORMAT, outFile);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return userName+ SLASH + name;
    }

}