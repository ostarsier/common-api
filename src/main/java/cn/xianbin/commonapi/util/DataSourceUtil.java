package cn.xianbin.commonapi.util;

import cn.xianbin.commonapi.entity.CommonApiDatasource;
import cn.xianbin.commonapi.enums.DataBaseTypeEnum;
import cn.xianbin.commonapi.exception.SourceException;
import cn.xianbin.commonapi.vo.JdbcSourceInfo;
import com.alibaba.druid.pool.DruidDataSource;
import com.alibaba.druid.util.StringUtils;
import lombok.extern.slf4j.Slf4j;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

@Slf4j
public class DataSourceUtil {
    private static volatile Map<String, DruidDataSource> dataSourceMap = new ConcurrentHashMap<>();
    private static volatile Map<String, Lock> dataSourceLockMap = new ConcurrentHashMap<>();
    private static final Object lockLock = new Object();

    private DataSourceUtil() {

    }

    /**
     * 获得锁
     *
     * @param key
     * @return
     */
    private static Lock getDataSourceLock(String key) {
        if (dataSourceLockMap.containsKey(key)) {
            return dataSourceLockMap.get(key);
        }
        synchronized (lockLock) {
            if (dataSourceLockMap.containsKey(key)) {
                return dataSourceLockMap.get(key);
            }
            Lock lock = new ReentrantLock();
            dataSourceLockMap.put(key, lock);
            return lock;
        }
    }

    /**
     * 获得jdbcdatasource key
     *
     * @param jdbcSourceInfo
     * @return
     */
    private static String getKey(JdbcSourceInfo jdbcSourceInfo) {
        StringBuilder sb = new StringBuilder();
        if (!StringUtils.isEmpty(jdbcSourceInfo.getUsername())) {
            sb.append(jdbcSourceInfo.getUsername());
        }
        if (!StringUtils.isEmpty(jdbcSourceInfo.getPassword())) {
            sb.append(":").append(jdbcSourceInfo.getPassword());
        }
        sb.append("@").append(jdbcSourceInfo.getJdbcUrl());
        return MD5Util.getMD5(sb.toString(), true, 64);

    }

    /**
     * 获取数据源
     *
     * @param jdbcSourceInfo
     * @return
     * @throws SourceException
     */
    public static DruidDataSource getDataSource(JdbcSourceInfo jdbcSourceInfo) throws SourceException {
        String key = getKey(jdbcSourceInfo);
        DruidDataSource druidDataSource = dataSourceMap.get(key);
        if (null != druidDataSource && !druidDataSource.isClosed()) {
            return druidDataSource;
        }
        Lock lock = getDataSourceLock(key);
        try {
            if (!lock.tryLock(5L, TimeUnit.SECONDS)) {
                throw new SourceException("jdbc无法获取实例对象: " + jdbcSourceInfo.getJdbcUrl());
            }
        } catch (InterruptedException e) {
            throw new SourceException("jdbc无法获取实例对象: " + jdbcSourceInfo.getJdbcUrl());
        }
        druidDataSource = new DruidDataSource();
        try {
            DataBaseTypeEnum dataTypeEnum = DataBaseTypeEnum.getInstance(jdbcSourceInfo.getType());
            if (null == dataTypeEnum) {
                throw new SourceException("未配置数据库驱动");
            }
            String className = dataTypeEnum.getDriver();
            try {
                Class.forName(dataTypeEnum.getDriver());
            } catch (ClassNotFoundException e) {
                throw new SourceException("无法获取数据库驱动实例");
            }
            druidDataSource.setDriverClassName(className);
            druidDataSource.setUrl(jdbcSourceInfo.getJdbcUrl());
            druidDataSource.setUsername(jdbcSourceInfo.getUsername());
            druidDataSource.setPassword(jdbcSourceInfo.getPassword());
            druidDataSource.setInitialSize(1);
            druidDataSource.setMinIdle(3);
            druidDataSource.setMaxActive(10);
            druidDataSource.setMaxWait(30000);
            druidDataSource.setTimeBetweenEvictionRunsMillis(30000);
            druidDataSource.setMinEvictableIdleTimeMillis(300000);
            druidDataSource.setTestWhileIdle(false);
            druidDataSource.setTestOnBorrow(true);
            druidDataSource.setTestOnReturn(false);
            druidDataSource.setConnectionErrorRetryAttempts(3);
            druidDataSource.setBreakAfterAcquireFailure(true);
            try {
                druidDataSource.init();
            } catch (Exception e) {
                log.error("连接池初始化失败：", e);
                throw new SourceException(e.getMessage());
            }
            dataSourceMap.put(key, druidDataSource);
        } finally {
            lock.unlock();
        }
        return druidDataSource;
    }

    /**
     * 获取JdbcSourceInfo
     *
     * @param source
     * @return
     */
    public static JdbcSourceInfo getJdbcSourceInfo(CommonApiDatasource source) {
        return JdbcSourceInfo.JdbcSourceInfoBuilder.builder().withUsername(source.getUsername()).withPassword(source.getPassword()).withJdbcUrl(source.getUrl()).withType(source.getType()).build();
    }

    /**
     * 释放数据源
     *
     * @param jdbcSourceInfo
     */
    public static void removeDatasource(JdbcSourceInfo jdbcSourceInfo) {
        String key = getKey(jdbcSourceInfo);
        Lock lock = getDataSourceLock(key);
        if (!lock.tryLock()) {
            throw new SourceException("jdbc释放实例失败: " + jdbcSourceInfo.getJdbcUrl());
        }
        try {
            DruidDataSource druidDataSource = dataSourceMap.remove(key);
            if (druidDataSource != null) {
                druidDataSource.close();
            }
            dataSourceLockMap.remove(key);
        } finally {
            lock.unlock();
        }
    }
}
