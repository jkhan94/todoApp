package com.sparta.todoapp.service;


import com.sparta.todoapp.CommonResponse;
import com.sparta.todoapp.dto.LoginRequestDto;
import com.sparta.todoapp.dto.SignupRequestDto;
import com.sparta.todoapp.entity.User;
import com.sparta.todoapp.entity.UserRoleEnum;
import com.sparta.todoapp.jwt.JwtUtil;
import com.sparta.todoapp.repository.UserRepository;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {
    // ADMIN_TOKEN
    private final String ADMIN_TOKEN = "AAABnvxRVklrnYxKZ0aHgTBcXukeZygoC";

    private final UserRepository userRepository;
    private final JwtUtil jwtUtil;

    public void signup(SignupRequestDto requestDto) {
        String nickname = requestDto.getNickname();
        String username = requestDto.getUsername();
        String password = requestDto.getPassword();

        // 회원 중복 확인
        Optional<User> checkUsername = userRepository.findByUsername(username);
        if (checkUsername.isPresent()) {
            throw new IllegalArgumentException("중복된 사용자가 존재합니다.");
        }

        // 사용자 ROLE 확인
        UserRoleEnum role = UserRoleEnum.USER;
        if (requestDto.isAdmin()) {
            if (!ADMIN_TOKEN.equals(requestDto.getAdminToken())) {
                throw new IllegalArgumentException("관리자 암호가 틀려 등록이 불가능합니다.");
            }
            role = UserRoleEnum.ADMIN;
        }
        // 값 넘어온 거 확인
        System.out.println(requestDto.getNickname());
        System.out.println(requestDto.getUsername());
        System.out.println(requestDto.getPassword());
        System.out.println(requestDto.getAdminToken());
        System.out.println(nickname + username + password + role);
        // 사용자 등록
        User user = new User(nickname, username, password, role);
        userRepository.save(user);
    }

    public void login(LoginRequestDto requestDto, HttpServletResponse res) {
        String username = requestDto.getUsername();
        String password = requestDto.getPassword();

        // 사용자 확인
        User user = userRepository.findByUsername(username).orElseThrow(
                () -> new IllegalArgumentException("등록된 사용자가 없습니다.")
        );

        // 비밀번호 확인
        if (!password.equals(user.getPassword())) {
            throw new IllegalArgumentException("비밀번호가 일치하지 않습니다.");
        }

        // JWT 생성 및 Response 객체의 헤더에 추가
        String token = jwtUtil.createToken(user.getUsername(), user.getRole());
        res.addHeader(JwtUtil.AUTHORIZATION_HEADER, token);
    }
}