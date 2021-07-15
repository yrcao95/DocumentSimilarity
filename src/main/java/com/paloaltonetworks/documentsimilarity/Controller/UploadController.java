package com.paloaltonetworks.documentsimilarity.Controller;

import org.apache.tika.detect.DefaultDetector;
import org.apache.tika.detect.Detector;
import org.apache.tika.exception.TikaException;
import org.apache.tika.metadata.Metadata;
import org.apache.tika.mime.MediaType;
import org.apache.tika.parser.AutoDetectParser;
import org.apache.tika.parser.ParseContext;
import org.apache.tika.parser.Parser;
import org.apache.tika.sax.BodyContentHandler;
import org.apache.tika.sax.ContentHandlerFactory;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.xml.sax.ContentHandler;
import org.xml.sax.SAXException;

import java.io.*;
import java.nio.Buffer;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Enumeration;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

@RestController
public class UploadController {
  @PostMapping("/upload")
  @ResponseBody
  public String handleFileUpload(@RequestParam("file") MultipartFile file) throws IOException, TikaException, SAXException {
    if (!file.isEmpty()) {
      StringBuilder sb = new StringBuilder();
      File tmpZip = File.createTempFile("lib", ".tmp");
      file.transferTo(tmpZip);
      ZipFile zipFile = new ZipFile(tmpZip);
      Enumeration<? extends ZipEntry> entries = zipFile.entries();
      while (entries.hasMoreElements()) {
        ZipEntry entry = entries.nextElement();
        InputStream inputStream = zipFile.getInputStream(entry);
        AutoDetectParser parser = new AutoDetectParser();
        ContentHandler handler = new BodyContentHandler();
        Metadata metadata = new Metadata();
        parser.parse(inputStream, handler, metadata);
        sb.append("Content is: ").append(handler.toString()).append("\n");
        sb.append("Metadata is: ").append(metadata.toString()).append("\n");
//        try (Reader reader =
//            new BufferedReader(
//                new InputStreamReader(stream, Charset.forName(StandardCharsets.UTF_8.name())))) {
//          int c = 0;
//          while ((c = reader.read()) != -1) {
//            textBuilder.append((char) c);
//          }
//        }
//        System.out.println(textBuilder);
      }
      tmpZip.delete();
      return sb.toString();
    }
    return "File is empty!";
  }


  @PostMapping("/uploadInfo")
  @ResponseBody
  public String showFileInfo(@RequestParam("file") MultipartFile file) throws IOException, TikaException, SAXException {
    InputStream inputStream = file.getInputStream();
    AutoDetectParser parser = new AutoDetectParser();
    ContentHandler handler = new BodyContentHandler();
    Metadata metadata = new Metadata();
    parser.parse(inputStream, handler, metadata);
    StringBuilder sb = new StringBuilder();
    sb.append("Content is: ").append(handler.toString()).append("\n");
    sb.append("Metadata is: ").append(metadata.toString()).append("\n");
    return sb.toString();
  }
}
