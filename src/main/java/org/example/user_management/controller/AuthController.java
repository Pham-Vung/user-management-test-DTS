package org.example.user_management.controller;

import jakarta.persistence.EntityManager;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.example.user_management.DTO.request.LoginRequestDTO;
import org.example.user_management.DTO.request.UserRequestDTO;
import org.example.user_management.DTO.response.ApiError;
import org.example.user_management.DTO.response.ApiResponse;
import org.example.user_management.DTO.response.UserResponseDTO;
import org.example.user_management.entity.User;
import org.example.user_management.service.interfaces.IUserService;
import org.hibernate.Session;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
@AllArgsConstructor
public class AuthController {
    private final IUserService userService;
    private final EntityManager entityManager;
//    private final JwtUtil jwtUtil;

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody @Valid UserRequestDTO userDTO) {
        try {
            User user = new User();
            user.setUsername(userDTO.getUsername());
            user.setPassword(userDTO.getPassword());
            user.setEmail(userDTO.getEmail());
            user.setName(userDTO.getName());
            user.setPhone(userDTO.getPhone());

            User saved = userService.register(user);
            UserResponseDTO responseDTO = convertToDTO(saved);
            return ResponseEntity.ok(new ApiResponse<>(200, "Register successfully", responseDTO));
        } catch (IllegalArgumentException ex) {
            return ResponseEntity.badRequest().body(new ApiError(400, HttpStatus.valueOf(400).getReasonPhrase(), ex.getMessage()));
        }
    }

    private UserResponseDTO convertToDTO(User saved) {
        return new UserResponseDTO(saved.getId(), saved.getUsername(), saved.getEmail(), saved.getName(), saved.getPhone());
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody @Valid LoginRequestDTO loginRequestDTO) {
        enableSoftDeleteFilter();
        try {
            User user = userService.authenticate(loginRequestDTO.getUsername(), loginRequestDTO.getPassword());
//            String token = jwtUtil.generateToken(user.getUsername());

//            Map<String, Object> response = new HashMap<>();
//            response.put("token", token);
//            response.put("user", convertToDTO(user));
            return ResponseEntity.ok(new ApiResponse<>(200, "Login successfully", convertToDTO(user)));
        } catch (UsernameNotFoundException | BadCredentialsException ex) {
//            Map<String, String> error = new HashMap<>();
//            error.put("error", ex.getMessage());
            return ResponseEntity.status(401).body(new ApiError(401, HttpStatus.valueOf(401).getReasonPhrase(), ex.getMessage()));
        }
    }

    private void enableSoftDeleteFilter() {
        Session session = entityManager.unwrap(Session.class);
        session.enableFilter("deletedFilter").setParameter("isDeleted", Boolean.FALSE);
    }
}
