package org.humor.zxc.library.commons.util.utils;



import org.apache.commons.lang3.StringUtils;

import javax.servlet.http.HttpServletRequest;
import java.net.*;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.Set;

/**
 * IP工具类
 */
public class IpUtils {

    /**
     * 从请求头中获取用户IP
     */
    public static String getIp(HttpServletRequest request) {
        String ip = request.getHeader("X-Forwarded-For");
        if (StringUtils.isEmpty(ip) || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("X-Real-IP");
        }
        if (StringUtils.isEmpty(ip) || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if (StringUtils.isEmpty(ip) || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if (StringUtils.isEmpty(ip) || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("HTTP_CLIENT_IP");
        }
        if (StringUtils.isEmpty(ip) || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("HTTP_X_FORWARDED_FOR");
        }
        if (StringUtils.isEmpty(ip) || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
            if ("127.0.0.1".equals(ip)) {
                ip = getLocalIp();
            }
        }
        if (StringUtils.isNotEmpty(ip) && ip.contains(",")) {
            //阿里云等第三方服务器，会在ip后面附加一个负载均衡的ip或者代理IP
            ip = ip.substring(0, ip.indexOf(","));
        }
        return ip;
    }

    /**
     * 获取本机IP
     */
    public static String getLocalIp() {
        try {
            for (Enumeration<NetworkInterface> enumeration = NetworkInterface.getNetworkInterfaces(); enumeration.hasMoreElements(); ) {
                //遍历所有的网卡
                NetworkInterface item = enumeration.nextElement();
                for (InterfaceAddress address : item.getInterfaceAddresses()) {
                    if (item.isLoopback() || !item.isUp()) {
                        // 如果是回环和虚拟网络地址的话继续
                        continue;
                    }
                    if (address.getAddress() instanceof Inet4Address) {
                        Inet4Address inet4Address = (Inet4Address) address.getAddress();
                        //只获取ipv4地址
                        return inet4Address.getHostAddress();
                    }
                }
            }
            return InetAddress.getLocalHost().getHostAddress();
        } catch (SocketException | UnknownHostException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 适用于以下情况
     * 1. 开启了vpn
     * 2. 虚拟网卡
     * 3. dubbo 等rpc 将服务注册到了以上两个ip上了
     * 多网卡获取本机ip地址
     * @return ip地址列表
     */
    public static Set<String> getLocalV4Ips() {
        Set<String> LOCAL_IPS = new HashSet<>();
        try {
            Enumeration<NetworkInterface> allNEtInterfaces = NetworkInterface.getNetworkInterfaces();
            while (allNEtInterfaces.hasMoreElements()) {
                NetworkInterface networkInterface = allNEtInterfaces.nextElement();
                // 去除回环接口，子接口，未运行的接口
                if (networkInterface.isLoopback() || networkInterface.isVirtual() || !networkInterface.isUp()) {
                    continue;
                }
                Enumeration<InetAddress> addresses = networkInterface.getInetAddresses();
                while (addresses.hasMoreElements()) {
                    InetAddress ip = addresses.nextElement();
                    if (ip instanceof Inet4Address) {
                        LOCAL_IPS.add(ip.getHostAddress());
                    }
                }
            }
        } catch (SocketException e) {
            throw new RuntimeException(e);
        }
        return LOCAL_IPS;
    }

}
