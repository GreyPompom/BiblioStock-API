package com.BiblioStock.BiblioStock_API.service;

import com.BiblioStock.BiblioStock_API.dto.AuthorRequestDTO;
import com.BiblioStock.BiblioStock_API.dto.AuthorResponseDTO;
import com.BiblioStock.BiblioStock_API.exception.BusinessException;
import com.BiblioStock.BiblioStock_API.exception.ResourceNotFoundException;
import com.BiblioStock.BiblioStock_API.model.Author;
import com.BiblioStock.BiblioStock_API.repository.AuthorRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.Set;
import java.util.stream.Collectors;

import java.util.List;

@Service
public class AuthorService {

    private final AuthorRepository repository;

    public AuthorService(AuthorRepository repository) {
        this.repository = repository;
    }

    public List<AuthorResponseDTO> findAll() {
        return repository.findAll()
                .stream()
                .map(AuthorResponseDTO::fromEntity)
                .toList();
    }

    public AuthorResponseDTO findById(Long id) {
        Author author = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Autor não encontrado com id " + id));
        return AuthorResponseDTO.fromEntity(author);
    }

    public List<AuthorResponseDTO> findAllById(Set<Long> authorIds) {
        return repository.findAllById(authorIds)
                .stream()
                .map(AuthorResponseDTO::fromEntity)
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

        return AuthorResponseDTO.fromEntity(repository.save(author));
    }

    @Transactional
    public AuthorResponseDTO update(Long id, AuthorRequestDTO dto) {
        Author author = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Autor não encontrado com id " + id));

        author.setFullName(dto.fullName());
        author.setNationality(dto.nationality());
        author.setBirthDate(dto.birthDate());
        author.setBiography(dto.biography());

        return AuthorResponseDTO.fromEntity(repository.save(author));
    }

    @Transactional
    public void delete(Long id) {
        Author author = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Autor não encontrado com id " + id));

        // Regra de negócio: não pode excluir se tiver livros associados
        boolean hasBooks = false; // aqui entraria a verificação real, ex: productRepository.existsByAuthor(author)
        if (hasBooks) {
            throw new BusinessException("Não é possível excluir um autor que possui livros vinculados.");
        }

        repository.delete(author);
    }
}
