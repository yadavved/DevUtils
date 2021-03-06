package dev.utils.common.thread;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * detail: 线程池管理类
 * @author Ttt
 */
public final class DevThreadManager {

    private DevThreadManager() {
    }

    // 默认通用线程池 - 通过 CPU 自动处理
    private static final DevThreadPool sDevThreadPool = new DevThreadPool(DevThreadPool.DevThreadPoolType.CALC_CPU);
    // 线程池数据
    private static final LinkedHashMap<String, DevThreadPool> sThreads = new LinkedHashMap<>();
    // 配置数据
    private static final Map<String, Object> sConfigs = new HashMap<>();

    /**
     * 获取 DevThreadManager 实例
     * @param threadNumber 线程数量
     * @return {@link DevThreadPool}
     */
    public static synchronized DevThreadPool getInstance(final int threadNumber) {
        // 初始化key
        String key = "n_" + threadNumber;
        // 如果不为null, 则直接返回
        DevThreadPool devThreadPool = sThreads.get(key);
        if (devThreadPool != null) {
            return devThreadPool;
        }
        devThreadPool = new DevThreadPool(threadNumber);
        sThreads.put(key, devThreadPool);
        return devThreadPool;
    }

    /**
     * 获取 DevThreadManager 实例
     * @param key 线程配置 key {@link DevThreadPool.DevThreadPoolType} or int-Integer
     * @return {@link DevThreadPool}
     */
    public static synchronized DevThreadPool getInstance(final String key) {
        // 如果不为null, 则直接返回
        DevThreadPool devThreadPool = sThreads.get(key);
        if (devThreadPool != null) {
            return devThreadPool;
        }
        Object obj = sConfigs.get(key);
        if (obj != null) {
            try {
                // 判断是否属于线程池类型
                if (obj instanceof DevThreadPool.DevThreadPoolType) {
                    devThreadPool = new DevThreadPool((DevThreadPool.DevThreadPoolType) obj);
                } else if (obj instanceof Integer) {
                    devThreadPool = new DevThreadPool((Integer) obj);
                } else { // 其他类型, 统一转换 Integer
                    devThreadPool = new DevThreadPool(Integer.parseInt((String) obj));
                }
                if (devThreadPool != null) {
                    sThreads.put(key, devThreadPool);
                    return devThreadPool;
                }
            } catch (Exception e) {
                return sDevThreadPool;
            }
        }
        return sDevThreadPool;
    }

    // =

    /**
     * 初始化配置信息
     * @param mapConfigs 线程配置信息 Map
     */
    public static void initConfig(final Map<String, Object> mapConfigs) {
        if (mapConfigs != null) {
            sConfigs.putAll(mapConfigs);
        }
    }

    /**
     * 添加配置信息
     * @param key   线程配置 key
     * @param value 线程配置 value
     */
    public static void putConfig(final String key, final Object value) {
        sConfigs.put(key, value);
    }

    /**
     * 移除配置信息
     * @param key 线程配置 key
     */
    public static void removeConfig(final String key) {
        sConfigs.remove(key);
    }
}
