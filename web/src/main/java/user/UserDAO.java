package user;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.sql.DataSource;

public class UserDAO {
	DataSource dataSource;
	//Connection conn = null;
	//PreparedStatement pstmt = null;
	
	public UserDAO() {
		try {
			InitialContext initContext = new InitialContext();
			Context envContext = (Context) initContext.lookup("java:/comp/env");
			dataSource = (DataSource) envContext.lookup("jdbc/web");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	//로그인 함수
	public int login(String userID, String userPW) {
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String SQL = "SELECT * FROM user WHERE userID = ?";
		try {
			conn = dataSource.getConnection();
			pstmt = conn.prepareStatement(SQL);
			pstmt.setString(1, userID);
			rs = pstmt.executeQuery();
			if (rs.next()) {
				if(rs.getString("userPW").equals(userPW)) {
					return 1; //로그인 성공
				}
				return 2; //비밀번호 틀림
			} else {
				return 0; // 해당 사용자가 존재하지 않음
			}
		} catch (Exception e) {
			e.printStackTrace();
			
		} finally {
			try {
				if(rs != null) rs.close();
				if(pstmt != null) pstmt.close();
				if(conn != null) conn.close();
			} catch(Exception e) {
				e.printStackTrace();
			}
		}
		return -1; // 데이터베이스 오류
	}
	
	public int registerCheck(String userID) {
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String SQL = "SELECT * FROM user WHERE userID = ?";
		try {
			conn = dataSource.getConnection();
			pstmt = conn.prepareStatement(SQL);
			pstmt.setString(1, userID);
			rs = pstmt.executeQuery();
			if (rs.next() || userID.equals("")) {
				return 0; //이미 존재하는 회원
			} else {
				return 1; // 가입 가능한 회원 아이디
			}
		} catch (Exception e) {
			e.printStackTrace();
			
		} finally {
			try {
				if(rs != null) rs.close();
				if(pstmt != null) pstmt.close();
				if(conn != null) conn.close();
			} catch(Exception e) {
				e.printStackTrace();
			}
		}
		return -1; // 데이터베이스 오류
	}
	public int register(String userID, String userPW, String userPWC, String userName, String userBirthYear, String userBirthMonth, String userBirthDate, String userEmail, String userGender, String userProfile ) {
		Connection conn = null;
		PreparedStatement pstmt = null;
		String SQL = "INSERT INTO user VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
		try {
			conn = dataSource.getConnection();
			pstmt = conn.prepareStatement(SQL);
			pstmt.setString(1, userID);
			pstmt.setString(2, userPW);
			pstmt.setString(3, userPWC);
			pstmt.setString(4, userName);
			pstmt.setInt(5, Integer.parseInt(userBirthYear));
			pstmt.setInt(6, Integer.parseInt(userBirthMonth));
			pstmt.setInt(7, Integer.parseInt(userBirthDate));
			pstmt.setString(8, userEmail);
			pstmt.setString(9, userGender);
			pstmt.setString(10, userProfile);
			return pstmt.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if(pstmt != null) pstmt.close();
				if(conn != null) conn.close();
			} catch(Exception e) {
				e.printStackTrace();
			}
		}
		return -1; // 데이터베이스 오류
	}
	
	public UserDTO getUser(String userID) {
		UserDTO user = new UserDTO();
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String SQL = "SELECT * FROM user WHERE userID = ?";
		try {
			conn = dataSource.getConnection();
			pstmt = conn.prepareStatement(SQL);
			pstmt.setString(1, userID);
			rs = pstmt.executeQuery();
			if (rs.next()) {
				user.setUserID(userID);
				user.setUserPW(rs.getString("userPW"));
				user.setUserPWC(rs.getString("userPWC"));
				user.setUserName(rs.getString("userName"));
				user.setUserBirthYear(rs.getInt("userBirthYear"));
				user.setUserBirthMonth(rs.getInt("userBirthMonth"));
				user.setUserBirthDate(rs.getInt("userBirthDate"));
				user.setUserEmail(rs.getString("userEmail"));
				user.setUserGender(rs.getString("userGender"));
				user.setUserProfile(rs.getString("userProfile"));
			}
		} catch (Exception e) {
			e.printStackTrace();
			
		} finally {
			try {
				if(rs != null) rs.close();
				if(pstmt != null) pstmt.close();
				if(conn != null) conn.close();
			} catch(Exception e) {
				e.printStackTrace();
			}
		}
		return user;
	}
	
	public int update(String userID, String userPW, String userPWC, String userName, String userBirthYear, String userBirthMonth, String userBirthDate, String userEmail, String userGender) {
		Connection conn = null;
		PreparedStatement pstmt = null;
		String SQL = "UPDATE user SET userPW = ?, userPWC = ?, userName = ?, userBirthYear = ?, userBirthMonth = ?, userBirthDate = ?, userEmail = ?, userGender = ? WHERE userID = ?";
		try {
			conn = dataSource.getConnection();
			pstmt = conn.prepareStatement(SQL);
			pstmt.setString(1, userPW);
			pstmt.setString(2, userPWC);
			pstmt.setString(3, userName);
			pstmt.setInt(4, Integer.parseInt(userBirthYear));
			pstmt.setInt(5, Integer.parseInt(userBirthMonth));
			pstmt.setInt(6, Integer.parseInt(userBirthDate));
			pstmt.setString(7, userEmail);
			pstmt.setString(8, userGender);
			pstmt.setString(9, userID);
			return pstmt.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
			
		} finally {
			try {
				if(pstmt != null) pstmt.close();
				if(conn != null) conn.close();
			} catch(Exception e) {
				e.printStackTrace();
			}
		}
		return -1; // 데이터베이스 오류
	}
	
	public int profile(String userID, String userProfile) {
		Connection conn = null;
		PreparedStatement pstmt = null;
		String SQL = "UPDATE user SET userProfile = ? WHERE userID = ?";
		try {
			conn = dataSource.getConnection();
			pstmt = conn.prepareStatement(SQL);
			pstmt.setString(1, userProfile);
			pstmt.setString(2, userID);
			return pstmt.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if(pstmt != null) pstmt.close();
				if(conn != null) conn.close();
			} catch(Exception e) {
				e.printStackTrace();
			}
		}
		return -1; // 데이터베이스 오류
	}

	public String getProfile(String userID) {
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String SQL = "SELECT userProfile FROM user WHERE userID = ?";
		try {
			conn = dataSource.getConnection();
			pstmt = conn.prepareStatement(SQL);
			pstmt.setString(1, userID);
			rs = pstmt.executeQuery();
			if (rs.next()) {
				if(rs.getString("userProfile").equals("")) {
					return "http://localhost:18080/web/images/icon.png";
				}
				return "http://localhost:18080/web/upload" + rs.getString("userProfile");
			} 
		} catch (Exception e) {
			e.printStackTrace();
			
		} finally {
			try {
				if(rs != null) rs.close();
				if(pstmt != null) pstmt.close();
				if(conn != null) conn.close();
			} catch(Exception e) {
				e.printStackTrace();
			}
		}
		return "http://localhost:18080/web/images/icon.png";
	}
}
	
