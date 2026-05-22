package com.github.donnyk22.services.zip;

import org.springframework.web.servlet.mvc.method.annotation.StreamingResponseBody;

import com.github.donnyk22.models.forms.students.StudentsFindForm;

public interface ZipCompressFileService {

    byte[] generateZipInMemory(StudentsFindForm form); // simple, but high memory usage

    StreamingResponseBody generateZipInDisk(StudentsFindForm form); // memory safe, recommended for high traffic/heavy
                                                                    // files

}
