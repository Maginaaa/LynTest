package com.lyn.utils;

import com.lyn.bo.SqlExecuteBO;
import lombok.extern.slf4j.Slf4j;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 * @author 简单随风
 * @date 2020/8/5
 */
@Slf4j
public class JdbcUtil {

    public static Boolean doSqlExecute(SqlExecuteBO sqlExecuteBO){

        String url = "jdbc:mysql://" + sqlExecuteBO.getUrl() + "?characterEncoding=UTF-8";
        String username = sqlExecuteBO.getUsername();
        String password = sqlExecuteBO.getPassword();

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            //  获取数据库连接
            Connection con = DriverManager.getConnection(url,username,password);
            PreparedStatement st = con.prepareStatement(sqlExecuteBO.getSql());
            log.info("执行sql语句为： \n" + sqlExecuteBO.getSql());
            int i = st.executeUpdate();
            if (i != 0) {
                return true;
            }
            con.close();
            st.close();
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }

        return false;
    }
}
