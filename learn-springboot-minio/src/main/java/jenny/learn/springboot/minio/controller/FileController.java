package jenny.learn.springboot.minio.controller;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.lang.Dict;
import cn.hutool.core.util.StrUtil;
import io.minio.MinioClient;
import io.minio.PutObjectArgs;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.util.Date;

@Slf4j
@RestController
@RequestMapping("/file")
public class FileController {

    private static final String FILE_NAME_PATTERN = "{}_{}";

    @Resource
    private MinioClient minioClient;

    @RequestMapping("/upload")
    public Object upload(MultipartFile file) {
        // 上传文件的原始文件名
        String originalFilename = file.getOriginalFilename();
        // 文件大小
        long fileSize = file.getSize();
        log.info("上传文件的原始文件名：{}，文件大小：{}", originalFilename, fileSize);
        // 文件名：日期_原始文件名
        String fileName = StrUtil.format(FILE_NAME_PATTERN, DateUtil.format(new Date(), "yyyyMMddHHmmss"), originalFilename);
        log.info("格式化后的文件名：{}", fileName);

        try {
            minioClient.putObject(PutObjectArgs.builder().bucket("my-bucket")
                    .object(fileName).stream(file.getInputStream(), fileSize, -1).contentType(file.getContentType()).build());
        } catch (Exception e) {
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
}
