package jenny.learn.springboot.jasperreport.controller;

import cn.hutool.core.img.ImgUtil;
import cn.hutool.core.io.file.FileReader;
import cn.hutool.core.lang.Dict;
import cn.hutool.core.util.StrUtil;
import com.google.common.collect.Lists;
import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import lombok.extern.slf4j.Slf4j;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.export.HtmlExporter;
import net.sf.jasperreports.engine.export.JRPdfExporter;
import net.sf.jasperreports.export.SimpleExporterInput;
import net.sf.jasperreports.export.SimpleHtmlExporterOutput;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.util.ResourceUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.net.URL;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Controller
@RequestMapping("/examination")
public class ExaminationController {

    @ResponseBody
    @GetMapping("/report")
    public ResponseEntity<byte[]> report() {
        try {

            // 报表需要的动态参数
            Map<String, Object> paramMap = generateTestData();
            // 编译
            JasperReport jasperReport = JasperCompileManager.compileReport(
                    ResourceUtils.getFile("classpath:NBNA_2.jrxml").getAbsolutePath()
            );

            // 填充数据
            JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, paramMap, new JREmptyDataSource());

            // 导出报表
            HttpHeaders headers = new HttpHeaders();
            // 设置响应格式：PDF
            headers.setContentType(MediaType.APPLICATION_PDF);
            // 设置文件名称
            headers.setContentDispositionFormData("attachment", java.net.URLEncoder.encode("新生儿20项行为神经测检方法(NBNA).pdf", "UTF-8"));
            return new ResponseEntity<>(JasperExportManager.exportReportToPdf(jasperPrint), headers, HttpStatus.OK);

        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @ResponseBody
    @GetMapping("/pdf")
    public void pdf(HttpServletResponse response) {
        try {

            // 报表需要的动态参数
            Map<String, Object> paramMap = generateTestData();
            // 编译
            JasperReport jasperReport = JasperCompileManager.compileReport(
                    ResourceUtils.getFile("classpath:NBNA_2.jrxml").getAbsolutePath()
            );
//            JasperReport report = (JasperReport) JRLoader.loadObject(reportFilePath); // 加载.jasper文件

            List<JasperPrint> list = new ArrayList<>();
            for(int i = 0; i < 5; i++) {
                // 填充数据
                paramMap.put("name", new StringBuffer("汪小成#").append(i).toString());
                JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, paramMap, new JREmptyDataSource());
                list.add(jasperPrint);
            }

//            JasperExportManager.exportReportToPdfStream(jasperPrint, response.getOutputStream());

            JRAbstractExporter exporter = new JRPdfExporter();
            // 设置前台编译格式
            ServletOutputStream baos = response.getOutputStream();
            exporter.setParameter(JRExporterParameter.JASPER_PRINT_LIST, list);
            exporter.setParameter(JRExporterParameter.OUTPUT_STREAM, baos);
            exporter.setParameter(JRExporterParameter.CHARACTER_ENCODING, "UTF-8");
            exporter.exportReport();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @ResponseBody
    @GetMapping("/test")
    public void test(HttpServletResponse response) {
        try {
            FileReader reader = new FileReader("D:\\13-data\\organization_statistics_report.json");
            String content = reader.readString();
            log.info("content: {}", content);
            Type type = new TypeToken<Map<String, Object>>() {}.getType();
            Map<String, Object> data = new Gson().fromJson(content, type);

            Map<String, Map<String, Object>> parameter = new HashMap<>();
            parameter.put("data", data);
            // 编译
//            JasperReport jasperReport = JasperCompileManager.compileReport("C:\\Users\\PRS\\JaspersoftWorkspace\\MyReports\\organization_statistics_report.jrxml");
            ClassPathResource resource = new ClassPathResource("organization_statistics_report.jrxml");
            JasperReport jasperReport = JasperCompileManager.compileReport(resource.getInputStream());
            JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, data, new JREmptyDataSource());

            JasperExportManager.exportReportToPdfStream(jasperPrint, response.getOutputStream());

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @ResponseBody
    @GetMapping("/map")
    public void map(HttpServletResponse response) {
        try {
//            FileReader reader = new FileReader("D:\\13-data\\organization_statistics_report.json");
//            String content = reader.readString();
//            log.info("content: {}", content);
//            Type type = new TypeToken<Map<String, Object>>() {}.getType();
//            Map<String, Object> data = new Gson().fromJson(content, type);

            Map<String, Object> paramMap = new HashMap<>();

            Map<String, Object> base = new HashMap<>();
            base.put("year", "2022");
            base.put("organization", "普瑞森集团医院");

            // 前言
            FileReader reader = new FileReader("D:\\13-data\\organization\\Preface.txt");
            base.put("preface", reader.readString());
            // 基本进展情况
            reader = new FileReader("D:\\13-data\\organization\\Progress.txt");
            base.put("progress", reader.readString());

            Map<String, Object> peopleRate = new HashMap<>();
            peopleRate.put("sum6569", "36");
            peopleRate.put("sum7074", "27");
            peopleRate.put("sum7579", "18");
            peopleRate.put("sum8084", "10");
            peopleRate.put("sum85+", "7");
            peopleRate.put("man6569", "17");
            peopleRate.put("man7074", "13");
            peopleRate.put("man7579", "9");
            peopleRate.put("man8084", "5");
            peopleRate.put("man85+", "2");
            peopleRate.put("woman6569", "18");
            peopleRate.put("woman7074", "13");
            peopleRate.put("woman7579", "9");
            peopleRate.put("woman8084", "5");
            peopleRate.put("woman85+", "4");
            paramMap.put("peopleRate", peopleRate);

            Map<String, Object> peopleNumber = new HashMap<>();
            peopleNumber.put("sum6569", "2587");
            peopleNumber.put("sum7074", "1921");
            peopleNumber.put("sum7579", "1328");
            peopleNumber.put("sum8084", "772");
            peopleNumber.put("sum85+", "498");
            peopleNumber.put("man6569", "1257");
            peopleNumber.put("man7074", "928");
            peopleNumber.put("man7579", "646");
            peopleNumber.put("man8084", "376");
            peopleNumber.put("man85+", "197");
            peopleNumber.put("woman6569", "1330");
            peopleNumber.put("woman7074", "993");
            peopleNumber.put("woman7579", "682");
            peopleNumber.put("woman8084", "396");
            peopleNumber.put("woman85+", "301");
            paramMap.put("peopleNumber", peopleNumber);

            List<Dict> peopleLineChartData = new ArrayList<>();
            peopleLineChartData.add(Dict.create().set("type", "男性").set("name", "65-69岁").set("value", 1257));
            peopleLineChartData.add(Dict.create().set("type", "男性").set("name", "70-74岁").set("value", 928));
            peopleLineChartData.add(Dict.create().set("type", "男性").set("name", "75-79岁").set("value", 646));
            peopleLineChartData.add(Dict.create().set("type", "男性").set("name", "80-84岁").set("value", 376));
            peopleLineChartData.add(Dict.create().set("type", "男性").set("name", "85岁以上").set("value", 197));
            peopleLineChartData.add(Dict.create().set("type", "女性").set("name", "65-69岁").set("value", 1330));
            peopleLineChartData.add(Dict.create().set("type", "女性").set("name", "70-74岁").set("value", 993));
            peopleLineChartData.add(Dict.create().set("type", "女性").set("name", "75-79岁").set("value", 682));
            peopleLineChartData.add(Dict.create().set("type", "女性").set("name", "80-84岁").set("value", 396));
            peopleLineChartData.add(Dict.create().set("type", "女性").set("name", "85岁以上").set("value", 301));
            peopleLineChartData.add(Dict.create().set("type", "合计").set("name", "65-69岁").set("value", 2587));
            peopleLineChartData.add(Dict.create().set("type", "合计").set("name", "70-74岁").set("value", 1921));
            peopleLineChartData.add(Dict.create().set("type", "合计").set("name", "75-79岁").set("value", 1328));
            peopleLineChartData.add(Dict.create().set("type", "合计").set("name", "80-84岁").set("value", 772));
            peopleLineChartData.add(Dict.create().set("type", "合计").set("name", "85岁以上").set("value", 498));
            paramMap.put("peopleLineChartData", peopleLineChartData);

            List<Dict> peoplePieChartData = new ArrayList<>();
            peoplePieChartData.add(Dict.create().set("type", "65-69岁").set("value", 5174));
            peoplePieChartData.add(Dict.create().set("type", "70-74岁").set("value", 3842));
            peoplePieChartData.add(Dict.create().set("type", "75-79岁").set("value", 2656));
            peoplePieChartData.add(Dict.create().set("type", "80-84岁").set("value", 1544));
            peoplePieChartData.add(Dict.create().set("type", "85岁以上").set("value", 996));
            paramMap.put("peoplePieChartData", peoplePieChartData);

            // 体检异常分析：使用 LinkedHashMap 是为了 keySet() 取出的值有序
            Map<String, Object> exceptionData = new LinkedHashMap<>();
            exceptionData.put("2020", Lists.newArrayList(3279, 3190, 1023, 1623, 1231, 1843, 3409, 2111, 1875, 5816));
            exceptionData.put("2021", Lists.newArrayList(3279, 3191, 1022, 1622, 1232, 1843, 3409, 2111, 1875, 5816));
            exceptionData.put("2022", Lists.newArrayList(3262, 3186, 1023, 1511, 1231, 1846, 3408, 2109, 1874, 5818));
            paramMap.put("exceptionData", exceptionData);

            List<Dict> exceptionLineChartData = new ArrayList<>();
            exceptionLineChartData.add(Dict.create().set("type", "2020").set("name", "超重").set("value", 3279));
            exceptionLineChartData.add(Dict.create().set("type", "2021").set("name", "超重").set("value", 3279));
            exceptionLineChartData.add(Dict.create().set("type", "2022").set("name", "超重").set("value", 3262));
            exceptionLineChartData.add(Dict.create().set("type", "2020").set("name", "血压异常").set("value", 3190));
            exceptionLineChartData.add(Dict.create().set("type", "2021").set("name", "血压异常").set("value", 3191));
            exceptionLineChartData.add(Dict.create().set("type", "2022").set("name", "血压异常").set("value", 3186));
            exceptionLineChartData.add(Dict.create().set("type", "2020").set("name", "血常规异常").set("value", 1023));
            exceptionLineChartData.add(Dict.create().set("type", "2021").set("name", "血常规异常").set("value", 1022));
            exceptionLineChartData.add(Dict.create().set("type", "2022").set("name", "血常规异常").set("value", 1023));
            exceptionLineChartData.add(Dict.create().set("type", "2020").set("name", "血糖异常").set("value", 1623));
            exceptionLineChartData.add(Dict.create().set("type", "2021").set("name", "血糖异常").set("value", 1622));
            exceptionLineChartData.add(Dict.create().set("type", "2022").set("name", "血糖异常").set("value", 1511));
            exceptionLineChartData.add(Dict.create().set("type", "2020").set("name", "肝功能异常").set("value", 1231));
            exceptionLineChartData.add(Dict.create().set("type", "2021").set("name", "肝功能异常").set("value", 1232));
            exceptionLineChartData.add(Dict.create().set("type", "2022").set("name", "肝功能异常").set("value", 1231));
            exceptionLineChartData.add(Dict.create().set("type", "2020").set("name", "肾功能异常").set("value", 1843));
            exceptionLineChartData.add(Dict.create().set("type", "2021").set("name", "肾功能异常").set("value", 1846));
            exceptionLineChartData.add(Dict.create().set("type", "2022").set("name", "肾功能异常").set("value", 1843));
            exceptionLineChartData.add(Dict.create().set("type", "2020").set("name", "血脂异常").set("value", 3409));
            exceptionLineChartData.add(Dict.create().set("type", "2021").set("name", "血脂异常").set("value", 3408));
            exceptionLineChartData.add(Dict.create().set("type", "2022").set("name", "血脂异常").set("value", 3409));
            exceptionLineChartData.add(Dict.create().set("type", "2020").set("name", "尿常规异常").set("value", 2111));
            exceptionLineChartData.add(Dict.create().set("type", "2021").set("name", "尿常规异常").set("value", 2108));
            exceptionLineChartData.add(Dict.create().set("type", "2022").set("name", "尿常规异常").set("value", 2109));
            exceptionLineChartData.add(Dict.create().set("type", "2020").set("name", "B超异常").set("value", 1875));
            exceptionLineChartData.add(Dict.create().set("type", "2021").set("name", "B超异常").set("value", 1874));
            exceptionLineChartData.add(Dict.create().set("type", "2022").set("name", "B超异常").set("value", 1874));
            exceptionLineChartData.add(Dict.create().set("type", "2020").set("name", "心电异常").set("value", 5816));
            exceptionLineChartData.add(Dict.create().set("type", "2021").set("name", "心电异常").set("value", 5817));
            exceptionLineChartData.add(Dict.create().set("type", "2022").set("name", "心电异常").set("value", 5818));
            paramMap.put("exceptionLineChartData", exceptionLineChartData);



            List<Dict> exceptionOnePieChartData = exceptionLineChartData.stream().filter(
                    item -> Objects.equals("2020", item.getStr("type"))).collect(Collectors.toList()
            );
            paramMap.put("exceptionOnePieChartData", exceptionOnePieChartData);
            List<Dict> exceptionTwoPieChartData = exceptionLineChartData.stream().filter(
                    item -> Objects.equals("2021", item.getStr("type"))).collect(Collectors.toList()
            );
            paramMap.put("exceptionTwoPieChartData", exceptionTwoPieChartData);
            List<Dict> exceptionThreePieChartData = exceptionLineChartData.stream().filter(
                    item -> Objects.equals("2022", item.getStr("type"))).collect(Collectors.toList()
            );
            paramMap.put("exceptionThreePieChartData", exceptionThreePieChartData);

            // 健康评估与建议（异常）
            reader = new FileReader("D:\\13-data\\organization\\Exception.txt");
            base.put("exception", reader.readString());

            // 体检异常分析：使用 LinkedHashMap 是为了 keySet() 取出的值有序
//            List<Dict> bmiExceptionData = new ArrayList<>();

//            bmiExceptionData.add(Dict.create().set("age", "6569").set("year", "2020").set("type", "normal").set("value", 1257));
//            bmiExceptionData.add(Dict.create().set("age", "6569").set("year", "2020").set("type", "overweight").set("value", 937));
//            bmiExceptionData.add(Dict.create().set("age", "6569").set("year", "2020").set("type", "fat").set("value", 393));
//            bmiExceptionData.add(Dict.create().set("age", "6569").set("year", "2021").set("type", "normal").set("value", 1257));
//            bmiExceptionData.add(Dict.create().set("age", "6569").set("year", "2021").set("type", "overweight").set("value", 937));
//            bmiExceptionData.add(Dict.create().set("age", "6569").set("year", "2021").set("type", "fat").set("value", 393));
//            bmiExceptionData.add(Dict.create().set("age", "6569").set("year", "2022").set("type", "normal").set("value", 1257));
//            bmiExceptionData.add(Dict.create().set("age", "6569").set("year", "2022").set("type", "overweight").set("value", 941));
//            bmiExceptionData.add(Dict.create().set("age", "6569").set("year", "2022").set("type", "fat").set("value", 389));
//
//            bmiExceptionData.add(Dict.create().set("age", "7074").set("year", "2020").set("type", "normal").set("value", 1018));
//            bmiExceptionData.add(Dict.create().set("age", "7074").set("year", "2020").set("type", "overweight").set("value", 605));
//            bmiExceptionData.add(Dict.create().set("age", "7074").set("year", "2020").set("type", "fat").set("value", 389));
//            bmiExceptionData.add(Dict.create().set("age", "7074").set("year", "2021").set("type", "normal").set("value", 1018));
//            bmiExceptionData.add(Dict.create().set("age", "7074").set("year", "2021").set("type", "overweight").set("value", 606));
//            bmiExceptionData.add(Dict.create().set("age", "7074").set("year", "2021").set("type", "fat").set("value", 297));
//            bmiExceptionData.add(Dict.create().set("age", "7074").set("year", "2022").set("type", "normal").set("value", 1020));
//            bmiExceptionData.add(Dict.create().set("age", "7074").set("year", "2022").set("type", "overweight").set("value", 607));
//            bmiExceptionData.add(Dict.create().set("age", "7074").set("year", "2022").set("type", "fat").set("value", 294));
//
//            bmiExceptionData.add(Dict.create().set("age", "7579").set("year", "2020").set("type", "normal").set("value", 769));
//            bmiExceptionData.add(Dict.create().set("age", "7579").set("year", "2020").set("type", "overweight").set("value", 382));
//            bmiExceptionData.add(Dict.create().set("age", "7579").set("year", "2020").set("type", "fat").set("value", 177));
//            bmiExceptionData.add(Dict.create().set("age", "7579").set("year", "2021").set("type", "normal").set("value", 769));
//            bmiExceptionData.add(Dict.create().set("age", "7579").set("year", "2021").set("type", "overweight").set("value", 382));
//            bmiExceptionData.add(Dict.create().set("age", "7579").set("year", "2021").set("type", "fat").set("value", 177));
//            bmiExceptionData.add(Dict.create().set("age", "7579").set("year", "2022").set("type", "normal").set("value", 776));
//            bmiExceptionData.add(Dict.create().set("age", "7579").set("year", "2022").set("type", "overweight").set("value", 380));
//            bmiExceptionData.add(Dict.create().set("age", "7579").set("year", "2022").set("type", "fat").set("value", 172));
//
//            bmiExceptionData.add(Dict.create().set("age", "8084").set("year", "2020").set("type", "normal").set("value", 769));
//            bmiExceptionData.add(Dict.create().set("age", "8084").set("year", "2020").set("type", "overweight").set("value", 382));
//            bmiExceptionData.add(Dict.create().set("age", "8084").set("year", "2020").set("type", "fat").set("value", 177));
//            bmiExceptionData.add(Dict.create().set("age", "8084").set("year", "2021").set("type", "normal").set("value", 769));
//            bmiExceptionData.add(Dict.create().set("age", "8084").set("year", "2021").set("type", "overweight").set("value", 382));
//            bmiExceptionData.add(Dict.create().set("age", "8084").set("year", "2021").set("type", "fat").set("value", 177));
//            bmiExceptionData.add(Dict.create().set("age", "8084").set("year", "2022").set("type", "normal").set("value", 776));
//            bmiExceptionData.add(Dict.create().set("age", "8084").set("year", "2022").set("type", "overweight").set("value", 380));
//            bmiExceptionData.add(Dict.create().set("age", "8084").set("year", "2022").set("type", "fat").set("value", 172));
//
//            bmiExceptionData.add(Dict.create().set("age", "85+").set("year", "2020").set("type", "normal").set("value", 769));
//            bmiExceptionData.add(Dict.create().set("age", "85+").set("year", "2020").set("type", "overweight").set("value", 382));
//            bmiExceptionData.add(Dict.create().set("age", "85+").set("year", "2020").set("type", "fat").set("value", 177));
//            bmiExceptionData.add(Dict.create().set("age", "85+").set("year", "2021").set("type", "normal").set("value", 769));
//            bmiExceptionData.add(Dict.create().set("age", "85+").set("year", "2021").set("type", "overweight").set("value", 382));
//            bmiExceptionData.add(Dict.create().set("age", "85+").set("year", "2021").set("type", "fat").set("value", 177));
//            bmiExceptionData.add(Dict.create().set("age", "85+").set("year", "2022").set("type", "normal").set("value", 776));
//            bmiExceptionData.add(Dict.create().set("age", "85+").set("year", "2022").set("type", "overweight").set("value", 380));
//            bmiExceptionData.add(Dict.create().set("age", "85+").set("year", "2022").set("type", "fat").set("value", 172));


            Map<String, Object> bmiExceptionData = new LinkedHashMap<>();
            bmiExceptionData.put("year", Lists.newArrayList(2020, 2021, 2022, 2020, 2021, 2022, 2020, 2021, 2022, 2020, 2021, 2022, 2020, 2021, 2022));
            bmiExceptionData.put("normal", Lists.newArrayList(1257, 1257, 1257, 1018, 1018, 1020, 769, 769, 776, 465, 465, 469, 316, 316, 320));
            bmiExceptionData.put("overweight", Lists.newArrayList(937, 937, 941, 605, 606, 607, 382, 382, 380, 211, 211, 210, 119, 119, 119));
            bmiExceptionData.put("fat", Lists.newArrayList(393, 393, 389, 298, 297, 294, 177, 177, 172, 96, 96, 93, 63, 63, 59));
            paramMap.put("bmiExceptionData", bmiExceptionData);

            List<Dict> bmiExceptionLineChartData = new ArrayList<>();
            bmiExceptionLineChartData.add(Dict.create().set("type", "2020").set("name", "正常").set("value", 3825));
            bmiExceptionLineChartData.add(Dict.create().set("type", "2021").set("name", "正常").set("value", 3825));
            bmiExceptionLineChartData.add(Dict.create().set("type", "2022").set("name", "正常").set("value", 3825));
            bmiExceptionLineChartData.add(Dict.create().set("type", "2020").set("name", "超重").set("value", 2254));
            bmiExceptionLineChartData.add(Dict.create().set("type", "2021").set("name", "超重").set("value", 2255));
            bmiExceptionLineChartData.add(Dict.create().set("type", "2022").set("name", "超重").set("value", 2257));
            bmiExceptionLineChartData.add(Dict.create().set("type", "2020").set("name", "肥胖").set("value", 1027));
            bmiExceptionLineChartData.add(Dict.create().set("type", "2021").set("name", "肥胖").set("value", 1026));
            bmiExceptionLineChartData.add(Dict.create().set("type", "2022").set("name", "肥胖").set("value", 1027));
            paramMap.put("bmiExceptionLineChartData", bmiExceptionLineChartData);

            List<Dict> bmiExceptionOnePieChartData = bmiExceptionLineChartData.stream().filter(
                    item -> Objects.equals("2020", item.getStr("type"))).collect(Collectors.toList()
            );
            paramMap.put("bmiExceptionOnePieChartData", bmiExceptionOnePieChartData);

            List<Dict> bmiExceptionTwoPieChartData = bmiExceptionLineChartData.stream().filter(
                    item -> Objects.equals("2021", item.getStr("type"))).collect(Collectors.toList()
            );
            paramMap.put("bmiExceptionTwoPieChartData", bmiExceptionTwoPieChartData);

            List<Dict> bmiExceptionThreePieChartData = bmiExceptionLineChartData.stream().filter(
                    item -> Objects.equals("2022", item.getStr("type"))).collect(Collectors.toList()
            );
            paramMap.put("bmiExceptionThreePieChartData", bmiExceptionThreePieChartData);


            // 健康评估与建议（BMI）
            reader = new FileReader("D:\\13-data\\organization\\bmi.txt");
            base.put("bmi", reader.readString());

            Map<String, Object> bloodPresureExceptionData = new LinkedHashMap<>();
            bloodPresureExceptionData.put("year", Lists.newArrayList(2020, 2021, 2022, 2020, 2021, 2022, 2020, 2021, 2022, 2020, 2021, 2022, 2020, 2021, 2022));
            bloodPresureExceptionData.put("normal", Lists.newArrayList(1120, 1118, 1120, 739, 740, 739, 454, 454, 454, 220, 220, 220, 276, 126, 126));
            bloodPresureExceptionData.put("one", Lists.newArrayList(980, 981, 981, 764, 764, 764, 519, 519, 519, 302, 302, 302, 14, 164, 164));
            bloodPresureExceptionData.put("two", Lists.newArrayList(397, 397, 397, 322, 322, 322, 279, 279, 279, 185, 185, 185, 144, 144, 144));
            bloodPresureExceptionData.put("three", Lists.newArrayList(90, 91, 89, 96, 95, 96, 76, 76, 76, 65, 65, 65, 64, 64, 64));
            paramMap.put("bloodPresureExceptionData", bloodPresureExceptionData);

            List<Dict> bloodPresureExceptionLineChartData = new ArrayList<>();
            bloodPresureExceptionLineChartData.add(Dict.create().set("type", "2020").set("name", "正常").set("value", 2809));
            bloodPresureExceptionLineChartData.add(Dict.create().set("type", "2021").set("name", "正常").set("value", 2658));
            bloodPresureExceptionLineChartData.add(Dict.create().set("type", "2022").set("name", "正常").set("value", 2659));
            bloodPresureExceptionLineChartData.add(Dict.create().set("type", "2020").set("name", "一级高血压").set("value", 2579));
            bloodPresureExceptionLineChartData.add(Dict.create().set("type", "2021").set("name", "一级高血压").set("value", 2730));
            bloodPresureExceptionLineChartData.add(Dict.create().set("type", "2022").set("name", "一级高血压").set("value", 2730));
            bloodPresureExceptionLineChartData.add(Dict.create().set("type", "2020").set("name", "二级高血压").set("value", 1327));
            bloodPresureExceptionLineChartData.add(Dict.create().set("type", "2021").set("name", "二级高血压").set("value", 1327));
            bloodPresureExceptionLineChartData.add(Dict.create().set("type", "2022").set("name", "二级高血压").set("value", 1327));
            bloodPresureExceptionLineChartData.add(Dict.create().set("type", "2020").set("name", "三级高血压").set("value", 391));
            bloodPresureExceptionLineChartData.add(Dict.create().set("type", "2021").set("name", "三级高血压").set("value", 390));
            bloodPresureExceptionLineChartData.add(Dict.create().set("type", "2022").set("name", "三级高血压").set("value", 391));
            paramMap.put("bloodPresureExceptionLineChartData", bloodPresureExceptionLineChartData);

            List<Dict> bloodPresureExceptionOnePieChartData = bloodPresureExceptionLineChartData.stream().filter(
                    item -> Objects.equals("2020", item.getStr("type"))).collect(Collectors.toList()
            );
            paramMap.put("bloodPresureExceptionOnePieChartData", bloodPresureExceptionOnePieChartData);

            List<Dict> bloodPresureExceptionTwoPieChartData = bloodPresureExceptionLineChartData.stream().filter(
                    item -> Objects.equals("2021", item.getStr("type"))).collect(Collectors.toList()
            );
            paramMap.put("bloodPresureExceptionTwoPieChartData", bloodPresureExceptionTwoPieChartData);

            List<Dict> bloodPresureExceptionThreePieChartData = bloodPresureExceptionLineChartData.stream().filter(
                    item -> Objects.equals("2022", item.getStr("type"))).collect(Collectors.toList()
            );
            paramMap.put("bloodPresureExceptionThreePieChartData", bloodPresureExceptionThreePieChartData);
            // 健康评估与建议（血压）
            reader = new FileReader("D:\\13-data\\organization\\bloodPresure.txt");
            base.put("bloodPresure", reader.readString());

            Map<String, Object> bloodRoutineExceptionData = new LinkedHashMap<>();
            bloodRoutineExceptionData.put("year", Lists.newArrayList(2020, 2021, 2022, 2020, 2021, 2022, 2020, 2021, 2022, 2020, 2021, 2022, 2020, 2021, 2022));
            bloodRoutineExceptionData.put("whiteBlood", Lists.newArrayList(134, 133, 133, 110, 110, 110, 77, 77, 77, 50, 50, 50, 29, 29, 29));
            bloodRoutineExceptionData.put("bloodCells", Lists.newArrayList(90, 90, 91, 68, 68, 68, 66, 66, 66, 49, 49, 49, 32, 32, 32));
            bloodRoutineExceptionData.put("hemoglobin", Lists.newArrayList(90, 90, 90, 113, 113, 113, 117, 117, 117, 87, 87, 87, 82, 82, 82));
            bloodRoutineExceptionData.put("redBlood", Lists.newArrayList(0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0));
            paramMap.put("bloodRoutineExceptionData", bloodRoutineExceptionData);

            List<Dict> bloodRoutineExceptionLineChartData = new ArrayList<>();
            bloodRoutineExceptionLineChartData.add(Dict.create().set("type", "2020").set("name", "白细胞").set("value", 400));
            bloodRoutineExceptionLineChartData.add(Dict.create().set("type", "2021").set("name", "白细胞").set("value", 399));
            bloodRoutineExceptionLineChartData.add(Dict.create().set("type", "2022").set("name", "白细胞").set("value", 399));
            bloodRoutineExceptionLineChartData.add(Dict.create().set("type", "2020").set("name", "血小板").set("value", 305));
            bloodRoutineExceptionLineChartData.add(Dict.create().set("type", "2021").set("name", "血小板").set("value", 305));
            bloodRoutineExceptionLineChartData.add(Dict.create().set("type", "2022").set("name", "血小板").set("value", 306));
            bloodRoutineExceptionLineChartData.add(Dict.create().set("type", "2020").set("name", "血红蛋白").set("value", 489));
            bloodRoutineExceptionLineChartData.add(Dict.create().set("type", "2021").set("name", "血红蛋白").set("value", 489));
            bloodRoutineExceptionLineChartData.add(Dict.create().set("type", "2022").set("name", "血红蛋白").set("value", 489));
            bloodRoutineExceptionLineChartData.add(Dict.create().set("type", "2020").set("name", "红细胞").set("value", 0));
            bloodRoutineExceptionLineChartData.add(Dict.create().set("type", "2021").set("name", "红细胞").set("value", 0));
            bloodRoutineExceptionLineChartData.add(Dict.create().set("type", "2022").set("name", "红细胞").set("value", 0));
            paramMap.put("bloodRoutineExceptionLineChartData", bloodRoutineExceptionLineChartData);

            List<Dict> bloodRoutineExceptionOnePieChartData = bloodRoutineExceptionLineChartData.stream().filter(
                    item -> Objects.equals("2020", item.getStr("type"))).collect(Collectors.toList()
            );
            paramMap.put("bloodRoutineExceptionOnePieChartData", bloodRoutineExceptionOnePieChartData);

            List<Dict> bloodRoutineExceptionTwoPieChartData = bloodRoutineExceptionLineChartData.stream().filter(
                    item -> Objects.equals("2021", item.getStr("type"))).collect(Collectors.toList()
            );
            paramMap.put("bloodRoutineExceptionTwoPieChartData", bloodRoutineExceptionTwoPieChartData);

            List<Dict> bloodRoutineExceptionThreePieChartData = bloodRoutineExceptionLineChartData.stream().filter(
                    item -> Objects.equals("2022", item.getStr("type"))).collect(Collectors.toList()
            );
            paramMap.put("bloodRoutineExceptionThreePieChartData", bloodRoutineExceptionThreePieChartData);

            // 健康评估与建议（血常规）
            reader = new FileReader("D:\\13-data\\organization\\bloodRoutine.txt");
            base.put("bloodRoutine", reader.readString());

            Map<String, Object> urineRoutineExceptionData = new LinkedHashMap<>();
            urineRoutineExceptionData.put("year", Lists.newArrayList(2020, 2021, 2022, 2020, 2021, 2022, 2020, 2021, 2022, 2020, 2021, 2022, 2020, 2021, 2022));
            urineRoutineExceptionData.put("urineProtein", Lists.newArrayList(421, 420, 420, 324, 325, 326, 220, 220, 220, 148, 148, 148, 103, 103, 103));
            urineRoutineExceptionData.put("urineSugar", Lists.newArrayList(109, 107, 108, 82, 82, 82, 52, 52, 52, 30, 30, 30, 34, 34, 34));
            urineRoutineExceptionData.put("urineKetone", Lists.newArrayList(79, 78, 78, 52, 52, 52, 23, 23, 23, 22, 22, 22, 20, 20, 20));
            urineRoutineExceptionData.put("urineoOccultBlood", Lists.newArrayList(244, 244, 244, 213, 213, 212, 180, 180, 180, 133, 133, 133, 87, 87, 87));
            paramMap.put("urineRoutineExceptionData", urineRoutineExceptionData);

            List<Dict> urineRoutineExceptionLineChartData = new ArrayList<>();
            urineRoutineExceptionLineChartData.add(Dict.create().set("type", "2020").set("name", "尿蛋白").set("value", 1216));
            urineRoutineExceptionLineChartData.add(Dict.create().set("type", "2021").set("name", "尿蛋白").set("value", 1216));
            urineRoutineExceptionLineChartData.add(Dict.create().set("type", "2022").set("name", "尿蛋白").set("value", 1217));
            urineRoutineExceptionLineChartData.add(Dict.create().set("type", "2020").set("name", "尿糖").set("value", 305));
            urineRoutineExceptionLineChartData.add(Dict.create().set("type", "2021").set("name", "尿糖").set("value", 305));
            urineRoutineExceptionLineChartData.add(Dict.create().set("type", "2022").set("name", "尿糖").set("value", 306));
            urineRoutineExceptionLineChartData.add(Dict.create().set("type", "2020").set("name", "尿酮体").set("value", 196));
            urineRoutineExceptionLineChartData.add(Dict.create().set("type", "2021").set("name", "尿酮体").set("value", 196));
            urineRoutineExceptionLineChartData.add(Dict.create().set("type", "2022").set("name", "尿酮体").set("value", 195));
            urineRoutineExceptionLineChartData.add(Dict.create().set("type", "2020").set("name", "尿潜血").set("value", 857));
            urineRoutineExceptionLineChartData.add(Dict.create().set("type", "2021").set("name", "尿潜血").set("value", 856));
            urineRoutineExceptionLineChartData.add(Dict.create().set("type", "2022").set("name", "尿潜血").set("value", 857));
            paramMap.put("urineRoutineExceptionLineChartData", urineRoutineExceptionLineChartData);

            List<Dict> urineRoutineExceptionOnePieChartData = urineRoutineExceptionLineChartData.stream().filter(
                    item -> Objects.equals("2020", item.getStr("type"))).collect(Collectors.toList()
            );
            paramMap.put("urineRoutineExceptionOnePieChartData", urineRoutineExceptionOnePieChartData);

            List<Dict> urineRoutineExceptionTwoPieChartData = urineRoutineExceptionLineChartData.stream().filter(
                    item -> Objects.equals("2021", item.getStr("type"))).collect(Collectors.toList()
            );
            paramMap.put("urineRoutineExceptionTwoPieChartData", urineRoutineExceptionTwoPieChartData);

            List<Dict> urineRoutineExceptionThreePieChartData = urineRoutineExceptionLineChartData.stream().filter(
                    item -> Objects.equals("2022", item.getStr("type"))).collect(Collectors.toList()
            );
            paramMap.put("urineRoutineExceptionThreePieChartData", urineRoutineExceptionThreePieChartData);

            // 健康评估与建议（血常规）
            reader = new FileReader("D:\\13-data\\organization\\urineRoutine.txt");
            base.put("urineRoutine", reader.readString());

            Map<String, Object> bloodSugarExceptionData = new LinkedHashMap<>();
            bloodSugarExceptionData.put("year", Lists.newArrayList(2020, 2021, 2022, 2020, 2021, 2022, 2020, 2021, 2022, 2020, 2021, 2022, 2020, 2021, 2022));
            bloodSugarExceptionData.put("normal", Lists.newArrayList(1504, 1504, 1506, 1124, 1124, 1128, 761, 761, 763, 471, 471, 473, 292, 292, 293));
            bloodSugarExceptionData.put("impaired", Lists.newArrayList(791, 791, 791, 574, 574, 574, 428, 428, 428, 231, 231, 231, 154, 154, 154));
            bloodSugarExceptionData.put("highBloodSugar", Lists.newArrayList(290, 290, 290, 220, 220, 219, 137, 137, 137, 68, 68, 68, 51, 51, 51));
            paramMap.put("bloodSugarExceptionData", bloodSugarExceptionData);

            List<Dict> bloodSugarExceptionLineChartData = new ArrayList<>();
            bloodSugarExceptionLineChartData.add(Dict.create().set("type", "2020").set("name", "正常").set("value", 4152));
            bloodSugarExceptionLineChartData.add(Dict.create().set("type", "2021").set("name", "正常").set("value", 4152));
            bloodSugarExceptionLineChartData.add(Dict.create().set("type", "2022").set("name", "正常").set("value", 4163));
            bloodSugarExceptionLineChartData.add(Dict.create().set("type", "2020").set("name", "血糖受损").set("value", 2178));
            bloodSugarExceptionLineChartData.add(Dict.create().set("type", "2021").set("name", "血糖受损").set("value", 2178));
            bloodSugarExceptionLineChartData.add(Dict.create().set("type", "2022").set("name", "血糖受损").set("value", 2178));
            bloodSugarExceptionLineChartData.add(Dict.create().set("type", "2020").set("name", "血糖偏高").set("value", 766));
            bloodSugarExceptionLineChartData.add(Dict.create().set("type", "2021").set("name", "血糖偏高").set("value", 766));
            bloodSugarExceptionLineChartData.add(Dict.create().set("type", "2022").set("name", "血糖偏高").set("value", 766));
            paramMap.put("bloodSugarExceptionLineChartData", bloodSugarExceptionLineChartData);

            List<Dict> bloodSugarExceptionOnePieChartData = bloodSugarExceptionLineChartData.stream().filter(
                    item -> Objects.equals("2020", item.getStr("type"))).collect(Collectors.toList()
            );
            paramMap.put("bloodSugarExceptionOnePieChartData", bloodSugarExceptionOnePieChartData);

            List<Dict> bloodSugarExceptionTwoPieChartData = bloodSugarExceptionLineChartData.stream().filter(
                    item -> Objects.equals("2021", item.getStr("type"))).collect(Collectors.toList()
            );
            paramMap.put("bloodSugarExceptionTwoPieChartData", bloodSugarExceptionTwoPieChartData);

            List<Dict> bloodSugarExceptionThreePieChartData = bloodSugarExceptionLineChartData.stream().filter(
                    item -> Objects.equals("2022", item.getStr("type"))).collect(Collectors.toList()
            );
            paramMap.put("bloodSugarExceptionThreePieChartData", bloodSugarExceptionThreePieChartData);

            // 健康评估与建议（血糖）
            reader = new FileReader("D:\\13-data\\organization\\bloodSugar.txt");
            base.put("bloodSugar", reader.readString());

            Map<String, Object> bloodFatExceptionData = new LinkedHashMap<>();
            bloodFatExceptionData.put("year", Lists.newArrayList(2020, 2021, 2022, 2020, 2021, 2022, 2020, 2021, 2022, 2020, 2021, 2022, 2020, 2021, 2022));
            // 总胆固醇
            bloodFatExceptionData.put("cholesterol", Lists.newArrayList(211, 211, 212, 156, 155, 155, 102, 102, 102, 60, 60, 60, 52, 52, 52));
            // 甘油三酯
            bloodFatExceptionData.put("triglyceride", Lists.newArrayList(511, 511, 511, 378, 376, 377, 230, 230, 230, 114, 114, 114, 71, 71, 71));
            // 低密度脂蛋白
            bloodFatExceptionData.put("lowDensityLlipoprotein", Lists.newArrayList(423, 424, 423, 338, 338, 337, 215, 215, 215, 126, 126, 126, 69, 69, 69));
            // 高密度脂蛋白
            bloodFatExceptionData.put("highDensityLlipoprotein", Lists.newArrayList(511, 512, 513, 377, 377, 377, 272, 272, 272, 140, 140, 140, 83, 83, 83));
            paramMap.put("bloodFatExceptionData", bloodFatExceptionData);

            List<Dict> bloodFatExceptionLineChartData = new ArrayList<>();
            bloodFatExceptionLineChartData.add(Dict.create().set("type", "2020").set("name", "总胆固醇").set("value", 4152));
            bloodFatExceptionLineChartData.add(Dict.create().set("type", "2021").set("name", "总胆固醇").set("value", 4152));
            bloodFatExceptionLineChartData.add(Dict.create().set("type", "2022").set("name", "总胆固醇").set("value", 4163));
            bloodFatExceptionLineChartData.add(Dict.create().set("type", "2020").set("name", "甘油三酯").set("value", 2178));
            bloodFatExceptionLineChartData.add(Dict.create().set("type", "2021").set("name", "甘油三酯").set("value", 2178));
            bloodFatExceptionLineChartData.add(Dict.create().set("type", "2022").set("name", "甘油三酯").set("value", 2178));
            bloodFatExceptionLineChartData.add(Dict.create().set("type", "2020").set("name", "低密度脂蛋白").set("value", 766));
            bloodFatExceptionLineChartData.add(Dict.create().set("type", "2021").set("name", "低密度脂蛋白").set("value", 766));
            bloodFatExceptionLineChartData.add(Dict.create().set("type", "2022").set("name", "低密度脂蛋白").set("value", 766));
            bloodFatExceptionLineChartData.add(Dict.create().set("type", "2020").set("name", "高密度脂蛋白").set("value", 766));
            bloodFatExceptionLineChartData.add(Dict.create().set("type", "2021").set("name", "高密度脂蛋白").set("value", 766));
            bloodFatExceptionLineChartData.add(Dict.create().set("type", "2022").set("name", "高密度脂蛋白").set("value", 766));
            paramMap.put("bloodFatExceptionLineChartData", bloodFatExceptionLineChartData);

            List<Dict> bloodFatExceptionOnePieChartData = bloodFatExceptionLineChartData.stream().filter(
                    item -> Objects.equals("2020", item.getStr("type"))).collect(Collectors.toList()
            );
            paramMap.put("bloodFatExceptionOnePieChartData", bloodFatExceptionOnePieChartData);

            List<Dict> bloodFatExceptionTwoPieChartData = bloodFatExceptionLineChartData.stream().filter(
                    item -> Objects.equals("2021", item.getStr("type"))).collect(Collectors.toList()
            );
            paramMap.put("bloodFatExceptionTwoPieChartData", bloodFatExceptionTwoPieChartData);

            List<Dict> bloodFatExceptionThreePieChartData = bloodFatExceptionLineChartData.stream().filter(
                    item -> Objects.equals("2022", item.getStr("type"))).collect(Collectors.toList()
            );
            paramMap.put("bloodFatExceptionThreePieChartData", bloodFatExceptionThreePieChartData);

            // 健康评估与建议（血糖）
            reader = new FileReader("D:\\13-data\\organization\\bloodFat.txt");
            base.put("bloodFat", reader.readString());

            // 肝功能
            Map<String, Object> liverExceptionData = new LinkedHashMap<>();
            liverExceptionData.put("year", Lists.newArrayList(2020, 2021, 2022, 2020, 2021, 2022, 2020, 2021, 2022, 2020, 2021, 2022, 2020, 2021, 2022));
            // 总胆红素
            liverExceptionData.put("bilirubin", Lists.newArrayList(211, 211, 212, 156, 155, 155, 102, 102, 102, 60, 60, 60, 52, 52, 52));
            // 谷草转氨酶
            liverExceptionData.put("GOT", Lists.newArrayList(511, 511, 511, 378, 376, 377, 230, 230, 230, 114, 114, 114, 71, 71, 71));
            // 谷丙转氨酶
            liverExceptionData.put("ALT", Lists.newArrayList(423, 424, 423, 338, 338, 337, 215, 215, 215, 126, 126, 126, 69, 69, 69));
            paramMap.put("liverExceptionData", liverExceptionData);

            List<Dict> liverExceptionLineChartData = new ArrayList<>();
            liverExceptionLineChartData.add(Dict.create().set("type", "2020").set("name", "总胆红素").set("value", 4152));
            liverExceptionLineChartData.add(Dict.create().set("type", "2021").set("name", "总胆红素").set("value", 4152));
            liverExceptionLineChartData.add(Dict.create().set("type", "2022").set("name", "总胆红素").set("value", 4163));
            liverExceptionLineChartData.add(Dict.create().set("type", "2020").set("name", "谷草转氨酶").set("value", 2178));
            liverExceptionLineChartData.add(Dict.create().set("type", "2021").set("name", "谷草转氨酶").set("value", 2178));
            liverExceptionLineChartData.add(Dict.create().set("type", "2022").set("name", "谷草转氨酶").set("value", 2178));
            liverExceptionLineChartData.add(Dict.create().set("type", "2020").set("name", "谷丙转氨酶").set("value", 766));
            liverExceptionLineChartData.add(Dict.create().set("type", "2021").set("name", "谷丙转氨酶").set("value", 766));
            liverExceptionLineChartData.add(Dict.create().set("type", "2022").set("name", "谷丙转氨酶").set("value", 766));
            paramMap.put("liverExceptionLineChartData", liverExceptionLineChartData);

            List<Dict> liverExceptionOnePieChartData = liverExceptionLineChartData.stream().filter(
                    item -> Objects.equals("2020", item.getStr("type"))).collect(Collectors.toList()
            );
            paramMap.put("liverExceptionOnePieChartData", liverExceptionOnePieChartData);

            List<Dict> liverExceptionTwoPieChartData = liverExceptionLineChartData.stream().filter(
                    item -> Objects.equals("2021", item.getStr("type"))).collect(Collectors.toList()
            );
            paramMap.put("liverExceptionTwoPieChartData", liverExceptionTwoPieChartData);

            List<Dict> liverExceptionThreePieChartData = liverExceptionLineChartData.stream().filter(
                    item -> Objects.equals("2022", item.getStr("type"))).collect(Collectors.toList()
            );
            paramMap.put("liverExceptionThreePieChartData", liverExceptionThreePieChartData);

            // 健康评估与建议（肝功能）
            reader = new FileReader("D:\\13-data\\organization\\liver.txt");
            base.put("liver", reader.readString());

            Map<String, Object> kidneyExceptionData = new LinkedHashMap<>();
            kidneyExceptionData.put("year", Lists.newArrayList(2020, 2021, 2022, 2020, 2021, 2022, 2020, 2021, 2022, 2020, 2021, 2022, 2020, 2021, 2022));
            // 肌肝
            kidneyExceptionData.put("creatinine", Lists.newArrayList(211, 211, 212, 156, 155, 155, 102, 102, 102, 60, 60, 60, 52, 52, 52));
            // 尿素
            kidneyExceptionData.put("urea", Lists.newArrayList(511, 511, 511, 378, 376, 377, 230, 230, 230, 114, 114, 114, 71, 71, 71));
            paramMap.put("kidneyExceptionData", kidneyExceptionData);

            List<Dict> kidneyExceptionLineChartData = new ArrayList<>();
            kidneyExceptionLineChartData.add(Dict.create().set("type", "2020").set("name", "肌肝").set("value", 4152));
            kidneyExceptionLineChartData.add(Dict.create().set("type", "2021").set("name", "肌肝").set("value", 4152));
            kidneyExceptionLineChartData.add(Dict.create().set("type", "2022").set("name", "肌肝").set("value", 4163));
            kidneyExceptionLineChartData.add(Dict.create().set("type", "2020").set("name", "尿素").set("value", 2178));
            kidneyExceptionLineChartData.add(Dict.create().set("type", "2021").set("name", "尿素").set("value", 2178));
            kidneyExceptionLineChartData.add(Dict.create().set("type", "2022").set("name", "尿素").set("value", 2178));
            paramMap.put("kidneyExceptionLineChartData", kidneyExceptionLineChartData);

            List<Dict> kidneyExceptionOnePieChartData = kidneyExceptionLineChartData.stream().filter(
                    item -> Objects.equals("2020", item.getStr("type"))).collect(Collectors.toList()
            );
            paramMap.put("kidneyExceptionOnePieChartData", kidneyExceptionOnePieChartData);

            List<Dict> kidneyExceptionTwoPieChartData = kidneyExceptionLineChartData.stream().filter(
                    item -> Objects.equals("2021", item.getStr("type"))).collect(Collectors.toList()
            );
            paramMap.put("kidneyExceptionTwoPieChartData", kidneyExceptionTwoPieChartData);

            List<Dict> kidneyExceptionThreePieChartData = kidneyExceptionLineChartData.stream().filter(
                    item -> Objects.equals("2022", item.getStr("type"))).collect(Collectors.toList()
            );
            paramMap.put("kidneyExceptionThreePieChartData", kidneyExceptionThreePieChartData);

            // 健康评估与建议（肾功能）
            reader = new FileReader("D:\\13-data\\organization\\kidney.txt");
            base.put("kidney", reader.readString());

            // 腹部B超
            Map<String, Object> transabdominalUltrasoundExceptionData = new LinkedHashMap<>();
            transabdominalUltrasoundExceptionData.put("year", Lists.newArrayList(2020, 2021, 2022, 2020, 2021, 2022, 2020, 2021, 2022, 2020, 2021, 2022, 2020, 2021, 2022));
            transabdominalUltrasoundExceptionData.put("noException", Lists.newArrayList(211, 211, 212, 156, 155, 155, 102, 102, 102, 60, 60, 60, 52, 52, 52));
            transabdominalUltrasoundExceptionData.put("hasException", Lists.newArrayList(511, 511, 511, 378, 376, 377, 230, 230, 230, 114, 114, 114, 71, 71, 71));
            paramMap.put("transabdominalUltrasoundExceptionData", transabdominalUltrasoundExceptionData);

            List<Dict> transabdominalUltrasoundExceptionLineChartData = new ArrayList<>();
            transabdominalUltrasoundExceptionLineChartData.add(Dict.create().set("type", "2020").set("name", "无异常").set("value", 4152));
            transabdominalUltrasoundExceptionLineChartData.add(Dict.create().set("type", "2021").set("name", "无异常").set("value", 4152));
            transabdominalUltrasoundExceptionLineChartData.add(Dict.create().set("type", "2022").set("name", "无异常").set("value", 4163));
            transabdominalUltrasoundExceptionLineChartData.add(Dict.create().set("type", "2020").set("name", "异常").set("value", 2178));
            transabdominalUltrasoundExceptionLineChartData.add(Dict.create().set("type", "2021").set("name", "异常").set("value", 2178));
            transabdominalUltrasoundExceptionLineChartData.add(Dict.create().set("type", "2022").set("name", "异常").set("value", 2178));
            paramMap.put("transabdominalUltrasoundExceptionLineChartData", transabdominalUltrasoundExceptionLineChartData);

            List<Dict> transabdominalUltrasoundExceptionOnePieChartData = transabdominalUltrasoundExceptionLineChartData.stream().filter(
                    item -> Objects.equals("2020", item.getStr("type"))).collect(Collectors.toList()
            );
            paramMap.put("transabdominalUltrasoundExceptionOnePieChartData", transabdominalUltrasoundExceptionOnePieChartData);

            List<Dict> transabdominalUltrasoundExceptionTwoPieChartData = transabdominalUltrasoundExceptionLineChartData.stream().filter(
                    item -> Objects.equals("2021", item.getStr("type"))).collect(Collectors.toList()
            );
            paramMap.put("transabdominalUltrasoundExceptionTwoPieChartData", transabdominalUltrasoundExceptionTwoPieChartData);

            List<Dict> transabdominalUltrasoundExceptionThreePieChartData = transabdominalUltrasoundExceptionLineChartData.stream().filter(
                    item -> Objects.equals("2022", item.getStr("type"))).collect(Collectors.toList()
            );
            paramMap.put("transabdominalUltrasoundExceptionThreePieChartData", transabdominalUltrasoundExceptionThreePieChartData);

            // 健康评估与建议（肝功能）
            reader = new FileReader("D:\\13-data\\organization\\transabdominalUltrasound.txt");
            base.put("transabdominalUltrasound", reader.readString());

            // 心电图
            Map<String, Object> electrocardiogramExceptionData = new LinkedHashMap<>();
            electrocardiogramExceptionData.put("year", Lists.newArrayList(2020, 2021, 2022, 2020, 2021, 2022, 2020, 2021, 2022, 2020, 2021, 2022, 2020, 2021, 2022));
            electrocardiogramExceptionData.put("noException", Lists.newArrayList(211, 211, 212, 156, 155, 155, 102, 102, 102, 60, 60, 60, 52, 52, 52));
            electrocardiogramExceptionData.put("hasException", Lists.newArrayList(511, 511, 511, 378, 376, 377, 230, 230, 230, 114, 114, 114, 71, 71, 71));
            paramMap.put("electrocardiogramExceptionData", electrocardiogramExceptionData);

            List<Dict> electrocardiogramExceptionLineChartData = new ArrayList<>();
            electrocardiogramExceptionLineChartData.add(Dict.create().set("type", "2020").set("name", "无异常").set("value", 4152));
            electrocardiogramExceptionLineChartData.add(Dict.create().set("type", "2021").set("name", "无异常").set("value", 4152));
            electrocardiogramExceptionLineChartData.add(Dict.create().set("type", "2022").set("name", "无异常").set("value", 4163));
            electrocardiogramExceptionLineChartData.add(Dict.create().set("type", "2020").set("name", "异常").set("value", 2178));
            electrocardiogramExceptionLineChartData.add(Dict.create().set("type", "2021").set("name", "异常").set("value", 2178));
            electrocardiogramExceptionLineChartData.add(Dict.create().set("type", "2022").set("name", "异常").set("value", 2178));
            paramMap.put("electrocardiogramExceptionLineChartData", electrocardiogramExceptionLineChartData);

            List<Dict> electrocardiogramExceptionOnePieChartData = electrocardiogramExceptionLineChartData.stream().filter(
                    item -> Objects.equals("2020", item.getStr("type"))).collect(Collectors.toList()
            );
            paramMap.put("electrocardiogramExceptionOnePieChartData", electrocardiogramExceptionOnePieChartData);

            List<Dict> electrocardiogramExceptionTwoPieChartData = electrocardiogramExceptionLineChartData.stream().filter(
                    item -> Objects.equals("2021", item.getStr("type"))).collect(Collectors.toList()
            );
            paramMap.put("electrocardiogramExceptionTwoPieChartData", electrocardiogramExceptionTwoPieChartData);

            List<Dict> electrocardiogramExceptionThreePieChartData = electrocardiogramExceptionLineChartData.stream().filter(
                    item -> Objects.equals("2022", item.getStr("type"))).collect(Collectors.toList()
            );
            paramMap.put("electrocardiogramExceptionThreePieChartData", electrocardiogramExceptionThreePieChartData);

            // 健康评估与建议（肝功能）
            reader = new FileReader("D:\\13-data\\organization\\electrocardiogram.txt");
            base.put("electrocardiogram", reader.readString());

            Map<String, Object> chronicDiseaseExceptionData = new LinkedHashMap<>();
            chronicDiseaseExceptionData.put("year", Lists.newArrayList(2020, 2021, 2022, 2020, 2021, 2022, 2020, 2021, 2022, 2020, 2021, 2022, 2020, 2021, 2022));
            chronicDiseaseExceptionData.put("hypertension", Lists.newArrayList(211, 211, 212, 156, 155, 155, 102, 102, 102, 60, 60, 60, 52, 52, 52));
            chronicDiseaseExceptionData.put("diabetes", Lists.newArrayList(511, 511, 511, 378, 376, 377, 230, 230, 230, 114, 114, 114, 71, 71, 71));
            chronicDiseaseExceptionData.put("coronaryDisease", Lists.newArrayList(423, 424, 423, 338, 338, 337, 215, 215, 215, 126, 126, 126, 69, 69, 69));
            chronicDiseaseExceptionData.put("apoplexy", Lists.newArrayList(511, 512, 513, 377, 377, 377, 272, 272, 272, 140, 140, 140, 83, 83, 83));
            chronicDiseaseExceptionData.put("mentalDisease", Lists.newArrayList(511, 512, 513, 377, 377, 377, 272, 272, 272, 140, 140, 140, 83, 83, 83));
            paramMap.put("chronicDiseaseExceptionData", chronicDiseaseExceptionData);

            List<Dict> chronicDiseaseExceptionLineChartData = new ArrayList<>();
            chronicDiseaseExceptionLineChartData.add(Dict.create().set("type", "2020").set("name", "高血压").set("value", 4152));
            chronicDiseaseExceptionLineChartData.add(Dict.create().set("type", "2021").set("name", "高血压").set("value", 4152));
            chronicDiseaseExceptionLineChartData.add(Dict.create().set("type", "2022").set("name", "高血压").set("value", 4163));
            chronicDiseaseExceptionLineChartData.add(Dict.create().set("type", "2020").set("name", "糖尿病").set("value", 2178));
            chronicDiseaseExceptionLineChartData.add(Dict.create().set("type", "2021").set("name", "糖尿病").set("value", 2178));
            chronicDiseaseExceptionLineChartData.add(Dict.create().set("type", "2022").set("name", "糖尿病").set("value", 2178));
            chronicDiseaseExceptionLineChartData.add(Dict.create().set("type", "2020").set("name", "冠心病").set("value", 766));
            chronicDiseaseExceptionLineChartData.add(Dict.create().set("type", "2021").set("name", "冠心病").set("value", 766));
            chronicDiseaseExceptionLineChartData.add(Dict.create().set("type", "2022").set("name", "冠心病").set("value", 766));
            chronicDiseaseExceptionLineChartData.add(Dict.create().set("type", "2020").set("name", "脑卒中").set("value", 766));
            chronicDiseaseExceptionLineChartData.add(Dict.create().set("type", "2021").set("name", "脑卒中").set("value", 766));
            chronicDiseaseExceptionLineChartData.add(Dict.create().set("type", "2022").set("name", "脑卒中").set("value", 766));
            chronicDiseaseExceptionLineChartData.add(Dict.create().set("type", "2020").set("name", "精神病").set("value", 766));
            chronicDiseaseExceptionLineChartData.add(Dict.create().set("type", "2021").set("name", "精神病").set("value", 766));
            chronicDiseaseExceptionLineChartData.add(Dict.create().set("type", "2022").set("name", "精神病").set("value", 766));
            paramMap.put("chronicDiseaseExceptionLineChartData", chronicDiseaseExceptionLineChartData);

            List<Dict> chronicDiseaseExceptionOnePieChartData = chronicDiseaseExceptionLineChartData.stream().filter(
                    item -> Objects.equals("2020", item.getStr("type"))).collect(Collectors.toList()
            );
            paramMap.put("chronicDiseaseExceptionOnePieChartData", chronicDiseaseExceptionOnePieChartData);

            List<Dict> chronicDiseaseExceptionTwoPieChartData = chronicDiseaseExceptionLineChartData.stream().filter(
                    item -> Objects.equals("2021", item.getStr("type"))).collect(Collectors.toList()
            );
            paramMap.put("chronicDiseaseExceptionTwoPieChartData", chronicDiseaseExceptionTwoPieChartData);

            List<Dict> chronicDiseaseExceptionThreePieChartData = chronicDiseaseExceptionLineChartData.stream().filter(
                    item -> Objects.equals("2022", item.getStr("type"))).collect(Collectors.toList()
            );
            paramMap.put("chronicDiseaseExceptionThreePieChartData", chronicDiseaseExceptionThreePieChartData);

            // 健康评估与建议（肝功能）
            reader = new FileReader("D:\\13-data\\organization\\chronicDisease.txt");
            base.put("chronicDisease", reader.readString());

            List<Dict> diseaseStatsList = new ArrayList<>();
            diseaseStatsList.add(Dict.create().set("name", "脑血管疾病").set("value", 228));
            diseaseStatsList.add(Dict.create().set("name", "肾脏疾病").set("value", 11));
            diseaseStatsList.add(Dict.create().set("name", "心血管疾病").set("value", 3169));
            diseaseStatsList.add(Dict.create().set("name", "高血压").set("value", 3107));
            diseaseStatsList.add(Dict.create().set("name", "眼部疾病").set("value", 5));
            diseaseStatsList.add(Dict.create().set("name", "神经系统其他疾病").set("value", 11));
            diseaseStatsList.add(Dict.create().set("name", "糖尿病").set("value", 820));
            diseaseStatsList.add(Dict.create().set("name", "慢性支气管炎").set("value", 68));
            diseaseStatsList.add(Dict.create().set("name", "慢性阻塞性肺病").set("value", 20));
            diseaseStatsList.add(Dict.create().set("name", "恶性肿瘤").set("value", 5));
            diseaseStatsList.add(Dict.create().set("name", "其他").set("value", 81));
            diseaseStatsList.add(Dict.create().set("name", "冠心病").set("value", 291));
            diseaseStatsList.add(Dict.create().set("name", "脑卒中").set("value", 327));

            Map<String, Integer> diseaseStatsMap = new HashMap<>();
            int diseaseCountNum = diseaseStatsList.stream().mapToInt(item -> item.getInt("value")).sum();
            diseaseStatsList.stream().forEach(item -> {
                diseaseStatsMap.put(item.getStr("name"), item.getInt("value"));
                item.set("percent", calPercent(diseaseCountNum, item.getInt("value")));
            });

            paramMap.put("diseaseStatsList", diseaseStatsList);
            paramMap.put("diseaseStatsMap", diseaseStatsMap);


            reader = new FileReader("D:\\13-data\\organization\\diseaseStats.txt");
            base.put("diseaseStats", reader.readString());

            reader = new FileReader("D:\\13-data\\organization\\high.txt");
            base.put("high", reader.readString());

            reader = new FileReader("D:\\13-data\\organization\\workSummary.txt");
            base.put("workSummary", reader.readString());

            paramMap.put("base", base);

            // 编译
            JasperReport jasperReport = JasperCompileManager.compileReport("C:\\Users\\PRS\\JaspersoftWorkspace\\MyReports\\organization_statistics_report_map.jrxml");
//            ClassPathResource resource = new ClassPathResource("organization_statistics_report.jrxml");
//            JasperReport jasperReport = JasperCompileManager.compileReport(resource.getInputStream());
            JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, paramMap, new JREmptyDataSource());

            JasperExportManager.exportReportToPdfStream(jasperPrint, response.getOutputStream());

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @ResponseBody
    @GetMapping("/chart")
    public void chart(HttpServletResponse response) {
        try {
            // 报表需要的动态参数
            List<Dict> dataList = Lists.newArrayList();
            dataList.add(Dict.create().set("name", "总人数").set("value", 100));
            dataList.add(Dict.create().set("name", "孕产妇").set("value", 20));
            dataList.add(Dict.create().set("name", "0-6岁儿童").set("value", 5));
            dataList.add(Dict.create().set("name", "老年人").set("value", 15));
            dataList.add(Dict.create().set("name", "高血压").set("value", 10));
            dataList.add(Dict.create().set("name", "糖尿病").set("value", 2));
            dataList.add(Dict.create().set("name", "脑卒中").set("value", 18));
            dataList.add(Dict.create().set("name", "冠心病").set("value", 30));
            dataList.add(Dict.create().set("name", "恶性肿瘤").set("value", 12));
            dataList.add(Dict.create().set("name", "慢阻肺").set("value", 8));
            // 编译
            JasperReport jasperReport = JasperCompileManager.compileReport(
                    ResourceUtils.getFile("classpath:BarChartDemo.jrxml").getAbsolutePath()
            );

            Map<String, Object> paramMap = new HashMap<>();
            paramMap.put("dataList", dataList);
            JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, paramMap, new JREmptyDataSource());

            JasperExportManager.exportReportToPdfStream(jasperPrint, response.getOutputStream());

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @GetMapping("/html")
    public void html(HttpServletResponse response) {
        response.setContentType("text/html");
        response.setCharacterEncoding("UTF-8");
        try {
            // 报表需要的动态参数
            Map<String, Object> paramMap = generateTestData();
            // 编译
            JasperReport jasperReport = JasperCompileManager.compileReport(
                    ResourceUtils.getFile("classpath:NBNA_2.jrxml").getAbsolutePath()
            );

            // 填充数据
            JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, paramMap, new JREmptyDataSource());

            HtmlExporter htmlExporter = new HtmlExporter(DefaultJasperReportsContext.getInstance());
            htmlExporter.setExporterInput(new SimpleExporterInput(jasperPrint));
            htmlExporter.setExporterOutput(new SimpleHtmlExporterOutput(response.getWriter()));
            htmlExporter.exportReport();


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



    @ResponseBody
    @GetMapping("/textFieldDemo")
    public void textFieldDemo(HttpServletResponse response) {
        try {
            FileReader reader = new FileReader("D:\\13-data\\TextFieldDemo-content-simple.txt");
            String content = reader.readString();
            log.info("simple: {}", content);

            Map<String, Object> parameter = new HashMap<>();
            parameter.put("simple", content);

            reader = new FileReader("D:\\13-data\\TextFieldDemo-content-more.txt");
            content = reader.readString();
            log.info("content: {}", content);
            parameter.put("content", content);
            // 编译
            JasperReport jasperReport = JasperCompileManager.compileReport("C:\\Users\\PRS\\JaspersoftWorkspace\\MyReports\\textFieldDemo2.jrxml");
//            ClassPathResource resource = new ClassPathResource("organization_statistics_report.jrxml");
//            JasperReport jasperReport = JasperCompileManager.compileReport(resource.getInputStream());
            JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameter, new JREmptyDataSource());

            JasperExportManager.exportReportToPdfStream(jasperPrint, response.getOutputStream());

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private String calPercent(Integer total, Integer num) {
        BigDecimal tb = new BigDecimal(total);
        BigDecimal nb = new BigDecimal(num);
        return StrUtil.format("{}%", nb.divide(tb, 2, BigDecimal.ROUND_HALF_UP));
    }
}
