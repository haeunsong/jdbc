package hello.jdbc.connection;

import lombok.extern.slf4j.Slf4j;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import static hello.jdbc.connection.ConnectionConst.*;

@Slf4j
public abstract class DBConnectionUtil {

    public static Connection getConnection() {
        try {
            // DriverManager 가 h2 관련 안에 있는 Driver 파일을 찾아서 연결해준다.
            Connection connection = DriverManager.getConnection(URL, USERNAME,PASSWORD);
            log.info("get connection={}, class={}", connection, connection.getClass());
            return connection;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }


}
