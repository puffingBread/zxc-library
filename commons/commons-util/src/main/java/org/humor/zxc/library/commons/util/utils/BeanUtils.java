package org.humor.zxc.library.commons.util.utils;

import org.apache.commons.lang3.ArrayUtils;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;

import java.util.*;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collectors;


public class BeanUtils {

    public static <T> T copyProperties(Object source, T target, String... ignoreProperties) {
        if (source == null) {
            return target;
        }
        org.springframework.beans.BeanUtils.copyProperties(source, target, ignoreProperties);
        return target;
    }

    public static <T, R> R copyProperties(T source, Supplier<R> supplier, String... ignoreProperties) {
        R target = supplier.get();
        if (source == null) {
            return target;
        }
        org.springframework.beans.BeanUtils.copyProperties(source, target, ignoreProperties);
        return target;
    }

    private static String[] getNullPropertyNames(Object source) {
        final BeanWrapper src = new BeanWrapperImpl(source);
        java.beans.PropertyDescriptor[] pds = src.getPropertyDescriptors();

        Set<String> emptyNames = new HashSet<>();
        for (java.beans.PropertyDescriptor pd : pds) {
            Object srcValue = src.getPropertyValue(pd.getName());
            if (srcValue == null) {
                emptyNames.add(pd.getName());
            }
        }
        String[] result = new String[emptyNames.size()];
        return emptyNames.toArray(result);
    }

    /**
     * 适用于String, Collection, Map or Array
     * 获取是null或者为empty（即长度为0）的字段名称
     * @param source
     * @return
     */
    private static String[] getEmptyPropertyNames(Object source) {
        final BeanWrapper src = new BeanWrapperImpl(source);
        java.beans.PropertyDescriptor[] pds = src.getPropertyDescriptors();

        Set<String> emptyNames = new HashSet<>();
        for (java.beans.PropertyDescriptor pd : pds) {
            Object srcValue = src.getPropertyValue(pd.getName());
            if (srcValue == null) {
                emptyNames.add(pd.getName());
            } else if (srcValue instanceof Object[] && ((Object[])srcValue).length == 0) {
                emptyNames.add(pd.getName());
            } else if (srcValue instanceof CharSequence && ((CharSequence) srcValue).length() == 0) {
                emptyNames.add(pd.getName());
            } else if (srcValue instanceof Collection && ((Collection) srcValue).size() == 0) {
                emptyNames.add(pd.getName());
            } else if (srcValue instanceof Map && ((Map) srcValue).size() == 0) {
                emptyNames.add(pd.getName());
            }
        }
        String[] result = new String[emptyNames.size()];
        return emptyNames.toArray(result);
    }

    /**
     * then use Spring BeanUtils to copy and ignore null
     */
    public static void copyPropertiesIgnoreNull(Object src, Object target, String... ignoreProperties) {
        String[] ignores = getNullPropertyNames(src);
        org.springframework.beans.BeanUtils.copyProperties(src, target, (String[]) ArrayUtils.addAll(ignores, ignoreProperties));
    }

    public static <T> T copyPropertiesIgnoreNull(Object src, Supplier<T> supplier, String... ignoreProperties) {
        T target = supplier.get();
        if (src == null) {
            return target;
        }
        String[] ignores = getNullPropertyNames(src);
        org.springframework.beans.BeanUtils.copyProperties(src, target, (String[]) ArrayUtils.addAll(ignores, ignoreProperties));
        return target;
    }

    public static <T> T copyPropertiesIgnoreEmpty(Object src, Supplier<T> supplier, String... ignoreProperties) {
        T target = supplier.get();
        if (src == null) {
            return target;
        }
        String[] ignores = getEmptyPropertyNames(src);
        org.springframework.beans.BeanUtils.copyProperties(src, target, ArrayUtils.addAll(ignores, ignoreProperties));
        return target;
    }

    public static <T, R> List<T> convert(Collection<R> source, Supplier<T> supplier) {
        Objects.requireNonNull(supplier);
        if (source == null || source.isEmpty()) {
            return Collections.emptyList();
        }
        return source.stream().map(n -> BeanUtils.copyProperties(n, supplier)).collect(Collectors.toList());
    }

    public static <T, R> List<R> convert(Collection<T> source, Function<T, R> supplier) {
        Objects.requireNonNull(supplier);
        if (source == null || source.isEmpty()) {
            return Collections.emptyList();
        }
        return source.stream().map(supplier).collect(Collectors.toList());
    }
}
