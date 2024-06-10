package com.example.courtstar.controller;

import com.example.courtstar.dto.request.*;
import com.example.courtstar.services.payment.*;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.configurationprocessor.json.JSONException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.net.URISyntaxException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Map;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/payment")
public class Payment {
    @Autowired
    private CallBackPaymentService callBackPaymentService;
    @Autowired
    private CreateOrderService service;
    @Autowired
    private RefundPaymentService refundPaymentService;
    @Autowired
    private RefundStatusPaymentService refundStatusService;
    @Autowired
    private GetStatusOrderPaymentService orderPaymentService;

    @PostMapping("/callback")
    public ResponseEntity<String> callback(@RequestBody CallBackPayment paymentDTO)
            throws JSONException, NoSuchAlgorithmException, InvalidKeyException, org.json.JSONException {

        JSONObject result = new JSONObject();
        return new ResponseEntity<>(this.callBackPaymentService
                .doCallBack(result, paymentDTO.getJsonString()).toString(), HttpStatus.OK);

    }



    @PostMapping("/create-order")
    public ResponseEntity<Map<String, Object>> createOrder(@RequestBody OrderRequest request) throws org.json.JSONException, IOException {

        Map<String, Object> resultOrder = this.service.createOrder(request);
        return new ResponseEntity<>(resultOrder, HttpStatus.OK);
    }



    @PostMapping("/order-status")
    public Map<String, Object> getStatusOrder(@RequestBody StatusRequest statusRequestDTO) throws org.json.JSONException, URISyntaxException, IOException {

        return this.orderPaymentService.statusOrder(statusRequestDTO);
    }



    @PostMapping("/refund-payment")
    public ResponseEntity<Map<String, Object>> sendRefundRequest(@RequestBody RefundRequest refundRequestDTO) throws org.json.JSONException, IOException {

        Map<String, Object> result = this.refundPaymentService.sendRefund(refundRequestDTO);
        return new ResponseEntity<>(new HashMap<>(), HttpStatus.OK);
    }


    @PostMapping("/get-refund-status")
    public ResponseEntity<Map<String, Object>> getStatusRefund(@RequestBody RefundStatusRequest refundStatusDTO) throws org.json.JSONException, IOException, URISyntaxException {

        Map<String, Object> result =  this.refundStatusService.getStatusRefund(refundStatusDTO);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }
}
