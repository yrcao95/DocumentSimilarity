package com.paloaltonetworks.documentsimilarity.Controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Enumeration;
import java.util.Scanner;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipInputStream;

@RestController
public class UploadController {
  @PostMapping("/upload")
  @ResponseBody
  public String handleFileUpload(@RequestParam("file") MultipartFile file) throws IOException {
    if (!file.isEmpty()) {
      File tmpZip = File.createTempFile("lib", ".tmp");
      file.transferTo(tmpZip);
      ZipFile zipFile = new ZipFile(tmpZip);
      Enumeration<? extends ZipEntry> entries = zipFile.entries();
      while (entries.hasMoreElements()) {
        ZipEntry entry = entries.nextElement();
        InputStream stream = zipFile.getInputStream(entry);
        StringBuilder textBuilder = new StringBuilder();
        try (Reader reader =
            new BufferedReader(
                new InputStreamReader(stream, Charset.forName(StandardCharsets.UTF_8.name())))) {
          int c = 0;
          while ((c = reader.read()) != -1) {
            textBuilder.append((char) c);
          }
        }
        System.out.println(textBuilder.toString());
      }
      tmpZip.delete();
      //      ZipInputStream zipInputStream = new ZipInputStream(file.getInputStream());
      //      while (zipInputStream.getNextEntry() != null) {
      //        ZipEntry entry = zipInputStream.getNextEntry();
      //        System.out.println(entry.getName());
      //        Scanner sc = new Scanner(zipInputStream);
      //        while (sc.hasNextLine())
      //        {
      //          System.out.println(sc.nextLine());
      //        }
      //      }

      return "Upload successfully!";
    }
    return "File is empty!";
  }
}
