package com.example.courtstar.controller;

import com.example.courtstar.dto.request.ApiResponse;
import com.example.courtstar.dto.request.AuthWithdrawalOrderRequest;
import com.example.courtstar.dto.request.TransferMoneyRequest;
import com.example.courtstar.dto.response.AuthWithdrawalOrderResponse;
import com.example.courtstar.dto.response.TransferMoneyReponse;
import com.example.courtstar.entity.TransferMoney;
import com.example.courtstar.services.TransferMoneyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = {"http://localhost:3000", "https://courtstar-platform-frontend.vercel.app/"})
@RestController
@RequestMapping("/transfer-money")
public class TransferMoneyController {
    @Autowired
    TransferMoneyService transferMoneyService;
    @PostMapping("/create-withdrawal/{id}")
    public ApiResponse<TransferMoneyReponse> createTransferMoney(@PathVariable int id, @RequestBody TransferMoneyRequest request) {
        return  ApiResponse.<TransferMoneyReponse>builder()
                .data(transferMoneyService.createTransferMoney(id,request)).build();
    }
    @PostMapping("/authenticate-withdrawal-order/{id}")
    public ApiResponse<AuthWithdrawalOrderResponse> authenticateWithdrawalOrder(@PathVariable int id, @RequestBody AuthWithdrawalOrderRequest request){
        return ApiResponse.<AuthWithdrawalOrderResponse>builder()
                .data(transferMoneyService.authenticateTransferMoney(request,id))
                .build();
    }
    @GetMapping("/all")
    public ApiResponse<List<TransferMoney>> getAllWithdrawalOrder(){
        return ApiResponse.<List<TransferMoney>>builder()
                .data(transferMoneyService.getAllTransferMoney())
                .build();
    }

    @GetMapping("/getAllSuccess")
    public ApiResponse<List<TransferMoney>> getListTransferSuccess(){
        return ApiResponse.<List<TransferMoney>>builder()
                .data(transferMoneyService.getListTransferSuccess())
                .build();
    }
    @GetMapping("/getAllNotSuccess")
    public ApiResponse<List<TransferMoney>> getListTransferNotSuccess(){
        return ApiResponse.<List<TransferMoney>>builder()
                .data(transferMoneyService.getListTransferNotSuccess())
                .build();
    }
}
