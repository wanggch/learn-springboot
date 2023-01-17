package jenny.learn.springboot.thymeleaf.controller;

import com.lowagie.text.pdf.BaseFont;
import jenny.learn.springboot.thymeleaf.service.UserService;
import org.dom4j.DocumentException;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import org.thymeleaf.templatemode.TemplateMode;
import org.thymeleaf.templateresolver.ClassLoaderTemplateResolver;
import org.xhtmlrenderer.pdf.ITextRenderer;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

@Controller
@RequestMapping("/user")
public class UserController {

    @Resource
    private UserService userService;
    @Resource
    private TemplateEngine templateEngine;

    /**
     * 用户列表
     */
    @RequestMapping("/index")
    public ModelAndView index() {
        ModelAndView mv = new ModelAndView();
        mv.addObject("users", userService.listAllUsers());
        mv.setViewName("user/index");
        return mv;
    }
    @RequestMapping("/pdf")
    public void pdf(HttpServletResponse response) throws Exception {
        String content = parseThymeleafTemplate();
        System.out.println("content: \n" + content);
        this.generatePdfFromHtml(content, response.getOutputStream());
    }

    private String parseThymeleafTemplate() {
//        ClassLoaderTemplateResolver templateResolver = new ClassLoaderTemplateResolver();
//        templateResolver.setPrefix("classpath:/templates/");
//        templateResolver.setSuffix(".html");
//        templateResolver.setTemplateMode(TemplateMode.HTML);
//        templateResolver.setCharacterEncoding("UTF-8");
//
//        TemplateEngine templateEngine = new TemplateEngine();
//        templateEngine.setTemplateResolver(templateResolver);

        Context context = new Context();
        context.setVariable("users", userService.listAllUsers());

        return templateEngine.process("user/index", context);
    }

    public void generatePdfFromHtml(String html, OutputStream outputStream) throws Exception {
//        String outputFolder = System.getProperty("user.home") + File.separator + "thymeleaf.pdf";
//        System.out.println("output folder: " + outputFolder);
//        OutputStream outputStream = new FileOutputStream(outputFolder);

        ITextRenderer renderer = new ITextRenderer();
        renderer.getFontResolver().addFont("c:/Windows/Fonts/simsun.ttc", BaseFont.IDENTITY_H, BaseFont.EMBEDDED);
        renderer.setDocumentFromString(html);
        renderer.layout();
        renderer.createPDF(outputStream);

        outputStream.close();
    }
}
