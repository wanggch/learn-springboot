package learn.springboot.sharding.jdbc.config;

import com.google.common.collect.Lists;
import com.google.gson.Gson;
import lombok.extern.slf4j.Slf4j;
import org.apache.shardingsphere.api.sharding.complex.ComplexKeysShardingAlgorithm;
import org.apache.shardingsphere.api.sharding.complex.ComplexKeysShardingValue;

import java.util.Collection;
import java.util.List;
import java.util.Map;

@Slf4j
public class MyComplexKeysShardingAlgorithm implements ComplexKeysShardingAlgorithm<Long> {

    @Override
    public Collection<String> doSharding(Collection<String> availableTargetNames, ComplexKeysShardingValue<Long> shardingValue) {
        log.info("availableTargetNames: {}, shardingValue: {}", new Gson().toJson(availableTargetNames), new Gson().toJson(shardingValue));
        // availableTargetNames: ["t_order_0","t_order_1","t_order_2","t_order_3"],
        // shardingValue: {"logicTableName":"t_order","columnNameAndShardingValuesMap":{"create_time":["Feb 26, 2022 12:56:29 PM"],"order_id":[1],"user_id":[1]},"columnNameAndRangeValuesMap":{}}
        String logicTableName = shardingValue.getLogicTableName();
        Map<String, Collection<Long>> columnNameAndShardingValuesMap = shardingValue.getColumnNameAndShardingValuesMap();
        // 返回结果
        List<String> resultList = Lists.newArrayList();
        resultList.add("t_order_1");
        return resultList;
    }
}
