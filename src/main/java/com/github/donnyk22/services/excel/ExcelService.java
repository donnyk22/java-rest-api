package com.github.donnyk22.services.excel;

import java.util.List;
import java.util.Map;

import org.springframework.web.multipart.MultipartFile;

import com.github.donnyk22.models.forms.students.StudentsFindForm;

public interface ExcelService {

    byte[] exportToNewExcel(StudentsFindForm form);

    byte[] exportToExistingExcelTemplate(StudentsFindForm form);

    List<Map<String, Object>> readImportedExcelFile(MultipartFile file);

}
