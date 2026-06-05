package com.iexceed.appzillon.appstore.util;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class PlistUtils {

    public static void generateIpaPlist(Path targetPath, String bundleId, String version, String title, String ipaUrl) throws IOException {
        String plistContent = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
                "<!DOCTYPE plist PUBLIC \"-//Apple//DTD PLIST 1.0//EN\" \"http://www.apple.com/DTDs/PropertyList-1.0.dtd\">\n" +
                "<plist version=\"1.0\">\n" +
                "<dict>\n" +
                "    <key>items</key>\n" +
                "    <array>\n" +
                "        <dict>\n" +
                "            <key>assets</key>\n" +
                "            <array>\n" +
                "                <dict>\n" +
                "                    <key>kind</key>\n" +
                "                    <string>software-package</string>\n" +
                "                    <key>url</key>\n" +
                "                    <string>" + ipaUrl + "</string>\n" +
                "                </dict>\n" +
                "            </array>\n" +
                "            <key>metadata</key>\n" +
                "            <dict>\n" +
                "                <key>bundle-identifier</key>\n" +
                "                <string>" + bundleId + "</string>\n" +
                "                <key>bundle-version</key>\n" +
                "                <string>" + version + "</string>\n" +
                "                <key>kind</key>\n" +
                "                <string>software</string>\n" +
                "                <key>title</key>\n" +
                "                <string>" + title + "</string>\n" +
                "            </dict>\n" +
                "        </dict>\n" +
                "    </array>\n" +
                "</dict>\n" +
                "</plist>";
        Files.writeString(targetPath, plistContent);
    }
}
