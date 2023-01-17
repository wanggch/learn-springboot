package jenny.learn.springboot.excel.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import jenny.learn.springboot.excel.dao.BookDao;
import jenny.learn.springboot.excel.entity.Book;
import jenny.learn.springboot.excel.support.ExcelImporter;
import jenny.learn.springboot.excel.util.Id;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

@Service
public class BookService extends ServiceImpl<BookDao, Book> implements ExcelImporter<Book> {
    @Resource
    private BookDao bookDao;
    /** 添加书籍 */
    public Book add(Book book) {
        book.setId(Id.next());
        book.setCreateBy("wanggc");
        book.setCreateTime(new Date());
        bookDao.insert(book);
        return book;
    }
    /** 获取所有书籍 */
    public List<Book> all() {
        QueryWrapper<Book> queryWrapper = new QueryWrapper<>();
        return bookDao.selectList(queryWrapper);
    }

    @Override
    public void save(List<Book> data) {
        this.saveBatch(data);
    }
}
