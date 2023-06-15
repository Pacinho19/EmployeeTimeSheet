package pl.raportsa.timesheet.utils;

import com.google.common.hash.Hashing;

import java.nio.charset.StandardCharsets;
import java.sql.Timestamp;
import java.util.Objects;

public class ChecksumUtils {

    public static String hash(String signFile, Timestamp signDate) {
       return  "#CHECK#" +
               Hashing.sha256()
                       .hashString((signFile + signDate.toString()), StandardCharsets.UTF_8);
    }

    public static boolean isEquals(String srcHash, String dbHash){
        return Objects.equals(dbHash, srcHash);
    }
}
