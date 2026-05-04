package com.hammer.internal.user;

import com.hammer.internal.common.dto.PagedResponse;
import com.hammer.internal.common.exception.NotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Transactional(readOnly = true)
    public PagedResponse<UserResponse> getUsers(int page, int size, Short status) {
        PageRequest pageRequest = PageRequest.of(
                page - 1, size, Sort.by(Sort.Direction.DESC, "createdAt"));

        Page<User> result = (status != null)
                ? userRepository.findByStatus(status, pageRequest)
                : userRepository.findAll(pageRequest);

        return new PagedResponse<>(
                result.getContent().stream().map(UserResponse::from).toList(),
                page,
                size,
                result.getTotalElements(),
                result.getTotalPages());
    }

    @Transactional(readOnly = true)
    public UserResponse getUser(UUID id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("User", id));
        return UserResponse.from(user);
    }
}
