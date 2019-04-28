package com.marifnst.tutorial.decompression;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.zip.GZIPInputStream;

import org.apache.commons.compress.archivers.ArchiveInputStream;
import org.apache.commons.compress.archivers.ArchiveStreamFactory;
import org.apache.commons.compress.archivers.sevenz.SevenZArchiveEntry;
import org.apache.commons.compress.archivers.sevenz.SevenZFile;
import org.apache.commons.compress.archivers.tar.TarArchiveEntry;
import org.apache.commons.compress.archivers.tar.TarArchiveInputStream;
import org.apache.commons.compress.archivers.zip.ZipArchiveEntry;
import org.apache.commons.compress.compressors.gzip.GzipCompressorInputStream;
import org.apache.commons.compress.utils.IOUtils;

public class SampleDecompression {
    public void extractZip(String sourceFilePath, String destinationFilePath) throws Exception {
        File input = new File(sourceFilePath);
        InputStream is = new FileInputStream(input);
        ArchiveInputStream in = new ArchiveStreamFactory().createArchiveInputStream("zip", is);
        ZipArchiveEntry entry = null;
        // ZipArchiveEntry entry = (ZipArchiveEntry) in.getNextEntry();
        while((entry = (ZipArchiveEntry) in.getNextEntry()) != null) {
            OutputStream out = new FileOutputStream(new File(destinationFilePath, entry.getName()));
            IOUtils.copy(in, out);
            out.close();
        }
        in.close();
    }

    public void extract7z(String sourceFilePath, String destinationFilePath) throws Exception {
        SevenZFile sevenZFile = new SevenZFile(new File(sourceFilePath));
        SevenZArchiveEntry entry;
        while ((entry = sevenZFile.getNextEntry()) != null) {
            if (entry.isDirectory()) {
                continue;
            }
            File curfile = new File(destinationFilePath, "7z_" + entry.getName());
            File parent = curfile.getParentFile();
            if (!parent.exists()) {
                parent.mkdirs();
            }
            FileOutputStream out = new FileOutputStream(curfile);
            byte[] content = new byte[(int) entry.getSize()];
            sevenZFile.read(content, 0, content.length);
            out.write(content);
            out.close();
        }
        sevenZFile.close();
    }

    public void extractGzip(String sourceFilePath, String destinationFilePath) throws Exception {
        byte[] buffer = new byte[1024];
        FileInputStream fileIn = new FileInputStream(sourceFilePath);
        GZIPInputStream gZIPInputStream = new GZIPInputStream(fileIn);
        FileOutputStream fileOutputStream = new FileOutputStream(destinationFilePath);
        int bytes_read;
        while ((bytes_read = gZIPInputStream.read(buffer)) > 0) {
            fileOutputStream.write(buffer, 0, bytes_read);
        }
        gZIPInputStream.close();
        fileOutputStream.close();
    }

    public void extractTarGz(String decompressionMethod, String sourceFilePath, String destinationFilePath) throws Exception {
        TarArchiveInputStream fin = null;
        switch(decompressionMethod) {
            case "TAR":{
                fin = new TarArchiveInputStream(new FileInputStream(sourceFilePath));
                break;
            }
            case "TAR_GZ":{
                fin = new TarArchiveInputStream(new GzipCompressorInputStream(new FileInputStream(sourceFilePath)));
                break;
            }
        }

        TarArchiveEntry entry;
        while ((entry = fin.getNextTarEntry()) != null) {
            if (entry.isDirectory()) {
                continue;
            }
            File curfile = new File(destinationFilePath + entry.getName());
            File parent = curfile.getParentFile();
            if (!parent.exists()) {
                parent.mkdirs();
            }
            IOUtils.copy(fin, new FileOutputStream(curfile));
            System.out.println("tesss");
        }
        fin.close();
    }
}