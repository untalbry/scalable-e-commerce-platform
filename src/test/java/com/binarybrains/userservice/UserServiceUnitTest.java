package com.binarybrains.userservice;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import org.junit.jupiter.api.Test;
import static org.mockito.Mockito.when;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import com.binarybrains.userservice.core.buisness.input.UserService;
import com.binarybrains.userservice.core.buisness.output.UserRepository;
import com.binarybrains.userservice.core.entity.User;
import com.binarybrains.userservice.utils.ErrorCode;

@SpringBootTest
class UserServiceUnitTest {

	@Autowired
	private UserService userService;
	@MockitoBean
	private UserRepository userRepository;

	@Test
	void shouldReturnUsersWhenIdExists() {
		User user = User.builder()
						.id(1)
						.build(); 
		when(userRepository.findById(1)).thenReturn(Optional.of(user));
		var result = userService.getById(1);
		assertNotNull(result);
		assertEquals(user.getId(),result.getLeft().getId());
	}
	@Test
	void shouldReturnError404WhenIdNotExists() {
		when(userRepository.findById(2)).thenReturn(Optional.empty());
		var result = userService.getById(2);
		assertNotNull(result);
		assertEquals(ErrorCode.RN004 ,result.right().get());

	}
}