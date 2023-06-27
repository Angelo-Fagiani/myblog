package it.cgmconsulting.myblog.service;

import it.cgmconsulting.myblog.payload.response.XlsAuthorResponse;
import it.cgmconsulting.myblog.payload.response.XlsReaderResponse;
import it.cgmconsulting.myblog.repository.UserRepository;
import it.cgmconsulting.myblog.repository.PostRepository;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

@Service
public class XlsService {

    @Autowired UserRepository userRepository;
    @Autowired PostRepository postRepository;

    public InputStream createReport() throws IOException {

        ByteArrayOutputStream out = new ByteArrayOutputStream();

        // creazione xls
        HSSFWorkbook workbook = new HSSFWorkbook();

        // creazione sheet
        createAuthorReport(workbook);
        createReaderReport(workbook);
        //createPostReport(workbook);

        workbook.write(out);
        workbook.close();

        InputStream in = new ByteArrayInputStream(out.toByteArray());

        return in;
    }


    public void createAuthorReport(HSSFWorkbook workBook){

        HSSFSheet sheet = workBook.createSheet("Author Report");
        int rownum=0;
        int column=0;
        Row row;
        Cell cell;

        row = sheet.createRow(rownum);

        // intestazione delle colonne
        String[] labels = {"Id", "username", "Nr. posts written", "Average rate post"};
        for(String s : labels){
            cell = row.createCell(column++, CellType.STRING);
            cell.setCellValue(s);
        }

        List<XlsAuthorResponse> list = userRepository.getXlsAuthorResponse();
        for(XlsAuthorResponse x : list){
            column = 0;
            row = sheet.createRow(++rownum);
            // Id
            cell = row.createCell(column++, CellType.NUMERIC);
            cell.setCellValue(x.getId());
            // Username
            cell = row.createCell(column++, CellType.STRING);
            cell.setCellValue(x.getUsername());
            // Posts Written
            cell = row.createCell(column++, CellType.NUMERIC);
            cell.setCellValue(x.getWrittenPosts());
            // Average
            cell = row.createCell(column++, CellType.NUMERIC);
            cell.setCellValue(x.getAvg());

        }
    }






    public void createPostReport(HSSFWorkbook workBook) {

    }

    public void createReaderReport(HSSFWorkbook workBook) {

        HSSFSheet sheet = workBook.createSheet("Reader Report");
        int rownum=0;
        int column=0;
        Row row;
        Cell cell;

        row = sheet.createRow(rownum);

        // intestazione delle colonne
        String[] labels = {"Id", "username", "Nr. of comments", "Nr. reporting with ban", "Enabled(Y/N)"};
        for(String s : labels){
            cell = row.createCell(column++, CellType.STRING);
            cell.setCellValue(s);
        }

        List<XlsReaderResponse> list = userRepository.getXlsReaderResponse();
        for(XlsReaderResponse x : list) {
            column = 0;
            row = sheet.createRow(++rownum);
            // Id
            cell = row.createCell(column++, CellType.NUMERIC);
            cell.setCellValue(x.getId());
            // Username
            cell = row.createCell(column++, CellType.STRING);
            cell.setCellValue(x.getUsername());
            // Nr. comments written
            cell = row.createCell(column++, CellType.NUMERIC);
            cell.setCellValue(x.getWrittenComments());
            // Nr. reporting with Ban
            cell = row.createCell(column++, CellType.NUMERIC);
            cell.setCellValue(x.getReportingsWithBan());
            // Enabled (Y/N)
            cell = row.createCell(column++, CellType.STRING);
            cell.setCellValue(x.isEnabled() ? 'Y' : 'N');
        }

    }

}
