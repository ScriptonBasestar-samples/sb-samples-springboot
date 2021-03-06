package sample.member.domain


import org.junit.Assert
import org.junit.Before
import org.junit.BeforeClass
import org.junit.Test
import org.junit.runner.RunWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.dao.DataIntegrityViolationException
import org.springframework.test.context.junit4.SpringRunner
import org.springframework.transaction.annotation.Transactional
import sample.member.domain.model.UserEntity
import sample.member.domain.repository.UserRepository

/**
 * UserEntity CRUD 테스트
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = DomainTestApplication.class)
@Transactional
class CRUDTest {

	@Autowired
	private UserRepository userRepository

	//given
	@BeforeClass
	static void 'insert entity'() {
		(1..5).each {
//			UserEntity userEntity = new UserEntity(
//					username: "username$it",
//					realname: "realname$it",
//					email: "email$it@email.com",
//					password: "password0"
//			)
			UserEntity userEntity = new UserEntity(
					"username$it",
					"realname$it",
					"email$it@email.com",
					"password0"
			)
			userRepository.save(userEntity)
		}
	}

	@Test
	void 'password bcrypt'() {
		UserEntity userEntity = userRepository.findById(1).get()
		println("=========================")
		println(userEntity)
	}

	@Test(expected = DataIntegrityViolationException.class)
	void 'insert 중복사용자 추가'() {
		//when
		UserEntity userEntity = new UserEntity(
				username: "username0",
				realname: "realname0",
				email: "email0@email.com"
		)
		userRepository.save(userEntity)
		//then exception
	}

	@Test
	void 'select 사용자 목록 출력 및 createdAt 값 확인'() {
		//when
		List<UserEntity> userList = userRepository.findAll()
		//then
		userList.each { user ->
			Assert.assertNotNull(user.username)
			Assert.assertNotNull(user.createdAt)
		}
	}

	@Test
	void 'update 닉네임 변경 - 값변경 및 lastUpdatedTime 변경확인'() {
		//when
		def userOptional = userRepository.findById(1L)
		if (!userOptional.present) {
			throw new TestFailException("사용자가 없습니다")
		}
		def user = userOptional.get()
		Date date0 = user.lastUpdatedAt
		Thread.sleep(1000L)
		user.nickname = 'nicky'
		user = userRepository.saveAndFlush(user)
		Date date1 = user.lastUpdatedAt

		//then
		println(date0)
		println(date1)
		Assert.assertTrue(date0.before(date1))
	}

	@Test
	void 'delete 탈퇴 체크'() {
		//when
		userRepository.deleteById(1L)
		def userOptional = userRepository.findById(1L)
		//then
		Assert.assertTrue(!userOptional.present)
	}

}
