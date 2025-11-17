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
import com.BiblioStock.BiblioStock_API.service.UserService;

import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Pageable;


@Service
public class MovementService {

    private final MovementRepository movementRepository;
    private final ProductService productService;
    private final UserService userService;

    public MovementService(MovementRepository movementRepository,
            ProductService productService,
            UserService userService) {
        this.movementRepository = movementRepository;
        this.productService = productService;
        this.userService = userService;
    }

    public List<MovementResponseDTO> findAll() {
        return movementRepository.findAll()
                .stream()
                .map(MovementResponseDTO::fromEntity)
                .toList();
    }

    @Transactional
    public MovementResponseDTO create(MovementRequestDTO dto) {
        User user = userService.findByEmail("admin@livraria.com"); // Usuário fixo para teste

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

     public Page<MovementResponseDTO> getHistory(
            LocalDateTime startDate,
            LocalDateTime endDate,
            int page,
            int size
    ) {
        if (startDate == null || endDate == null) {
            throw new BusinessException("Data inicial e final são obrigatórias para o histórico de movimentações.");
        }
        if (startDate.isAfter(endDate)) {
            throw new BusinessException("A data inicial não pode ser maior que a data final.");
        }

        PageRequest pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "movementDate"));

        Page<Movement> movementPage =
                movementRepository.findByMovementDateBetween(startDate, endDate, pageable);

        return movementPage.map(MovementResponseDTO::fromEntity);
    }
}
