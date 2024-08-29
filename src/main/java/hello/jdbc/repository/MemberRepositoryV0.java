package hello.jdbc.repository;

import hello.jdbc.connection.DBConnectionUtil;
import hello.jdbc.domain.Member;
import lombok.extern.slf4j.Slf4j;

import java.sql.*;
import java.util.NoSuchElementException;

/*
JDBC - DriverManager 사용
 */
@Slf4j
public class MemberRepositoryV0 {

    public Member save(Member member) throws SQLException {
        // 여기서 왜 물음표를 쓰냐?(파라미터 바인딩) 그냥 바로 sql 문에 memberId 와 money 를 쓸 수 있는데. => sql injection 을 막기 위함이다.
        String sql = "insert into member(member_id,money) values (?,?)";
        Connection con = null;
        PreparedStatement pstmt = null;

        try {
            // 연결 불러오고
            con = getConnection();
            // sql 에 넘길 데이터 준비하기
            pstmt = con.prepareStatement(sql);
            // 위에 sql 에 파라미터 바인딩을 해준다.
            pstmt.setString(1,member.getMemberId());
            pstmt.setInt(2,member.getMoney());
            pstmt.executeUpdate(); // 준비된 것들이 db 에 딱 실행이 된다.
            return member;

        } catch (SQLException e) {
            log.error("db error",e);
            throw new RuntimeException(e);
        }finally {
            close(con,pstmt,null);
        }
    }

    // 조회
    public Member findById(String memberId) throws SQLException {
        String sql = "select * from member where member_id= ?";
        Connection con = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            con = getConnection();
            pstmt = con.prepareStatement(sql);
            pstmt.setString(1,memberId);

            rs = pstmt.executeQuery();
            // 조회된 Member 객체를 받기 위해
            if(rs.next()) {
                Member member = new Member();
                member.setMemberId(rs.getString("member_id"));
                member.setMoney(rs.getInt("money"));
                return member;
            }else {
                throw new NoSuchElementException("member not found memberId=" + memberId);
            }

        }catch (SQLException e){
            log.error("db error",e);
            throw e;
        }finally {
            close(con,pstmt,rs);
        }
    }

    // 수정
    public void update(String memberId, int money) {
        String sql = "update member set money=? where member_id=?";
        Connection con = null;
        PreparedStatement pstmt = null;

        try {
            // 연결 불러오고
            con = getConnection();
            // sql 에 넘길 데이터 준비하기
            pstmt = con.prepareStatement(sql);
            // 위에 sql 에 파라미터 바인딩을 해준다.
            pstmt.setInt(1,money);
            pstmt.setString(2,memberId);
            int resultSize = pstmt.executeUpdate(); // 준비된 것들이 db 에 딱 실행이 된다.
            log.info("resultSize={}", resultSize);

        } catch (SQLException e) {
            log.error("db error",e);
            throw new RuntimeException(e);
        }finally {
            close(con,pstmt,null);
        }
    }

    // 삭제
    public void delete(String memberId) throws SQLException {
        String sql = "delete from member where member_id=?";
        Connection con = null;
        PreparedStatement pstmt = null;
        try {
            con = getConnection();
    pstmt = con.prepareStatement(sql);
         pstmt.setString(1, memberId);
         pstmt.executeUpdate();
} catch (SQLException e) {
        log.error("db error", e);
         throw e;
     } finally {
        close(con, pstmt, null);
} }
    private void close(Connection con, Statement stmt, ResultSet rs) {

        if(rs!=null) {
            try  {
                rs.close();
            }catch(SQLException e){
                log.info("error",e);
            }
        }
        if(stmt != null) {
            try {
                stmt.close();
            }catch(SQLException e) {
                log.info("error",e);
            }
        }

        if(con != null) {
            try {
                con.close();
            }catch(SQLException e) {
                log.info("error",e);
            }
        }
    }

    private Connection getConnection() {
        return DBConnectionUtil.getConnection();
    }
}
