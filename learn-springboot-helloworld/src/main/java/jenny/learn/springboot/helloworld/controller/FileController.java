package jenny.learn.springboot.helloworld.controller;

import cn.hutool.core.io.IoUtil;
import cn.hutool.core.lang.Dict;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.servlet.http.HttpServletRequest;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

@RestController
@RequestMapping("/file")
public class FileController {

    private static final Logger LOGGER = LoggerFactory.getLogger(FileController.class);

    @PostMapping("/upload1")
    public Object upload1(HttpServletRequest request) {
        MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
        MultipartFile multipartFile = multipartRequest.getFile("file");
        String content = null;
        try (InputStream inputStream = multipartFile.getInputStream()) {
            content = IoUtil.read(inputStream, StandardCharsets.UTF_8);
        } catch (IOException e) {
            LOGGER.error("读取文件内容失败，异常信息：", e);
        }
        return Dict.create().set("content", content);
    }

    @PostMapping("/upload2")
    public Object upload2(HttpServletRequest request) {
        MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
        MultipartFile multipartFile = multipartRequest.getFile("file");
        String content = null;

        try {
            // 读取文件内容到InputStream中
            InputStream inputStream = multipartFile.getInputStream();
            // 缓存文件内容到内存中
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            byte[] buffer = new byte[1024];
            int bytesRead;
            while ((bytesRead = inputStream.read(buffer)) != -1) {
                byteArrayOutputStream.write(buffer, 0, bytesRead);
            }
            byteArrayOutputStream.flush();
            // 将内存中的数据转换为字符串
            content = new String(byteArrayOutputStream.toByteArray(), StandardCharsets.UTF_8);
        } catch (IOException e) {
            LOGGER.error("读取文件内容失败，异常信息：", e);
        }

        return Dict.create().set("content", content);
    }
}
