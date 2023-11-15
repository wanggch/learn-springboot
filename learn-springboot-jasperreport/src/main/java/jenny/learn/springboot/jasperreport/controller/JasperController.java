package jenny.learn.springboot.jasperreport.controller;

import cn.hutool.core.img.ImgUtil;
import cn.hutool.core.io.file.FileReader;
import cn.hutool.core.lang.Dict;
import com.google.common.collect.Lists;
import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;
import jenny.learn.springboot.jasperreport.entity.Student;
import lombok.extern.slf4j.Slf4j;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.export.JRPdfExporter;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Controller;
import org.springframework.util.ResourceUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Type;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Controller
@RequestMapping("/jasper")
public class JasperController {

    @ResponseBody
    @GetMapping("/pdf/hello")
    public void pdf01(HttpServletResponse response) {
        try {
            Map<String, Object> parameter = new HashMap<>();
            parameter.put("data", "Hello World.");
            // 编译
            JasperReport jasperReport = JasperCompileManager.compileReport("C:\\Users\\PRS\\JaspersoftWorkspace\\MyReports\\01-hello.jrxml");
            JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameter, new JREmptyDataSource());
            JasperExportManager.exportReportToPdfStream(jasperPrint, response.getOutputStream());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @ResponseBody
    @GetMapping("/pdf/studentList")
    public void studentList(HttpServletResponse response) {
        try {
            List<Student> students = Lists.newArrayList();
            for(int i = 0; i < 3; i++) {
                Student student = new Student();
                student.setName(String.format("学生-%s", i + 1));
                student.setNum(String.valueOf(i + 1));
                student.setGrade("2023级1班");
                student.setBarcode(String.format("学生-%s", i + 1));
                students.add(student);
            }
            Map<String, Object> parameter = new HashMap<>();
            parameter.put("students", new JRBeanCollectionDataSource(students));
            // 编译
            JasperReport jasperReport = JasperCompileManager.compileReport("C:\\Users\\PRS\\JaspersoftWorkspace\\MyReports\\02-student-list.jrxml");
            JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameter, new JREmptyDataSource());
            JasperExportManager.exportReportToPdfStream(jasperPrint, response.getOutputStream());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @ResponseBody
    @GetMapping("/pdf/barcode")
    public void barcode(HttpServletResponse response) {
        try {
            List<Student> students = Lists.newArrayList();
            for(int i = 0; i < 3; i++) {
                Student student = new Student();
                student.setName(String.format("学生-%s", i + 1));
                student.setNum(String.valueOf(i + 1));
                student.setGrade("2023级1班");
                student.setBarcode(String.format("学生-%s", i + 1));
                students.add(student);
            }
            Map<String, Object> parameter = new HashMap<>();
            parameter.put("students", new JRBeanCollectionDataSource(students));
            // 编译
            JasperReport jasperReport = JasperCompileManager.compileReport("C:\\Users\\PRS\\JaspersoftWorkspace\\MyReports\\03-barcode.jrxml");
            JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameter, new JREmptyDataSource());
            JasperExportManager.exportReportToPdfStream(jasperPrint, response.getOutputStream());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private Map<String, Object> generateTestData() {
        Map<String, Object> paramMap = new HashMap<>();
        paramMap.put("name", "汪小成");
        paramMap.put("gender", "男");
        paramMap.put("birthDay", "2021-01-10");
        paramMap.put("linkManName", "汪大成");
        paramMap.put("linkManPhone", "13800000000");
        paramMap.put("assessTime", "2022-08-06");
        paramMap.put("testMethod", "新生儿20项行为神经测检方法(NBNA)");
        paramMap.put("score", "97");
        paramMap.put("assessStandard", "90.4%的总分在39~40分，97%在37分以上，无1人在35分以下。地区差别对评分结果无明显影响。此评分只话用于足月儿，早产儿需在纠正年龄达到足月后再测查。");
        paramMap.put("guidance", "12-15个月练习独自走路。1-2岁在家长陪伴下练习搭积木，把豆子装入小瓶。1岁-1岁半练习指家中的物品，指鼻子，眼睛，耳朵等身体部位，有意识地拍手欢迎，叫“爸爸\"“妈妈”等家庭成员。给孩子一些东西让他(她)叠高，同时让他(她)学习从盒子里取出和放进东西。问一些简单问题，对孩子尝试学习说话的行为做出积极的应答;让孩子学习说一些简单的日常用语。");
        try {
            String base64 = ImgUtil.toBase64(ImgUtil.getImage(new URL("http://127.0.0.1:8000/child.png")), "png");
            log.info("base64: {}", base64);
            paramMap.put("image", base64);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return paramMap;
    }
}
