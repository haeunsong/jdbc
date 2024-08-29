package hello.jdbc.repository;

import hello.jdbc.connection.DBConnectionUtil;
import hello.jdbc.domain.Member;
import lombok.extern.slf4j.Slf4j;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

/*
JDBC - DriverManager 사용
 */
@Slf4j
public class MemberRepositoryV0 {

    public Member save(Member member) {
        String sql = "insert into member(member_id,money) values (?,?)";
        Connection con = null;
        PreparedStatement pstmt = null;

        try {
            // 연결 불러오고
            con = getConnection();
            // sql 넘겨서 정보 받아오기
            pstmt = con.prepareStatement(sql);
            // 위에 sql 에 파라미터 바인딩을 해준다.
            pstmt.setString(1,member.getMemberId());
            pstmt.setInt(2,member.getMoney());
            return member;

        } catch (SQLException e) {
            log.error("db error",e);
            throw new RuntimeException(e);
        }

    }

    private Connection getConnection() {
        return DBConnectionUtil.getConnection();
    }
}
