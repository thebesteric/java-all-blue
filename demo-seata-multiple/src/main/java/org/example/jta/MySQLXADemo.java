package org.example.jta;

import com.mysql.cj.jdbc.JdbcConnection;
import com.mysql.cj.jdbc.MysqlXAConnection;
import com.mysql.cj.jdbc.MysqlXid;

import javax.transaction.xa.XAException;
import javax.transaction.xa.XAResource;
import java.nio.charset.StandardCharsets;
import java.sql.*;

public class MySQLXADemo {

    private static final String DB_USERNAME = "root";
    private static final String DB_PASSWORD = "root";

    public static void main(String[] args) throws SQLException, XAException {
        // true 表示打印 XA 语句，用于调试
        boolean logXACommands = true;
        // 获取资源管理器（RM）操作接口实例
        Connection conn1 = DriverManager.getConnection("jdbc:mysql://localhost:3306/jta_user", DB_USERNAME, DB_PASSWORD);
        MysqlXAConnection xaConn1 = new MysqlXAConnection((JdbcConnection) conn1, logXACommands);
        XAResource rm1 = xaConn1.getXAResource();

        Connection conn2 = DriverManager.getConnection("jdbc:mysql://localhost:3306/jta_account", DB_USERNAME, DB_PASSWORD);
        MysqlXAConnection xaConn2 = new MysqlXAConnection((JdbcConnection) conn2, logXACommands);
        XAResource rm2 = xaConn2.getXAResource();

        // AP 请求 TM 执行一个分布式是事务，TM 生成全局事务 ID
        byte[] gTranId = "g00001".getBytes(StandardCharsets.UTF_8);
        int formatId = 1;

        MysqlXid xid1 = null;
        MysqlXid xid2 = null;
        try {
            // TM 生成 rm1 上的事务分支 ID
            byte[] bTranId1 = "b00001".getBytes(StandardCharsets.UTF_8);
            xid1 = new MysqlXid(gTranId, bTranId1, formatId);

            rm1.start(xid1, XAResource.TMNOFLAGS);
            PreparedStatement ps1 = conn1.prepareStatement("INSERT INTO user(name) VALUES ('eric')", Statement.RETURN_GENERATED_KEYS);
            ps1.execute();

            int id = 0;
            ResultSet rs = ps1.getGeneratedKeys();
            if (rs.next()) {
                id = rs.getInt(1);
            }

            rm1.end(xid1, XAResource.TMSUCCESS);

            // TM 生成 rm2 上的事务分支 ID
            byte[] bTranId2 = "b00002".getBytes(StandardCharsets.UTF_8);
            xid2 = new MysqlXid(gTranId, bTranId2, formatId);

            rm2.start(xid2, XAResource.TMNOFLAGS);
            PreparedStatement ps2 = conn2.prepareStatement("INSERT INTO account(user_id, balance) VALUES (" + id + ", 100)");
            ps2.execute();
            rm2.end(xid2, XAResource.TMSUCCESS);

            // ======== 两阶段提交（2PC）========
            // phase 1：询问所有 RM，准备提交事务分支
            int rm1Prepare = rm1.prepare(xid1);
            int rm2Prepare = rm2.prepare(xid2);
            // phase 2：提交所有事务
            boolean onePhase = false;
            if (XAResource.XA_OK == rm1Prepare && XAResource.XA_OK == rm2Prepare) {
                rm1.commit(xid1, onePhase);
                rm2.commit(xid2, onePhase);
            } else {
                // 如果有事务没有成功，则全部回滚
                rm1.rollback(xid1);
                rm2.rollback(xid2);
            }
        } catch (XAException ex) {
            rm1.rollback(xid1);
            rm2.rollback(xid2);
        }
    }
}
