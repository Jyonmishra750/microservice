package com.bank.cards.serviceImpl;

import com.bank.cards.dto.request.UpdateCardRequestDTO;
import com.bank.cards.dto.response.AuditResponse;
import com.bank.cards.dto.response.CardsResponseDTO;
import com.bank.cards.dto.response.ResponseDTO;
import com.bank.cards.entity.Cards;
import com.bank.cards.repository.CardsRepository;
import com.bank.cards.service.CardsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.concurrent.ThreadLocalRandom;

@Service
public class CardsServiceImpl implements CardsService {
    @Autowired
    private CardsRepository cardsRepository;

    private static final String CREDIT_CARD = "Credit Card";

    public static final int NEW_CARD_LIMIT = 100_000;

    @Override
    @Transactional
    public ResponseDTO createCard(String mobileNumber) {
        Optional<Cards> existingCard = cardsRepository.findByMobileNumber(mobileNumber);
        if (existingCard.isPresent()) {
            return new ResponseDTO(
                    HttpStatus.BAD_REQUEST.value(),
                    "Card already registered with given mobileNumber: " + mobileNumber,
                    HttpStatus.BAD_REQUEST.toString()
            );
        }

        Cards newCard = createNewCard(mobileNumber);
        Cards savedCard = cardsRepository.save(newCard);
        CardsResponseDTO responseData = mapToCardsResponseDTO(savedCard);

        return new ResponseDTO(
                HttpStatus.CREATED.value(),
                responseData,
                HttpStatus.CREATED.toString()
        );
    }

    @Override
    @Transactional(readOnly = true)
    public ResponseDTO fetchCard(String mobileNumber) {
        Optional<Cards> cardOptional = cardsRepository.findByMobileNumber(mobileNumber);
        if (cardOptional.isEmpty()) {
            return new ResponseDTO(
                    HttpStatus.NOT_FOUND.value(),
                    "Card not found for mobileNumber: " + mobileNumber,
                    HttpStatus.NOT_FOUND.toString()
            );
        }

        CardsResponseDTO responseData = mapToCardsResponseDTO(cardOptional.get());
        return new ResponseDTO(
                HttpStatus.OK.value(),
                responseData,
                HttpStatus.OK.toString()
        );
    }

    @Override
    @Transactional
    public ResponseDTO updateCard(UpdateCardRequestDTO cardsDto) {
        Optional<Cards> cardOptional = cardsRepository.findByCardNumber(cardsDto.getCardNumber());
        if (cardOptional.isEmpty()) {
            return new ResponseDTO(
                    HttpStatus.NOT_FOUND.value(),
                    "Card not found for cardNumber: " + cardsDto.getCardNumber(),
                    HttpStatus.NOT_FOUND.toString()
            );
        }

        Cards card = cardOptional.get();
        mapToCards(cardsDto, card);

        Cards updatedCard = cardsRepository.save(card);

        CardsResponseDTO responseData = mapToCardsResponseDTO(updatedCard);

        return new ResponseDTO(
                HttpStatus.OK.value(),
                responseData,
                HttpStatus.OK.toString()
        );
    }

    @Override
    @Transactional
    public ResponseDTO deleteCard(String mobileNumber) {
        Optional<Cards> cardOptional = cardsRepository.findByMobileNumber(mobileNumber);
        if (cardOptional.isEmpty()) {
            return new ResponseDTO(
                    HttpStatus.NOT_FOUND.value(),
                    "Card not found for mobileNumber: " + mobileNumber,
                    HttpStatus.NOT_FOUND.toString()
            );
        }

        cardsRepository.delete(cardOptional.get());
        return new ResponseDTO(
                HttpStatus.OK.value(),
                "Card deleted successfully.",
                HttpStatus.OK.toString()
        );
    }

    private Cards createNewCard(String mobileNumber) {
        Cards newCard = new Cards();
        long randomCardNumber = 100_000_000_000L + ThreadLocalRandom.current().nextLong(900_000_000_000L);
        newCard.setCardNumber(String.valueOf(randomCardNumber));
        newCard.setMobileNumber(mobileNumber);
        newCard.setCardType(CREDIT_CARD);
        newCard.setTotalLimit(NEW_CARD_LIMIT);
        newCard.setAmountUsed(0);
        newCard.setAvailableAmount(NEW_CARD_LIMIT);
        return newCard;
    }

    private CardsResponseDTO mapToCardsResponseDTO(Cards cards) {
        AuditResponse auditResponse = new AuditResponse(
                cards.getCreatedAt() != null ? cards.getCreatedAt().atStartOfDay() : null,
                cards.getCreatedBy(),
                cards.getUpdatedAt() != null ? cards.getUpdatedAt().atStartOfDay() : null,
                cards.getUpdatedBy()
        );

        return new CardsResponseDTO(
                cards.getMobileNumber(),
                cards.getCardNumber(),
                cards.getCardType(),
                cards.getTotalLimit(),
                cards.getAmountUsed(),
                cards.getAvailableAmount(),
                auditResponse
        );
    }

    private void mapToCards(UpdateCardRequestDTO dto, Cards cards) {
        if (dto.getMobileNumber() != null && !dto.getMobileNumber().isBlank()) {
            cards.setMobileNumber(dto.getMobileNumber());
        }
        if (dto.getCardType() != null && !dto.getCardType().isBlank()) {
            cards.setCardType(dto.getCardType());
        }
        if (dto.getTotalLimit() != null) {
            cards.setTotalLimit(dto.getTotalLimit());
        }
        if (dto.getAmountUsed() != null) {
            cards.setAmountUsed(dto.getAmountUsed());
        }
        if (dto.getAvailableAmount() != null) {
            cards.setAvailableAmount(dto.getAvailableAmount());
        } else if (dto.getTotalLimit() != null || dto.getAmountUsed() != null) {
            cards.setAvailableAmount(cards.getTotalLimit() - cards.getAmountUsed());
        }
    }

}
