package com.kh.toy.member.model.service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.kh.toy.common.db.JDBCTemplate;
import com.kh.toy.common.http.HttpConnector;
import com.kh.toy.common.http.RequestParams;
import com.kh.toy.common.mail.MailSender;
import com.kh.toy.member.model.dao.MemberDao;
import com.kh.toy.member.model.dto.Member;

//Service
//어플리케이션의 비지니스 로직(핵심로직)을 작성
//사용자의 요청을 컨트롤러로 부터 위임받아 해당 요청을 처리하기 위해 필요한 핵심적인 작업을 진행
//작업을 수행하기 위해 데이터베이스에 저장된 데이터가 필요하면 Dao에게 요청
//비지니스로직을 Service가 담당하기 때문에 Transaction(논리적 최소 관리 단위)관리를 Service가 담당.

//Connection 객체 생성, close처리
//commit,rollback
//SQLException에 대한 예외처리(rollback)


public class MemberService {

	//서비스가 MemberDao를 부른다
	private MemberDao memberDao = new MemberDao();
	
	//Connection생성
	private JDBCTemplate template = JDBCTemplate.getInstance();
	
	public Member memberAuthenticate(String userId, String password) {
		Connection conn = template.getConnection();
		Member member = null;
		try {
			member = memberDao.memberAuthenticate(userId,password,conn);
		}finally {
			template.close(conn);
		}
		
		
		return member;
	}

	public Member selectMemberById(String userId) {
		Connection conn = template.getConnection();
		Member member = null; //리턴해줄 member 만들어주고
		try {
			member = memberDao.selectMemberById(userId,conn);
		}finally {
			template.close(conn);
		}
		//가져온 아이디를 멤버 다오에게 넘겨줘
		return member;
	}

	public List<Member> selectMemberList() {
		Connection conn = template.getConnection();
		List<Member> memberList = null;   
		try {
			memberList = memberDao.selectMemberList(conn);
		} finally {
			template.close(conn);
		}
		
		return memberList;
	}

	public int insertMember(Member member){
		Connection conn = template.getConnection();//Connection선언,생성
		//회원가입을 진행하고
		int res=0;
		try {
			res = memberDao.insertMember(member,conn);
			//회원가입에 성공하면 아이디로 회원의 정보를 받아와서 Controller반환
			//성공:Member member = memberDao.selectMemberById(member.getUserId(), conn);
			
			//new FileOutputStream ...-> 여기서 실패해도 rollback실행 해줘야함
			template.commit(conn);	
		} catch (Exception e) {
			template.rollback(conn);//한군데에서라도 에러가 나면 롤백
			throw e;
		}finally {
			template.close(conn);
		}
		return res;
		
	}

	public int updateMemberPassword(String userId, String password) {
		// TODO Auto-generated method stub
		int res = 0;
		Connection conn = template.getConnection();
		try {
			res =memberDao.updateMemberPassword(userId, password,conn);
		}finally {
			template.close(conn);
		}
		
		return res;
	}

	public int deleteMember(String userId) {
		// TODO Auto-generated method stub
		int res = 0;
		Connection conn = template.getConnection();
		try {
			res = memberDao.deleteMember(userId,conn);
		} finally {
			template.close(conn);
		}
		return res;
	}

	public void authenticateByEmail(Member member,String persistToken) {
	
		MailSender mailSender = new MailSender();
		HttpConnector conn = new HttpConnector();
		
		String queryString = conn.urlEncodedForm(RequestParams
											.builder()
											.param("mailTemplate", "join-auth-mail")
											.param("userId", member.getUserId())
											.param("persistToken", persistToken).build());
						
		String response = conn.get("http://localhost:9090/mail?"+queryString);
		mailSender.sendEmail(member.getEmail(), "회원가입 축하합니다.",response);
		
	}

	

}
