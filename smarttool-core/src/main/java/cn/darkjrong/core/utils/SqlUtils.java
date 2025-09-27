package cn.darkjrong.core.utils;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.convert.Convert;
import cn.hutool.core.io.IoUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.ReflectUtil;
import cn.hutool.core.util.StrUtil;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.Field;
import java.sql.*;
import java.util.*;
import java.util.stream.Collectors;

/**
 * sql工具类
 *
 * @author Rong.Jia
 * @date 2024/07/03
 */
@Slf4j
@SuppressWarnings("all")
public class SqlUtils {

    public static String getSchema(String schemaName, String defaultSchema) {
        return StrUtil.isBlank(schemaName) ? defaultSchema : schemaName;
    }

    public static Boolean executeUpdate(String jdbcUrl, String sql, String rootUsername, String rootPassword) {
        if (StrUtil.isBlank(sql)) {
            log.error("**************,变更SQL执行,jdbc-url【{}】SQL为空,跳过执行", jdbcUrl);
            return Boolean.TRUE;
        }

        log.info("=============,executeUpdate(),JDBC【{}】,SQL执行【{}】====================", jdbcUrl, sql);

        Connection conn = null;
        Statement stmt = null;
        try {
            conn = DriverManager.getConnection(jdbcUrl, rootUsername, rootPassword);
            stmt = conn.createStatement();
            stmt.executeUpdate(sql);
            return Boolean.TRUE;
        } catch (SQLException e) {
            log.error(String.format("变更SQL执行异常【%s】, 【%s】", e.getMessage(), sql), e);
        } finally {
            IoUtil.close(stmt);
            IoUtil.close(conn);
        }
        return Boolean.FALSE;
    }

    public static Boolean executeUpdate(String jdbcUrl, List<String> sqls, String rootUsername, String rootPassword) {
        if (CollectionUtil.isEmpty(sqls)) {
            log.error("**************,变更多SQL执行,jdbc-url【{}】SQL为空,跳过执行", jdbcUrl);
            return Boolean.TRUE;
        }

        log.info("=============,executeUpdate(),JDBC【{}】,SQL执行【{}】====================", jdbcUrl,
                sqls.stream().collect(Collectors.joining(",")));

        Connection conn = null;
        Statement stmt = null;
        try {
            conn = DriverManager.getConnection(jdbcUrl, rootUsername, rootPassword);
            stmt = conn.createStatement();
            for (String sql : sqls) {
                stmt.executeUpdate(sql);
            }
            return Boolean.TRUE;
        } catch (SQLException e) {
            log.error(String.format("变更SQL执行异常【%s】, 【%s】", e.getMessage(), sqls.toString()), e);
        } finally {
            IoUtil.close(stmt);
            IoUtil.close(conn);
        }
        return Boolean.FALSE;
    }

    public static <T> T executeQuery(String jdbcUrl, String sql, String username, String password, String columnLabel, Class<T> tClass) {
        if (StrUtil.isEmpty(sql)) {
            log.error("**************,executeQuery(),jdbc【{}】SQL为空,跳过执行", jdbcUrl);
            if (tClass.equals(String.class)) {
                return Convert.convert(tClass, StrUtil.EMPTY);
            }
            if (tClass.equals(Integer.class)) {
                return Convert.convert(tClass, 0);
            }
            if (tClass.equals(Long.class)) {
                return Convert.convert(tClass, 0);
            }
            return null;
        }

        log.info("=============executeQuery(),jdbc【{}】SQL执行【{}】====================", jdbcUrl, sql);

        Connection conn = null;
        Statement stmt = null;
        ResultSet rs = null;
        try {
            conn = DriverManager.getConnection(jdbcUrl, username, password);
            stmt = conn.createStatement();
            rs = stmt.executeQuery(sql);
            if (rs.next()) {
                return rs.getObject(columnLabel, tClass);
            }
        } catch (SQLException e) {
            log.error(String.format("查询SQL执行异常【%s】, 【%s】", e.getMessage(), sql), e);
        } finally {
            IoUtil.close(rs);
            IoUtil.close(stmt);
            IoUtil.close(conn);
        }
        if (tClass.equals(String.class)) {
            return Convert.convert(tClass, StrUtil.EMPTY);
        }
        if (tClass.equals(Integer.class)) {
            return Convert.convert(tClass, 0);
        }
        return null;
    }

    public static Boolean executeQuery(String jdbcUrl, List<String> sqls, String username, String password) {
        if (CollectionUtil.isEmpty(sqls)) {
            log.error("**************,查询多SQL执行,jdbc-url【{}】SQL为空,跳过执行", jdbcUrl);
            return Boolean.TRUE;
        }

        log.info("=============查询多SQL执行(),JDBC【{}】,SQL执行【{}】====================", jdbcUrl,
                sqls.stream().collect(Collectors.joining(",")));

        Connection conn = null;
        Statement stmt = null;
        try {
            conn = DriverManager.getConnection(jdbcUrl, username, password);
            stmt = conn.createStatement();
            for (String sql : sqls) {
                stmt.executeQuery(sql);
            }
            return Boolean.TRUE;
        } catch (SQLException e) {
            log.error(String.format("查询SQL执行异常【%s】, 【%s】", e.getMessage(), sqls.toString()), e);
        } finally {
            IoUtil.close(stmt);
            IoUtil.close(conn);
        }
        return Boolean.FALSE;
    }

    public static <T> List<T> executeQueries(String jdbcUrl, String sql, String username, String password, String columnLabel, Class<T> tClass) {
        if (StrUtil.isEmpty(sql)) {
            log.error("**************,查询多SQL执行,jdbc-url【{}】SQL为空,跳过执行", jdbcUrl);
            return Collections.emptyList();
        }

        log.info("=============,executeQueries(),JDBC【{}】SQL执行【{}】====================", jdbcUrl, sql);

        Connection conn = null;
        Statement stmt = null;
        ResultSet rs = null;
        try {
            conn = DriverManager.getConnection(jdbcUrl, username, password);
            stmt = conn.createStatement();
            rs = stmt.executeQuery(sql);
            List<T> tList = new ArrayList<>();
            while (rs.next()) {
                tList.add(rs.getObject(columnLabel, tClass));
            }
            return tList;
        } catch (SQLException e) {
            log.error(String.format("查询SQL执行异常【%s】, 【%s】", e.getMessage(), sql), e);
        } finally {
            IoUtil.close(rs);
            IoUtil.close(stmt);
            IoUtil.close(conn);
        }
        return Collections.emptyList();
    }

    public static <T> List<T> executeQueries(String jdbcUrl, String sql, String username, String password, Class<T> tClass, String... columnLabels) {
        if (StrUtil.isEmpty(sql)) {
            log.error("**************,查询多SQL执行,jdbc-url【{}】SQL为空,跳过执行", jdbcUrl);
            return Collections.emptyList();
        }
        log.info("=============,executeQueries(),JDBC【{}】SQL执行【{}】====================", jdbcUrl, sql);
        Connection conn = null;
        Statement stmt = null;
        ResultSet rs = null;
        try {
            conn = DriverManager.getConnection(jdbcUrl, username, password);
            stmt = conn.createStatement();
            rs = stmt.executeQuery(sql);
            List<T> tList = new ArrayList<>();

            while (rs.next()) {
                T newInstance = ReflectUtil.newInstance(tClass);
                Map<String, Field> fieldMap = Arrays.stream(ReflectUtil.getFields(tClass)).collect(Collectors.toMap(Field::getName, b -> b));
                for (String columnLabel : columnLabels) {
                    Field field = fieldMap.get(StrUtil.toCamelCase(columnLabel));
                    if (ObjectUtil.isNotNull(field)) {
                        ReflectUtil.setFieldValue(newInstance, field, rs.getObject(columnLabel, field.getType()));
                    }
                }
                tList.add(newInstance);
            }
            return tList;
        } catch (SQLException e) {
            log.error(String.format("查询SQL执行异常【%s】, 【%s】", e.getMessage(), sql), e);
        } finally {
            IoUtil.close(rs);
            IoUtil.close(stmt);
            IoUtil.close(conn);
        }
        return Collections.emptyList();
    }

    public static List<Map<String, Object>> executeQueries(String jdbcUrl, String sql, String username, String password) {
        if (StrUtil.isEmpty(sql)) {
            log.error("**************,查询多SQL执行,jdbc-url【{}】SQL为空,跳过执行", jdbcUrl);
            return Collections.emptyList();
        }
        log.info("=============,executeQueries(), JDBC【{}】SQL执行【{}】====================", jdbcUrl, sql);
        Connection conn = null;
        Statement stmt = null;
        ResultSet rs = null;
        try {
            conn = DriverManager.getConnection(jdbcUrl, username, password);
            stmt = conn.createStatement();
            rs = stmt.executeQuery(sql);
            List<Map<String, Object>> records = new ArrayList<>();
            ResultSetMetaData metaData = rs.getMetaData();
            int columnCount = metaData.getColumnCount();
            while (rs.next()) {
                Map<String, Object> map = new LinkedHashMap<>();
                for (int i = 0; i < columnCount; i++) {
                    String columnName = metaData.getColumnName(i + 1);
                    Object columnValue = rs.getObject(columnName);
                    map.put(columnName, ObjectUtil.isNull(columnValue) ? StrUtil.EMPTY : columnValue);
                }
                records.add(map);
            }
            return records;
        } catch (SQLException e) {
            log.error(String.format("查询SQL执行异常【%s】, 【%s】", e.getMessage(), sql), e);
        } finally {
            IoUtil.close(rs);
            IoUtil.close(stmt);
            IoUtil.close(conn);
        }
        return Collections.emptyList();
    }

    public static Map<String, Object> executeQuery(String jdbcUrl, String sql, String username, String password) {
        if (StrUtil.isEmpty(sql)) {
            log.error("**************,查询SQL执行,jdbc-url【{}】SQL为空,跳过执行", jdbcUrl);
            return Collections.emptyMap();
        }
        log.info("=============,executeQuery(),JDBC【{}】SQL执行【{}】====================", jdbcUrl, sql);
        Connection conn = null;
        Statement stmt = null;
        ResultSet rs = null;
        try {
            conn = DriverManager.getConnection(jdbcUrl, username, password);
            stmt = conn.createStatement();
            rs = stmt.executeQuery(sql);
            Map<String, Object> records = new LinkedHashMap<>();
            ResultSetMetaData metaData = rs.getMetaData();
            int columnCount = metaData.getColumnCount();
            while (rs.next()) {
                for (int i = 0; i < columnCount; i++) {
                    String columnName = metaData.getColumnName(i + 1);
                    Object columnValue = rs.getObject(columnName);
                    records.put(columnName, ObjectUtil.isNull(columnValue) ? StrUtil.EMPTY : columnValue);
                }
            }
            return records;
        } catch (SQLException e) {
            log.error(String.format("查询SQL执行异常【%s】, 【%s】", e.getMessage(), sql), e);
        } finally {
            IoUtil.close(rs);
            IoUtil.close(stmt);
            IoUtil.close(conn);
        }
        return Collections.emptyMap();
    }


}
