package jjk.api.api_server.feature.user.user.service;

import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import jjk.api.api_server.feature.user.user.dto.UserDto;
import jjk.api.api_server.feature.user.user.entity.QUser;
import jjk.api.api_server.feature.user.user.entity.User;
import jjk.api.api_server.feature.user.user.repository.UserRepository;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

@Service
public class UserService {

  private final UserRepository userRepository;
  private final ModelMapper modelMapper;
  private final JPAQueryFactory jpaQueryFactory;

  // QEntity
  private static final QUser qUser = QUser.user; // QUser를 클래스 필드로 선언


  public UserService(UserRepository userRepository, ModelMapper modelMapper,
      JPAQueryFactory jpaQueryFactory) {
    this.userRepository = userRepository;
    this.modelMapper = modelMapper;
    this.jpaQueryFactory = jpaQueryFactory;
  }

  // 사용자 생성
  public void createUser(UserDto userDto) {
    userRepository.save(modelMapper.map(userDto, User.class));
  }

  // 모든 사용자 조회
  public List<UserDto> getAllUsers() {
    List<User> users = jpaQueryFactory.selectFrom(qUser).fetch();
    return users.stream().map(user -> modelMapper.map(user, UserDto.class)).toList();
  }

  // ID로 사용자 조회
  public UserDto getUserById(Long id) {
    User user = jpaQueryFactory.selectFrom(qUser).where(qUser.id.eq(id)).fetchOne(); // 단일 사용자 조회

    if (user != null) {
      return modelMapper.map(user, UserDto.class);
    } else {
      return null;
    }
  }

  // 사용자 업데이트
  @Transactional
  public void updateUser(Long id, UserDto userDto) {
    // 기존 사용자 확인
    Optional<User> existingUserOpt = userRepository.findById(id);

    if (existingUserOpt.isPresent()) {
      LocalDateTime now = LocalDateTime.now();

      // 업데이트된 필드 설정
      jpaQueryFactory.update(qUser).where(qUser.id.eq(id))
          .set(qUser.username, userDto.getUsername()).set(qUser.email, userDto.getEmail())
          .set(qUser.updatedDate, now).execute();
    }
  }

  // 사용자 삭제
  @Transactional
  public boolean deleteUser(Long id) {
    // 삭제 쿼리 실행
    long deletedCount = jpaQueryFactory.delete(qUser).where(qUser.id.eq(id)).execute();

    // 삭제된 행이 있으면 true 반환, 없으면 false 반환
    return deletedCount > 0;
  }
}
