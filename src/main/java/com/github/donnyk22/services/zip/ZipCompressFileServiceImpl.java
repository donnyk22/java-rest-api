package com.github.donnyk22.services.zip;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.StreamingResponseBody;

import com.github.donnyk22.exceptions.InternalServerErrorException;
import com.github.donnyk22.models.forms.students.StudentsFindForm;
import com.github.donnyk22.services.excel.ExcelService;
import com.github.donnyk22.services.word.WordService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class ZipCompressFileServiceImpl implements ZipCompressFileService {

    private final ExcelService excelService;
    private final WordService wordService;

    @Override
    public byte[] generateZipInMemory(StudentsFindForm form) {
        StudentsFindForm param = new StudentsFindForm()
                .setAcademicYear(form.getAcademicYear());
        param.setPage(0)
                .setSize(Integer.MAX_VALUE)
                .setKeyword(form.getKeyword());
        byte[] excelBytes = excelService.exportToNewExcel(param);
        byte[] wordBytes = wordService.generateWordStudentData(param);

        try (ByteArrayOutputStream baos = new ByteArrayOutputStream();
                ZipOutputStream zos = new ZipOutputStream(baos)) {

            // Put excel file into ZIP
            ZipEntry excelEntry = new ZipEntry("students-export.xlsx");
            zos.putNextEntry(excelEntry);
            zos.write(excelBytes);
            zos.closeEntry();

            // Put word file into ZIP
            ZipEntry wordEntry = new ZipEntry("students-export.docx");
            zos.putNextEntry(wordEntry);
            zos.write(wordBytes);
            zos.closeEntry();

            zos.finish();
            return baos.toByteArray();
        } catch (Exception e) {
            log.error("Failed to generate ZIP in memory: " + e.getMessage());
            throw new InternalServerErrorException("Failed to generate ZIP in memory: " + e.getMessage(), e);
        }
    }

    @Override
    public StreamingResponseBody generateZipInDisk(StudentsFindForm form) {
        StudentsFindForm param = new StudentsFindForm()
                .setAcademicYear(form.getAcademicYear());
        param.setPage(0)
                .setSize(Integer.MAX_VALUE)
                .setKeyword(form.getKeyword());
        byte[] excelBytes = excelService.exportToNewExcel(param);
        byte[] wordBytes = wordService.generateWordStudentData(param);

        File tempZipFile;

        try {
            // temp name example on disk: export-data-482910472391.zip
            tempZipFile = File.createTempFile("export-data-", ".zip");

            // write the data to file in harddisk via fileoutputstream
            try (FileOutputStream fos = new FileOutputStream(tempZipFile);
                    ZipOutputStream zos = new ZipOutputStream(fos)) {

                // Put excel file into ZIP
                ZipEntry excelEntry = new ZipEntry("students-export.xlsx");
                zos.putNextEntry(excelEntry);
                zos.write(excelBytes);
                zos.closeEntry();

                // Put word file into ZIP
                ZipEntry wordEntry = new ZipEntry("students-export.docx");
                zos.putNextEntry(wordEntry);
                zos.write(wordBytes);
                zos.closeEntry();

                zos.finish();
            }
        } catch (Exception e) {
            log.error("Failed to generate ZIP on disk: " + e.getMessage());
            throw new InternalServerErrorException("Failed to generate ZIP on disk: " + e.getMessage(), e);
        }

        return outputStream -> {
            try (InputStream inputStream = new FileInputStream(tempZipFile)) {
                // Stream by 4KB to keep RAM at a constant 4KB (Example: a 4MB file will trigger
                // 1,024x read-write loops from disk to network, instead of loading 4MB at once
                // into memory)
                // 4KB is the standard size of one cluster/page in modern operating system and
                // hard disk architectures (NTFS, EXT4)
                byte[] buffer = new byte[4096];
                int bytesRead;
                while ((bytesRead = inputStream.read(buffer)) != -1) {
                    outputStream.write(buffer, 0, bytesRead);
                }
                outputStream.flush();
            } finally {
                if (tempZipFile.exists() && !tempZipFile.delete()) {
                    log.warn("Failed to delete temporary zip file: {}", tempZipFile.getAbsolutePath());
                }
            }
        };
    }
}
