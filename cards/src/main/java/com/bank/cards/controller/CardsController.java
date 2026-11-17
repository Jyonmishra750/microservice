package com.bank.cards.controller;

import com.bank.cards.dto.request.UpdateCardRequestDTO;
import com.bank.cards.dto.response.ResponseDTO;
import com.bank.cards.service.CardsService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Pattern;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Tag(
        name = "CRUD REST APIs for Cards in DigiBank",
        description = "CRUD REST APIs in DigiBank to CREATE, UPDATE, FETCH AND DELETE card details"
)
@RestController
@RequestMapping("/cards")
public class CardsController {

    @Autowired
    private CardsService cardsService;

    @PostMapping("/create-card")
    public ResponseEntity<?> createCard(@Valid @RequestParam
                                                  @Pattern(regexp="(^$|[0-9]{10})",message = "Mobile number must be 10 digits")
                                                  String mobileNumber) {
        ResponseDTO response = cardsService.createCard(mobileNumber);
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }

    @GetMapping("/fetch-card")
    public ResponseEntity<?> fetchCardDetails(@RequestParam
                                                     @Pattern(regexp="(^$|[0-9]{10})",message = "Mobile number must be 10 digits")
                                                     String mobileNumber) {
        ResponseDTO response = cardsService.fetchCard(mobileNumber);
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }

    @PatchMapping("/update-card")
    public ResponseEntity<?> updateCardDetails(@Valid @RequestBody UpdateCardRequestDTO cardsDto) {
        ResponseDTO response = cardsService.updateCard(cardsDto);
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }

    @DeleteMapping("/delete")
    public ResponseEntity<?> deleteCardDetails(@RequestParam
                                                         @Pattern(regexp="(^$|[0-9]{10})",message = "Mobile number must be 10 digits")
                                                         String mobileNumber) {
        ResponseDTO response = cardsService.deleteCard(mobileNumber);
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }
}