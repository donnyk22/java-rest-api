package com.github.donnyk22.services.word;

import com.github.donnyk22.models.forms.ApplicationLetterForm;
import com.github.donnyk22.models.forms.students.StudentsFindForm;

public interface WordService {

    byte[] generateWordApplicationLetter(ApplicationLetterForm form);

    byte[] generateWordApplicationLetterWithExistingTemplate(ApplicationLetterForm form);

    byte[] generateWordStudentData(StudentsFindForm form);
}
