package com.BiblioStock.BiblioStock_API.service;

import java.util.List;
import java.util.Set;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.BiblioStock.BiblioStock_API.dto.AuthorRequestDTO;
import com.BiblioStock.BiblioStock_API.dto.AuthorResponseDTO;
import com.BiblioStock.BiblioStock_API.exception.BusinessException;
import com.BiblioStock.BiblioStock_API.exception.ResourceNotFoundException;
import com.BiblioStock.BiblioStock_API.model.Author;
import com.BiblioStock.BiblioStock_API.repository.AuthorRepository;

@Service
public class AuthorService {

    private final AuthorRepository repository;

    public AuthorService(AuthorRepository repository) {
        this.repository = repository;
    }

    public List<AuthorResponseDTO> findAll() {
        return repository.findAll()
                .stream()
                .map(author -> {
                    Integer count = repository.countProductsByAuthor(author.getId());
                    AuthorResponseDTO base = AuthorResponseDTO.fromEntity(author);
                    return new AuthorResponseDTO(
                            base.id(),
                            base.fullName(),
                            base.nationality(),
                            base.birthDate(),
                            base.biography(),
                            count
                    );
                })
                .toList();
    }

    public AuthorResponseDTO findById(Long id) {
        Author author = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Autor não encontrado com id " + id));

        Integer count = repository.countProductsByAuthor(id);

        AuthorResponseDTO base = AuthorResponseDTO.fromEntity(author);
        return new AuthorResponseDTO(
                base.id(),
                base.fullName(),
                base.nationality(),
                base.birthDate(),
                base.biography(),
                count
        );
    }

    public List<AuthorResponseDTO> findAllById(Set<Long> authorIds) {
        return repository.findAllById(authorIds)
                .stream()
                .map(author -> {
                    Integer count = repository.countProductsByAuthor(author.getId());
                    AuthorResponseDTO base = AuthorResponseDTO.fromEntity(author);
                    return new AuthorResponseDTO(
                            base.id(),
                            base.fullName(),
                            base.nationality(),
                            base.birthDate(),
                            base.biography(),
                            count
                    );
                })
                .toList();
    }

    @Transactional
    public AuthorResponseDTO create(AuthorRequestDTO dto) {
        if (repository.existsByFullNameIgnoreCase(dto.fullName())) {
            throw new BusinessException("Já existe um autor cadastrado com este nome.");
        }

        Author author = Author.builder()
                .fullName(dto.fullName())
                .nationality(dto.nationality())
                .birthDate(dto.birthDate())
                .biography(dto.biography())
                .build();

        Author saved = repository.save(author);
        int count = 0;

        return new AuthorResponseDTO(
                saved.getId(),
                saved.getFullName(),
                saved.getNationality(),
                saved.getBirthDate(),
                saved.getBiography(),
                count
        );
    }

    @Transactional
    public AuthorResponseDTO update(Long id, AuthorRequestDTO dto) {
        Author author = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Autor não encontrado com id " + id));

        author.setFullName(dto.fullName());
        author.setNationality(dto.nationality());
        author.setBirthDate(dto.birthDate());
        author.setBiography(dto.biography());

        Integer count = repository.countProductsByAuthor(id);
        AuthorResponseDTO base = AuthorResponseDTO.fromEntity(author);
        return new AuthorResponseDTO(
                base.id(),
                base.fullName(),
                base.nationality(),
                base.birthDate(),
                base.biography(),
                count
        );
    }

    @Transactional
    public void delete(Long id) {
        Author author = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Autor não encontrado com id " + id));

        // Regra de negócio: não pode excluir se tiver livros associados
        boolean hasProducts = false; // aqui entraria a verificação real, ex: productRepository.existsByAuthor(author)
        if (hasProducts) {
            throw new BusinessException("Não é possível excluir um autor que possui livros vinculados.");
        }

        repository.delete(author);
    }
}
