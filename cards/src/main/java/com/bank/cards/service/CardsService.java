package com.bank.cards.service;

import com.bank.cards.dto.request.UpdateCardRequestDTO;
import com.bank.cards.dto.response.ResponseDTO;

public interface CardsService {
    ResponseDTO createCard(String mobileNumber);

    ResponseDTO fetchCard(String mobileNumber);

    ResponseDTO updateCard(UpdateCardRequestDTO cardsDto);

    ResponseDTO deleteCard(String mobileNumber);
}
