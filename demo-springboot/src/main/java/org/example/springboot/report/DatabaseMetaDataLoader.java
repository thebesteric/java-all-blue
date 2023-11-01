package org.example.springboot.report;

import java.sql.*;

/**
 * DatabaseMetaLoader
 *
 * @author wangweijun
 * @version v1.0
 * @since 2023-10-13 16:42:45
 */
public class DatabaseMetaDataLoader {

    private String driverClassName = "com.mysql.cj.jdbc.Driver";
    public static String url = "jdbc:mysql://127.0.0.1:3306/test?useUnicode=true&characterEncoding=UTF-8&userSSL=false&serverTimezone=Asia/Shanghai&allowMultiQueries=true&zeroDateTimeBehavior=convertToNull";
    private static String username = "root";
    private static String password = "P@ssw0rd";


    public static Connection getConnection() throws SQLException {
        // 连接到数据库
        return DriverManager.getConnection(url, username, password);
    }

    public static void main(String[] args) throws Exception {
        Connection connection = getConnection();
        DatabaseMetaData metaData = connection.getMetaData();
        // 获取数据库中的所有表
        ResultSet tables = metaData.getTables("test", null, null, new String[] { "TABLE" });

        // 打印表名
        while (tables.next()) {
            String tableName = tables.getString("TABLE_NAME");
            String tableComment = tables.getString("REMARKS");
            System.out.println("Table Name: " + tableName + ", Comment: " + tableComment);

            // 获取表的字段信息
            ResultSet columns = metaData.getColumns("test", null, tableName, null);
            // 打印字段信息
            while (columns.next()) {
                String columnName = columns.getString("COLUMN_NAME");
                String dataType = columns.getString("TYPE_NAME");
                int columnSize = columns.getInt("COLUMN_SIZE");
                String comment = columns.getString("REMARKS");
                System.out.println("    Column Name: " + columnName + ", Data Type: " + dataType + ", Column Size: " + columnSize + ", Comment: " + comment);
            }

            // 获取表的主键信息
            ResultSet primaryKeys = metaData.getPrimaryKeys("test", null, tableName);

            while (primaryKeys.next()) {
                String columnName = primaryKeys.getString("COLUMN_NAME");
                System.out.println("    Primary Key Column: " + columnName);
            }

            // 获取表的外键信息
            ResultSet foreignKeys = metaData.getImportedKeys("test", null, tableName);
            while (foreignKeys.next()) {
                String fkColumnName = foreignKeys.getString("FKCOLUMN_NAME");
                String pkTableName = foreignKeys.getString("PKTABLE_NAME");
                String pkColumnName = foreignKeys.getString("PKCOLUMN_NAME");
                System.out.println("    Foreign Key Column: " + fkColumnName + "， Referenced Table: " + pkTableName + "， Referenced Column: " + pkColumnName);
            }

        }
    }



}
