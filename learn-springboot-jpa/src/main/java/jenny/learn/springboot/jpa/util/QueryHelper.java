package jenny.learn.springboot.jpa.util;

import com.google.common.collect.Lists;
import jenny.learn.springboot.jpa.annotation.Query;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;

/**
 * JPA复杂查询查询条件组装工具类
 *
 * @author: wanggc
 */
@Slf4j
public class QueryHelper {

    public static <E, P> Predicate getPredicate(Root<E> root, P param, CriteriaBuilder criteriaBuilder) {
        List<Predicate> list = new ArrayList<>();
        if (Objects.isNull(param)) {
            return criteriaBuilder.and(list.toArray(new Predicate[0]));
        }
        // 遍历param所有字段，处理
        List<Field> fieldList = Lists.newArrayList(param.getClass().getDeclaredFields());
        try {
            for (Field field : fieldList) {
                boolean accessible = field.isAccessible();
                // 设置field的访问权限，保证对field的访问
                field.setAccessible(true);
                // 获取Query注解信息
                Query query = field.getAnnotation(Query.class);
                if (Objects.nonNull(query)) {
                    // 属性名
                    String propName = query.propName();
//                    // 连接查询的属性名
//                    String joinName = query.joinName();
                    // 多字段模糊搜索，仅支持String类型字段，多个用逗号隔开
                    String blurry = query.blurry();
                    // 属性名
                    String attributeName = StringUtils.isBlank(propName) ? field.getName() : propName;
                    // 查询参数类型
                    Class<?> fieldType = field.getType();
                    // 查询参数的值
                    Object val = field.get(param);
                    // 如果查询参数对应的值为空，则跳过此参数
                    if (Objects.isNull(val) || Objects.equals(val, "")) {
                        continue;
                    }
                    // 模糊多字段
                    if (StringUtils.isNotEmpty(blurry)) {
                        String[] blurrys = blurry.split(",");
                        List<Predicate> orPredicate = new ArrayList<>();
                        for (String s : blurrys) {
                            orPredicate.add(criteriaBuilder.like(root.get(s).as(String.class), "%" + val.toString() + "%"));
                        }
                        Predicate[] p = new Predicate[orPredicate.size()];
                        list.add(criteriaBuilder.or(orPredicate.toArray(p)));
                        continue;
                    }
//                    if (ObjectUtil.isNotEmpty(joinName)) {
//                        String[] joinNames = joinName.split(">");
//                        for (String name : joinNames) {
//                            switch (query.join()) {
//                                case LEFT:
//                                    if (ObjectUtil.isNotNull(join) && ObjectUtil.isNotNull(val)) {
//                                        join = join.join(name, JoinType.LEFT);
//                                    } else {
//                                        join = root.join(name, JoinType.LEFT);
//                                    }
//                                    break;
//                                case RIGHT:
//                                    if (ObjectUtil.isNotNull(join) && ObjectUtil.isNotNull(val)) {
//                                        join = join.join(name, JoinType.RIGHT);
//                                    } else {
//                                        join = root.join(name, JoinType.RIGHT);
//                                    }
//                                    break;
//                                case INNER:
//                                    if (ObjectUtil.isNotNull(join) && ObjectUtil.isNotNull(val)) {
//                                        join = join.join(name, JoinType.INNER);
//                                    } else {
//                                        join = root.join(name, JoinType.INNER);
//                                    }
//                                    break;
//                                default:
//                                    break;
//                            }
//                        }
//                    }
                    switch (query.type()) {
                        case EQUAL:
                            list.add(criteriaBuilder.equal(root.get(attributeName).as((Class<? extends Comparable>) fieldType), val));
                            break;
                        case GREATER_THAN:
                            list.add(criteriaBuilder.greaterThanOrEqualTo(root.get(attributeName).as((Class<? extends Comparable>) fieldType), (Comparable) val));
                            break;
                        case LESS_THAN:
                            list.add(criteriaBuilder.lessThanOrEqualTo(root.get(attributeName).as((Class<? extends Comparable>) fieldType), (Comparable) val));
                            break;
                        case LESS_THAN_NQ:
                            list.add(criteriaBuilder.lessThan(root.get(attributeName).as((Class<? extends Comparable>) fieldType), (Comparable) val));
                            break;
                        case INNER_LIKE:
                            list.add(criteriaBuilder.like(root.get(attributeName).as(String.class), "%" + val.toString() + "%"));
                            break;
                        case LEFT_LIKE:
                            list.add(criteriaBuilder.like(root.get(attributeName).as(String.class), "%" + val.toString()));
                            break;
                        case RIGHT_LIKE:
                            list.add(criteriaBuilder.like(root.get(attributeName).as(String.class), val.toString() + "%"));
                            break;
                        case IN:
                            if (Objects.nonNull(val) && ((Collection<Long>) val).size() > 0) {
                                list.add(root.get(attributeName).in((Collection<Long>) val));
                            }
                            break;
                        case NOT_EQUAL:
                            list.add(criteriaBuilder.notEqual(root.get(attributeName), val));
                            break;
                        case NOT_NULL:
                            list.add(criteriaBuilder.isNotNull(root.get(attributeName)));
                            break;
                        case IS_NULL:
                            list.add(criteriaBuilder.isNull(root.get(attributeName)));
                            break;
                        case BETWEEN:
                            List<Object> between = new ArrayList<>((List<Object>) val);
                            list.add(criteriaBuilder.between(root.get(attributeName).as((Class<? extends Comparable>) between.get(0).getClass()),
                                    (Comparable) between.get(0), (Comparable) between.get(1)));
                            break;
                        default:
                            break;
                    }
                }
                // 还原field的访问权限
                field.setAccessible(accessible);
            }
            ;
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
        int size = list.size();
        return criteriaBuilder.and(list.toArray(new Predicate[size]));
    }

//    @SuppressWarnings("unchecked")
//    private static <T, R> Expression<T> getExpression(String attributeName, Join join, Root<R> root) {
//        if (Objects.nonNull(join)) {
//            return join.get(attributeName);
//        } else {
//            return root.get(attributeName);
//        }
//    }
}
