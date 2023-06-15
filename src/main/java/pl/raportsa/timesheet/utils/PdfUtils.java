package pl.raportsa.timesheet.utils;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import pl.raportsa.timesheet.model.dto.Work;
import pl.raportsa.timesheet.model.dto.WorkDayDto;
import pl.raportsa.timesheet.model.dto.WorkDaySingleDto;
import pl.raportsa.timesheet.model.enums.WorkType;

import java.io.File;
import java.io.FileOutputStream;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;

public class PdfUtils {

    private static final List<String> employeeTableHeaders = List.of("#", "Weekday", "Date", "Sign");
    private static final List<String> employeesConstantTableHeaders = List.of("#", "Weekday", "Date");

    private static Document createDocumentWithTitle(String path, String fileName, LocalDate date, String titlePart) {
        Document document = null;
        try {
            document = new Document(PageSize.A4, 6, 6, 8, 8);
            String reportDateSlash = DateUtils.formatYearMonthWithSlashSeparator(date);

            new File(path).mkdirs();
            PdfWriter.getInstance(document, new FileOutputStream(path + fileName));

            document.open();
            Font font = FontFactory.getFont(FontFactory.COURIER, 10, BaseColor.BLACK);
            Paragraph paragraph1 = new Paragraph("Timesheet for " + titlePart + " in " + reportDateSlash, font);
            paragraph1.setAlignment(Paragraph.ALIGN_CENTER);
            paragraph1.setSpacingAfter(10f);
            document.add(paragraph1);

            return document;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String createForAllEmployees(List<WorkDayDto> workDaysResultModel, List<String> logins, boolean generateByTask) {
        Document document = null;
        String fullPath = null;
        try {
            LocalDate date = workDaysResultModel.get(0).getDate();
            String reportDate = DateUtils.formatYearMonthWithoutSeparators(date);

            String path = FileUtils.CONSTANT_PATH + "PDF\\" + (generateByTask ? "GENERATOR\\" : "") + "EMPLOYEES\\";
            String fileName = "EMPLOYEES_" + reportDate + ".pdf";
            fullPath = path + fileName;

            document = createDocumentWithTitle(path, fileName, date, "Employees");
            document.add(createEmployeesTable(workDaysResultModel, logins));
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            assert document != null;
            document.close();
        }
        return fullPath;
    }

    public static String createEmployee(String employeeLogin, List<WorkDaySingleDto> workDaysResultModel, boolean generateByTask) {
        Document document = null;
        String fullPath = null;
        try {
            LocalDate date = workDaysResultModel.get(0).getDate();
            String reportDate = DateUtils.formatYearMonthWithoutSeparators(date);

            String path = FileUtils.CONSTANT_PATH + "PDF\\" + (generateByTask ? "GENERATOR\\" : "EMPLOYEE\\") + employeeLogin + "\\";
            String fileName = employeeLogin + "_" + reportDate + ".pdf";
            fullPath = path + fileName;

            document = createDocumentWithTitle(path, fileName, workDaysResultModel.get(0).getDate(), employeeLogin);
            document.add(createEmployeeTable(workDaysResultModel));
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            assert document != null;
            document.close();
        }
        return fullPath;
    }


    private static PdfPTable createEmployeesTable(List<WorkDayDto> workDaysResultModel, List<String> logins) {
        PdfPTable table = new PdfPTable(employeesConstantTableHeaders.size() + logins.size());
        table.setWidthPercentage(98);
        addTableHeaderForEmployees(table, logins);
        addRowsEmployees(table, workDaysResultModel, logins);
        return table;
    }

    private static PdfPTable createEmployeeTable(List<WorkDaySingleDto> workDaysResultModel) {
        PdfPTable table = new PdfPTable(4);
        table.setWidthPercentage(98);
       // addTableHeaderForEmployee(table);
        addRowsEmployee(table, workDaysResultModel);
        return table;
    }

    private static void addTableHeaderForEmployees(PdfPTable table, List<String> employees) {
        List<String> headers = new ArrayList<>() {
            {
                addAll(employeesConstantTableHeaders);
                addAll(employees);
            }
        };

        headers.forEach(columnTitle -> {
            PdfPCell header = new PdfPCell();
            header.setPhrase(new Phrase(columnTitle));
            table.addCell(header);
        });
    }

    private static void addTableHeaderForEmployee(PdfPTable table) {
        employeeTableHeaders
                .forEach(columnTitle -> {
                    PdfPCell header = new PdfPCell();
                    header.setPhrase(new Phrase(columnTitle));
                    table.addCell(header);
                });
    }

    private static void addRowsEmployees(PdfPTable table, List<WorkDayDto> workDaysResultModel, List<String> logins) {
        IntStream.range(0, workDaysResultModel.size())
                .forEach(i -> {
                    WorkDayDto w = workDaysResultModel.get(i);
                    table.addCell(String.format("%d", i + 1));
                    table.addCell(w.getWeekdayName());
                    table.addCell(DateUtils.formatWithSeparators(w.getDate()));
                    logins.forEach(l -> table.addCell(getSignCell(w.isHoliday(), w.getWork(l))));
                });
    }

    private static void addRowsEmployee(PdfPTable table, List<WorkDaySingleDto> workDaysResultModel) {
        IntStream.range(0, workDaysResultModel.size())
                .forEach(i -> {
                    WorkDaySingleDto w = workDaysResultModel.get(i);
                    table.addCell(String.format("%d", i + 1));
                    table.addCell(w.getWeekdayName());
                    table.addCell(DateUtils.formatWithSeparators(w.getDate()));
                    table.addCell(getSignCell(w.isHoliday(), w.getWork()));
                });
    }

    private static PdfPCell getSignCell(boolean isHoliday, Work w) {
        PdfPCell cell = createCellByType(w);
        cell.setBackgroundColor(isHoliday ? BaseColor.GRAY :  BaseColor.WHITE);
        return cell;
    }

    private static PdfPCell createCellByType(Work work) {
        if (work == null) return new PdfPCell();

        if (work.getWorkType() == WorkType.L4) {
            return createImageCell("l4.png", 20, false);
        } else if (work.getWorkType() == WorkType.VACATION) {
            return createImageCell("vacation.png", 20, false);
        } else if (work.getWorkType() == WorkType.SIGN) {
            return createImageCell(work.getSignSrc(), 6, work.isSignError());
        }
        return new PdfPCell();
    }

    private static PdfPCell createImageCell(String path, int scale, boolean signError) {

        PdfPCell pdfPCell = new PdfPCell();
        pdfPCell.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);
        pdfPCell.setVerticalAlignment(PdfPCell.ALIGN_CENTER);

        pdfPCell.addElement(createSignParagraph(loadImage(FileUtils.CONSTANT_PATH + path, scale), signError));

        return pdfPCell;
    }

    private static Image exclamationImg() {
        return loadImage(FileUtils.CONSTANT_PATH + "exclamation.png", 50);
    }

    private static Paragraph createSignParagraph(Image sign, boolean signError){
        Paragraph paragraph = new Paragraph();
        paragraph.setAlignment(Element.ALIGN_CENTER);
        paragraph.add(new Chunk(sign, 0, 0, true));
        if(signError) paragraph.add(new Chunk(exclamationImg(), 0, 10,true));

        return paragraph;
    }

    private static Image loadImage(String paths, int scale) {
        try {
            Image img = Image.getInstance(paths);
            img.scalePercent(scale);
            return img;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
