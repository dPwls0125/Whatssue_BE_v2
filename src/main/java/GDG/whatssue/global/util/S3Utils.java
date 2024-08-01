package GDG.whatssue.global.util;

public class S3Utils {
    private static String PATH = "https://whatssue.s3.ap-northeast-2.amazonaws.com/";

    public static String getFullPath(String fileName) {
        return PATH + fileName;
    }
}
