package com.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.sql.DataSource;

public class BoardDAO {
	DataSource dataFactory;

	public BoardDAO() { // 생성자
		// DataSource 얻기, 커넥션 풀 사용
		try {
			Context ctx = new InitialContext();
			dataFactory = (DataSource) ctx.lookup("java:comp/env/jdbc/Oracle11g");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}// end 생성자
		// 목록보기

	public ArrayList<BoardDTO> list() {
		ArrayList<BoardDTO> list = new ArrayList<BoardDTO>();
		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			con = dataFactory.getConnection();
			String query = "SELECT num, author, title, content, to_char(writeday,'YYYY/MM/DD') writeday,"
					+ "readcnt, repRoot, repStep, repIndent, repIndent FROM board"
					+ "order by repRoot desc, repStep asc";
			pstmt = con.prepareStatement(query);
			rs = pstmt.executeQuery();
			while (rs.next()) {
				BoardDTO data = new BoradDTO();
				data.setNum(rs.getInt("num"));
				data.setAuthor(rs.getString("author"));
				data.setTitle(rs.getString("title"));
				data.setContent(rs.getString("content"));
				data.setWriteday(rs.getString("writeday"));
				data.setReadcnt(rs.getInt("readcnt"));
				data.setRepRoot(rs.getInt("repRoot"));
				data.setRepStep(rs.getInt("repStep"));
				data.setRepIndent(rs.getInt("repIndent"));

				list.add(data);
			} // end while
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (rs != null)
					rs.close();
				if (pstmt != null)
					pstmt.close();
				if (con != null)
					con.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return list;
	}// end select
		// 글쓰기

	public void write(BoardDTO dto) {
		Connection con = null;
		PreparedStatement pstmt = null;
		try {
			con = dataFactory.getConnection();
			StringBuffer query = new StringBuffer();
			query.append("INSERT INTO board(num, title, author, content,");
			query.append(" repRoot, repStep, repIndent, passwd) values");
			query.append("(board_seq.nextval, ?, ?, ?, board_seq.currval, 0, 0, ?)");
			pstmt = con.prepareStatement(query.toString());
			pstmt.setString(1, dto.getTitle());
			pstmt.setString(2, dto.getAuthor());
			pstmt.setString(3, dto.getContent());
			pstmt.setString(4, dto.getPasswd());
			int n = pstmt.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (pstmt != null)
					pstmt.close();
				if (con != null)
					con.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		} // end finally
	}// end write
	public void makeReply{int_root, int_step){
			Connection con = null;
			PreparedStatement pstmt = null;
			try {
					con = dataFactory.getConnection();
					StringBuffer query = new StringBuffer();
					query.append("UPDATE board SET repStep = repStep + 1");
					query.append("WHERE repRoot = ? AND repStep > ? ");
					pstmt = con.prepareStatement(query.toString());
					pstmt.setInt(1, _root);
					pstmt.setInt(2, _step);
					pstmt.executeUpdate();
			} catch(Exception e) {
					e.printStackTrace();
			} finally {
					try {
							if(pstmt!=null) pstmt.close();
							if(con!=null) con.close();
					} catch(SQLException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
					}
			}
	}
	// 답변 달기
	public void reply(BoardDTO dto) {
			makeReply(dto.getRepRoot(), dto.getRepStep());
			Connection con = null;
			PreparedStatement pstmt = null;
			try {
					con = dataFactory.getConnection();
					StringBuffer query = new StringBuffer(); 
					query.append("INSERT INTO board(num, title, author, ");
					query.append("content, repRoot, repStep, repIndent, passwd)");
					query.append("values( board_seq.nextVal, ?,?,?,?,?,?,?)");
					pstmt = con.prepareStatement(query.toString());
					pstmt.setString(1, dto.getTitle());
					pstmt.setString(2, dto.getAuthor());
					pstmt.setString(3, dto.getContent());
					pstmt.setInt(4, dto.getRepRoot());
					pstmt.setInt(5, dto.getRepStep() + 1);
					pstmt.setInt(6, dto.getRepIndent() + 1);
					pstmt.setString(7, dto.getPasswd());
					pstmt.executeUpdate();
			} catch(Exception e) {
					e.printStackTrace();
			} finally {
						try {
								if(pstmt!=null)pstmt.close();
								if(con!=null)con.close();
						} catch (SQLException e) {
								//TODO Auto-generated catch block
								e.printStackTrace();
								
						}
			}
	} //end reply
}// end class
