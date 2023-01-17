package jenny.learn.springboot.excel.controller;

import com.alibaba.excel.EasyExcel;
import jenny.learn.springboot.excel.entity.Book;
import jenny.learn.springboot.excel.listener.DataListener;
import jenny.learn.springboot.excel.listener.ImportListener;
import jenny.learn.springboot.excel.service.BookService;
import org.apache.poi.ss.formula.functions.T;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.net.URLEncoder;
import java.util.List;
import java.util.Objects;

@RestController
@RequestMapping("/book")
public class BookController {
    @Resource
    private BookService bookService;

    @RequestMapping("/all")
    public List<Book> all() {
        return bookService.all();
    }

    @RequestMapping("/export")
    public void exportData(HttpServletResponse response) throws Exception {
        // 这里注意 有同学反应使用swagger 会导致各种问题，请直接用浏览器或者用postman
        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        response.setCharacterEncoding("utf-8");
        // 这里URLEncoder.encode可以防止中文乱码 当然和easyexcel没有关系
        String fileName = URLEncoder.encode("书籍", "UTF-8").replaceAll("\\+", "%20");
        response.setHeader("Content-disposition", "attachment;filename*=utf-8''" + fileName + ".xlsx");

        List<Book> books = bookService.all();
        EasyExcel.write(response.getOutputStream(), Book.class).sheet("书籍").doWrite(books);
    }

    @RequestMapping("/import")
    public void importData(MultipartFile file) throws Exception {
        ImportListener<Book> importListener = new ImportListener<>(bookService);
        EasyExcel.read(file.getInputStream(), Book.class, importListener).sheet().doRead();
    }
}
