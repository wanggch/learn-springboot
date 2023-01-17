package jenny.learn.springboot.mybatis.controller;

import jenny.learn.springboot.mybatis.service.TransferLogService;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
@RequestMapping("/transferLog")
public class TransferLogController {

    @Resource
    private TransferLogService transferLogService;

    @RequestMapping("/query")
    public Object query() {
        return transferLogService.query();
    }

    @RequestMapping("/{id}")
    public Object findById(@PathVariable(name = "id") String id) {
        return transferLogService.findById(id);
    }
}
