package com.github.donnyk22.services.word;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigInteger;
import java.util.Map;

import org.apache.poi.xwpf.usermodel.ParagraphAlignment;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.poi.xwpf.usermodel.XWPFRun;
import org.apache.poi.xwpf.usermodel.XWPFTable;
import org.apache.poi.xwpf.usermodel.XWPFTableCell;
import org.apache.poi.xwpf.usermodel.XWPFTableRow;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTblWidth;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.STTblWidth;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.donnyk22.exceptions.InternalServerErrorException;
import com.github.donnyk22.models.dtos.FindResponse;
import com.github.donnyk22.models.dtos.MstStudentsDto;
import com.github.donnyk22.models.enums.TimeFormat;
import com.github.donnyk22.models.enums.UserGender;
import com.github.donnyk22.models.forms.ApplicationLetterForm;
import com.github.donnyk22.models.forms.students.StudentsFindForm;
import com.github.donnyk22.services.school.SchoolService;
import com.github.donnyk22.utils.ConverterUtil;

import fr.opensagres.xdocreport.core.XDocReportException;
import fr.opensagres.xdocreport.document.IXDocReport;
import fr.opensagres.xdocreport.document.registry.XDocReportRegistry;
import fr.opensagres.xdocreport.template.IContext;
import fr.opensagres.xdocreport.template.TemplateEngineKind;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class WordServiceImpl implements WordService {

    private final ConverterUtil converterUtil;
    private final ObjectMapper objectMapper;
    private final SchoolService schoolService;

    @Override
    public byte[] generateWordApplicationLetter(ApplicationLetterForm form) {
        XWPFDocument document = new XWPFDocument();
        ByteArrayOutputStream out = new ByteArrayOutputStream();

        String fontName = "Arial";
        int fontSize = 11;

        XWPFParagraph senderParagraph = document.createParagraph();
        senderParagraph.setAlignment(ParagraphAlignment.RIGHT);
        senderParagraph.setSpacingAfter(0);

        XWPFRun senderRun = senderParagraph.createRun();
        senderRun.setText(form.getApplicantAddress());
        senderRun.addBreak();
        senderRun.setText(form.getApplicantPhone());
        senderRun.addBreak();
        senderRun.setText(form.getApplicantEmail());
        senderRun.setFontFamily(fontName);
        senderRun.setFontSize(fontSize);

        addEmptyParagraphs(document, 2);

        // 2. Recipient Data (Left Aligned)
        XWPFParagraph recipientParagraph = document.createParagraph();
        recipientParagraph.setAlignment(ParagraphAlignment.LEFT);
        recipientParagraph.setSpacingAfter(0);

        XWPFRun recNameRun = recipientParagraph.createRun();
        recNameRun.setText(form.getRecipientName());
        recNameRun.setBold(true);
        recNameRun.setFontFamily(fontName);
        recNameRun.setFontSize(fontSize);
        recNameRun.addBreak();

        XWPFRun recTitleRun = recipientParagraph.createRun();
        recTitleRun.setText(form.getRecipientTitle());
        recTitleRun.setFontFamily(fontName);
        recTitleRun.setFontSize(fontSize);
        recTitleRun.addBreak();

        XWPFRun companyRun = recipientParagraph.createRun();
        companyRun.setText(form.getRecipientCompany());
        companyRun.setFontFamily(fontName);
        companyRun.setFontSize(fontSize);

        addEmptyParagraphs(document, 1);

        // 3. Date
        XWPFParagraph dateParagraph = document.createParagraph();
        dateParagraph.setAlignment(ParagraphAlignment.LEFT);

        XWPFRun dateRun = dateParagraph.createRun();
        String currentDate = converterUtil.localDateToString(null, TimeFormat.MMMM_D_YYYY);
        dateRun.setText("Date: " + currentDate);
        dateRun.setFontFamily(fontName);
        dateRun.setFontSize(fontSize);

        addEmptyParagraphs(document, 1);

        // 4. Greeting
        XWPFParagraph greetingParagraph = document.createParagraph();
        XWPFRun greetingRun = greetingParagraph.createRun();
        greetingRun.setText("Dear " + form.getRecipientName() + ",");
        greetingRun.setFontFamily(fontName);
        greetingRun.setFontSize(fontSize);

        addEmptyParagraphs(document, 1);

        // 5. Paragraph 1: Position & Source Media
        XWPFParagraph p1 = document.createParagraph();
        p1.setAlignment(ParagraphAlignment.BOTH); // justify

        XWPFRun p1Run1 = p1.createRun();
        p1Run1.setText("I am writing to apply for the post of the ");
        p1Run1.setFontFamily(fontName);
        p1Run1.setFontSize(fontSize);

        // Print bold target position in the middle of the sentence
        XWPFRun p1Position = p1.createRun();
        p1Position.setText(form.getTargetPosition());
        p1Position.setBold(true);
        p1Position.setFontFamily(fontName);
        p1Position.setFontSize(fontSize);

        XWPFRun p1Run2 = p1.createRun();
        p1Run2.setText(" which I saw advertised on ");
        p1Run2.setFontFamily(fontName);
        p1Run2.setFontSize(fontSize);

        // Print bold source media
        XWPFRun p1Media = p1.createRun();
        p1Media.setText(form.getSourceMedia());
        p1Media.setBold(true);
        p1Media.setFontFamily(fontName);
        p1Media.setFontSize(fontSize);

        XWPFRun p1Run3 = p1.createRun();
        p1Run3.setText(". Please find my enclosed CV.");
        p1Run3.setFontFamily(fontName);
        p1Run3.setFontSize(fontSize);

        addEmptyParagraphs(document, 1);

        // 6. Paragraph 2: Qualification & Experience
        XWPFParagraph p2 = document.createParagraph();
        p2.setAlignment(ParagraphAlignment.BOTH);

        XWPFRun p2Run = p2.createRun();
        p2Run.setText(
                "I feel I have the required qualifications for this role. I have extensive experience in the relevant industry and have successfully worked with modern architectures, bringing a solid track record of problem-solving and collaboration into development environments.");
        p2Run.setFontFamily(fontName);
        p2Run.setFontSize(fontSize);

        addEmptyParagraphs(document, 1);

        // 7. Paragraph 3: Closing
        XWPFParagraph p3 = document.createParagraph();
        p3.setAlignment(ParagraphAlignment.BOTH);

        XWPFRun p3Run = p3.createRun();
        p3Run.setText(
                "Should you be interested in my qualifications and experience, please do not hesitate to contact me. I look forward to the possibility of discussing my application further in an interview.");
        p3Run.setFontFamily(fontName);
        p3Run.setFontSize(fontSize);

        addEmptyParagraphs(document, 2);

        // 8. Signature & Name
        XWPFParagraph closingParagraph = document.createParagraph();
        closingParagraph.setAlignment(ParagraphAlignment.LEFT);
        closingParagraph.setSpacingAfter(0);

        XWPFRun closingRun = closingParagraph.createRun();
        closingRun.setText("Yours sincerely,");
        addLineBreaks(closingRun, 4);
        closingRun.setFontFamily(fontName);
        closingRun.setFontSize(fontSize);

        XWPFRun nameRun = closingParagraph.createRun();
        nameRun.setText(form.getApplicantName());
        nameRun.setBold(true);
        nameRun.setFontFamily(fontName);
        nameRun.setFontSize(fontSize);

        try {
            document.write(out);
        } catch (Exception e) {
            throw new InternalServerErrorException("Failed to generate Word document: " + e.getMessage(), e);
        }

        return out.toByteArray();
    }

    private void addEmptyParagraphs(XWPFDocument document, int count) {
        for (int i = 0; i < count; i++) {
            document.createParagraph();
        }
    }

    private void addLineBreaks(XWPFRun run, int count) {
        for (int i = 0; i < count; i++) {
            run.addBreak();
        }
    }

    @Override
    public byte[] generateWordApplicationLetterWithExistingTemplate(ApplicationLetterForm form) {
        try {
            // can handle docx or odt templates
            InputStream in = new ClassPathResource("templates/word_template_example.odt").getInputStream();

            IXDocReport report = XDocReportRegistry.getRegistry().loadReport(in, TemplateEngineKind.Freemarker);

            IContext context = report.createContext();

            Map<String, Object> finalDataModel = objectMapper.convertValue(form, Map.class);
            finalDataModel.put("letterDate", converterUtil.localDateToString(null, TimeFormat.MMMM_D_YYYY));

            context.putMap(finalDataModel);

            ByteArrayOutputStream out = new ByteArrayOutputStream();
            report.process(context, out);

            return out.toByteArray();
        } catch (IOException | XDocReportException e) {
            throw new InternalServerErrorException("Failed to generate Word document from template: " + e.getMessage(),
                    e);
        }
    }

    @Override
    public byte[] generateWordStudentData(StudentsFindForm form) {
        StudentsFindForm param = new StudentsFindForm()
                .setAcademicYear(form.getAcademicYear());
        param.setPage(0)
                .setSize(Integer.MAX_VALUE)
                .setKeyword(form.getKeyword());

        FindResponse<MstStudentsDto> response = schoolService.findStudents(param);

        // Initialize Word document (.docx)
        try (XWPFDocument document = new XWPFDocument()) {

            // Add title for student data report (if needed)
            XWPFParagraph titleParagraph = document.createParagraph();
            titleParagraph.setAlignment(ParagraphAlignment.CENTER);
            XWPFRun titleRun = titleParagraph.createRun();
            titleRun.setText("STUDENTS REPORT");
            titleRun.setBold(true);
            titleRun.setFontSize(16);
            titleParagraph.setSpacingAfter(200); // add space after title

            String[] headers = { "ID", "Full Name", "Gender", "Address", "Phone", "Created At", "Updated At" };
            int numRows = response.getRecords().size() + 1; // +1 for header row
            int numCols = headers.length;

            // create table object
            XWPFTable table = document.createTable(numRows, numCols);

            // Set width table auto fit to page (width 100%)
            CTTblWidth width = table.getCTTbl().getTblPr().addNewTblW();
            width.setType(STTblWidth.PCT);
            // 5000 means 100% of page width in the Word PCT unit
            width.setW(BigInteger.valueOf(5000));

            // header row in first page only
            XWPFTableRow headerRow = table.getRow(0);
            // To repeat header row in every new page
            headerRow.setRepeatHeader(true);

            for (int i = 0; i < headers.length; i++) {
                XWPFTableCell cell = headerRow.getCell(i);
                cell.setColor("D3D3D3");

                XWPFParagraph p = cell.getParagraphs().get(0);
                p.setAlignment(ParagraphAlignment.CENTER);

                XWPFRun run = p.createRun();
                run.setText(headers[i]);
                run.setBold(true);
                run.setFontSize(11);
            }

            int rowIndex = 1;
            for (MstStudentsDto student : response.getRecords()) {
                XWPFTableRow row = table.getRow(rowIndex++);

                setCellText(row.getCell(0), student.getId() != null ? student.getId().toString() : "",
                        ParagraphAlignment.CENTER);
                setCellText(row.getCell(1), student.getFullName() != null ? student.getFullName() : "",
                        ParagraphAlignment.LEFT);
                setCellText(row.getCell(2), student.getGender() != null ? UserGender.getVal(student.getGender()) : "",
                        ParagraphAlignment.CENTER);
                setCellText(row.getCell(3), student.getAddress() != null ? student.getAddress() : "",
                        ParagraphAlignment.LEFT);
                setCellText(row.getCell(4), student.getPhone() != null ? student.getPhone() : "",
                        ParagraphAlignment.LEFT);
                setCellText(row.getCell(5),
                        student.getCreatedAt() != null
                                ? converterUtil.offsetDateTimeToString(student.getCreatedAt(),
                                        TimeFormat.DD_MM_YYYY_HH_MM_SS)
                                : "",
                        ParagraphAlignment.CENTER);
                setCellText(row.getCell(6),
                        student.getUpdatedAt() != null
                                ? converterUtil.offsetDateTimeToString(student.getUpdatedAt(),
                                        TimeFormat.DD_MM_YYYY_HH_MM_SS)
                                : "",
                        ParagraphAlignment.CENTER);
            }

            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            document.write(outputStream);
            return outputStream.toByteArray();
        } catch (IOException e) {
            throw new InternalServerErrorException("Failed to generate Word document: " +
                    e.getMessage(), e);
        }
    }

    private void setCellText(XWPFTableCell cell, String text, ParagraphAlignment alignment) {
        XWPFParagraph p = cell.getParagraphs().get(0);
        p.setAlignment(alignment);
        XWPFRun run = p.createRun();
        run.setText(text);
        run.setFontSize(10);
    }

}
