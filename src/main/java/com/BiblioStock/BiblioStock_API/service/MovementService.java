package com.BiblioStock.BiblioStock_API.service;

import com.BiblioStock.BiblioStock_API.dto.MovementRequestDTO;
import com.BiblioStock.BiblioStock_API.dto.MovementResponseDTO;
import com.BiblioStock.BiblioStock_API.exception.BusinessException;
import com.BiblioStock.BiblioStock_API.exception.ResourceNotFoundException;
import com.BiblioStock.BiblioStock_API.model.Movement;
import com.BiblioStock.BiblioStock_API.model.Product;
import com.BiblioStock.BiblioStock_API.model.User;
import com.BiblioStock.BiblioStock_API.model.enums.MovementType;
import com.BiblioStock.BiblioStock_API.repository.MovementRepository;
import com.BiblioStock.BiblioStock_API.repository.ProductRepository;
import com.BiblioStock.BiblioStock_API.repository.UserRepository;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class MovementService {

    private final MovementRepository movementRepository;
    private final ProductService productService;
    private final UserRepository userRepository;

    public MovementService(MovementRepository movementRepository,
            ProductService productService,
            UserRepository userRepository) {
        this.movementRepository = movementRepository;
        this.productService = productService;
        this.userRepository = userRepository;
    }

    public List<MovementResponseDTO> findAll() {
        return movementRepository.findAll()
                .stream()
                .map(MovementResponseDTO::fromEntity)
                .toList();
    }

    @Transactional
    public MovementResponseDTO create(MovementRequestDTO dto) {
        User user = null;
        if (dto.userId() != null) {
            user = userRepository.findById(dto.userId())
                    .orElseThrow(() -> new ResourceNotFoundException("Usuário não encontrado"));
        }

        // RN020/RN021 - Atualizar estoque
        Product updatedProduct = productService.updateStock(dto.productId(), dto.quantity(), dto.movementType());

        Movement movement = Movement.builder()
                .product(updatedProduct)
                .productNameSnapshot(updatedProduct.getName())
                .quantity(dto.quantity())
                .movementType(dto.movementType())
                .note(dto.note())
                .movementDate(LocalDateTime.now())
                .user(user)
                .build();
        
        return MovementResponseDTO.fromEntity(movementRepository.save(movement));
    }
}
