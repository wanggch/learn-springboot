package jenny.learn.springboot.upload.controller;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.lang.Dict;
import cn.hutool.core.util.StrUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.Part;
import java.io.File;
import java.io.IOException;
import java.util.Date;

@Slf4j
@RestController
@RequestMapping("/file")
public class FileController {

    private static final String FILE_NAME_PATTERN = "{}_{}";

    @RequestMapping("/upload/multipart")
    public Object uploadMultipart(MultipartFile file) {
        // 上传文件的原始文件名
        String originalFilename = file.getOriginalFilename();
        log.info("上传文件的原始文件名：{}", originalFilename);
        // 文件名：日期_原始文件名
        String fileName = StrUtil.format(FILE_NAME_PATTERN, DateUtil.format(new Date(), "yyyyMMddHHmmss"), originalFilename);
        log.info("格式化后的文件名：{}", fileName);
        File dest = new File(fileName);
        try {
            file.transferTo(dest);
        } catch (IOException e) {
            log.info("上传文件失败！", e);
            return result(500, "上传失败！");
        }
        return result(200, "上传成功！");
    }

    @RequestMapping("/upload/part")
    public Object uploadPart(Part file) {
        String originalFilename = file.getSubmittedFileName();
        log.info("上传文件的原始文件名：{}", originalFilename);
        // 文件名：日期_原始文件名
        String fileName = StrUtil.format(FILE_NAME_PATTERN, DateUtil.format(new Date(), "yyyyMMddHHmmss"), originalFilename);
        log.info("格式化后的文件名：{}", fileName);
        try {
            file.write(fileName);
        } catch (IOException e) {
            log.info("上传文件失败！", e);
            return result(500, "上传失败！");
        }
        return result(200, "上传成功！");
    }

    private Dict result(Integer code, String msg) {
        Dict dict = Dict.create();
        dict.set("code", code);
        dict.set("msg", msg);
        return dict;
    }

//    private Dict success() {
//        Dict dict = Dict.create();
//        dict.set("code", 200);
//        dict.set("msg", "上传成功！");
//        return dict;
//    }
//
//    private Dict fail() {
//        Dict dict = Dict.create();
//        dict.set("code", 500);
//        dict.set("msg", "上传失败！");
//        return dict;
//    }
}
