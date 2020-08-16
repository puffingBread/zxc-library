package org.humor.zxc.library.commons.util.dto;

import com.fasterxml.jackson.annotation.JsonValue;
import org.apache.commons.lang3.StringUtils;
import org.springframework.util.CollectionUtils;

import java.io.Serializable;
import java.util.List;
import java.util.stream.Collectors;

public class Sort implements Serializable {
    private static final long serialVersionUID = -7128278092009117637L;

    /**
     * 默认降序
     */
    public static final Direction DEFAULT_DIRECTION = Direction.DESC;

    private List<Order> orders;

    public String orderBy(boolean useCamelCaseMapping) {
        if (CollectionUtils.isEmpty(orders)) {
            return StringUtils.EMPTY;
        }

        return orders.stream().filter(n -> StringUtils.isNotEmpty(n.getProperty()))
            .map(n -> String.format("%s %s", n.getProperty(useCamelCaseMapping), n.getDefaultDirection()))
            .collect(Collectors.joining(","));
    }

    public List<Order> getOrders() {
        return orders;
    }

    public void setOrders(List<Order> orders) {
        this.orders = orders;
    }

    /**
     * Enumeration for sort directions.
     */
    public static enum Direction {
        /**
         * 升序
         */
        @JsonValue
        ASC,
        /**
         * 降序
         */
        @JsonValue
        DESC;
    }
    public static class Order implements Serializable {

        private static final long serialVersionUID = 1522511010900108987L;
        private static final String UNDER_LINE = "_";

        private Direction direction;
        private String property;

        public Direction getDefaultDirection() {
            return direction != null? direction: DEFAULT_DIRECTION;
        }

        public String getProperty(boolean useCamelCaseMapping) {
            return useCamelCaseMapping && isCamel(property)? camelToUnderline(property): property;
        }

        public Direction getDirection() {
            return direction;
        }

        public void setDirection(Direction direction) {
            this.direction = direction;
        }

        public String getProperty() {
            return property;
        }

        public void setProperty(String property) {
            this.property = property;
        }

        private boolean isCamel(String str) {
            if (str.contains(UNDER_LINE)) {
                return false;
            }
            return Character.isLowerCase(str.charAt(0));
        }

        /**
         * 字符串驼峰转下划线格式
         *
         * @param param 需要转换的字符串
         * @return 转换好的字符串
         */
        public static String camelToUnderline(String param) {
            if (StringUtils.isEmpty(param)) {
                return StringUtils.EMPTY;
            }
            int len = param.length();
            StringBuilder sb = new StringBuilder(len);
            for (int i = 0; i < len; i++) {
                char c = param.charAt(i);
                if (Character.isUpperCase(c) && i > 0) {
                    sb.append(UNDER_LINE);
                }
                sb.append(Character.toLowerCase(c));
            }
            return sb.toString();
        }
    }

}
