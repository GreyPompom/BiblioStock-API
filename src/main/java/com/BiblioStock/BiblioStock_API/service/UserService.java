package com.BiblioStock.BiblioStock_API.service;

import com.BiblioStock.BiblioStock_API.dto.UserRequestDTO;
import com.BiblioStock.BiblioStock_API.dto.UserResponseDTO;
import com.BiblioStock.BiblioStock_API.exception.BusinessException;
import com.BiblioStock.BiblioStock_API.exception.ResourceNotFoundException;
import com.BiblioStock.BiblioStock_API.model.User;
import com.BiblioStock.BiblioStock_API.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class UserService {

    private final UserRepository repository;

    public UserService(UserRepository repository) {
        this.repository = repository;
    }

    public List<UserResponseDTO> findAll() {
        return repository.findAll().stream()
                .map(UserResponseDTO::fromEntity)
                .toList();
    }

    public UserResponseDTO findById(Long id) {
        User user = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Usuário não encontrado"));
        return UserResponseDTO.fromEntity(user);
    }

    @Transactional
    public UserResponseDTO create(UserRequestDTO dto) {
        if (repository.existsByEmail(dto.email())) {
            throw new BusinessException("E-mail já cadastrado");
        }

        if (repository.existsByUsername(dto.username())) {
            throw new BusinessException("Nome de usuário já cadastrado");
        }

        User user = User.builder()
                .username(dto.username())
                .email(dto.email())
                .password(dto.password()) // depois pode ser criptografada
                .role(dto.role())
                .build();

        return UserResponseDTO.fromEntity(repository.save(user));
    }

    @Transactional
    public UserResponseDTO update(Long id, UserRequestDTO dto) {
        User user = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Usuário não encontrado"));

        if (!user.getEmail().equals(dto.email()) && repository.existsByEmail(dto.email())) {
            throw new BusinessException("E-mail já cadastrado");
        }

        if (!user.getUsername().equals(dto.username()) && repository.existsByUsername(dto.username())) {
            throw new BusinessException("Nome de usuário já cadastrado");
        }

        user.setUsername(dto.username());
        user.setEmail(dto.email());
        user.setPassword(dto.password());
        user.setRole(dto.role());

        return UserResponseDTO.fromEntity(repository.save(user));
    }

    @Transactional
    public void delete(Long id) {
        User user = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Usuário não encontrado"));
        repository.delete(user);
    }
}
