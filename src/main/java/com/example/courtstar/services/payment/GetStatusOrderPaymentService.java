package com.example.courtstar.services.payment;

import com.example.courtstar.dto.request.StatusRequest;

import com.example.courtstar.util.HMACUtil;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class GetStatusOrderPaymentService {
    @Value("${payment.zalopay.APP_ID}")
    private String APP_ID;
    @Value("${payment.zalopay.KEY1}")
    private String KEY1;
    @Value("${payment.zalopay.ORDER_STATUS_ENDPOINT}")
    private String ORDER_STATUS_ENDPOINT;

    public Map<String, Object> statusOrder(StatusRequest statusRequestDTO) throws URISyntaxException, IOException, JSONException {

        String data = APP_ID +"|"+ statusRequestDTO.getAppTransId()  +"|"+ KEY1; // appid|app_trans_id|key1
        String mac = HMACUtil.HMacHexStringEncode(HMACUtil.HMACSHA256, KEY1, data);

        List<NameValuePair> params = new ArrayList<>();
        params.add(new BasicNameValuePair("appid", APP_ID));
        params.add(new BasicNameValuePair("apptransid", statusRequestDTO.getAppTransId()));
        params.add(new BasicNameValuePair("mac", mac));

        URIBuilder uri = new URIBuilder(ORDER_STATUS_ENDPOINT);
        uri.addParameters(params);

        CloseableHttpClient client = HttpClients.createDefault();
        HttpPost post = new HttpPost(uri.build());
        post.setEntity(new UrlEncodedFormEntity(params));

        CloseableHttpResponse res = client.execute(post);
        BufferedReader rd = new BufferedReader(new InputStreamReader(res.getEntity().getContent()));
        StringBuilder resultJsonStr = new StringBuilder();
        String line;

        while ((line = rd.readLine()) != null) {

            resultJsonStr.append(line);
        }

        JSONObject result = new JSONObject(resultJsonStr.toString());
        Map<String, Object> finalResult = new HashMap<>();
        finalResult.put("returncode", result.get("returncode"));
        finalResult.put("returnmessage", result.get("returnmessage"));
        finalResult.put("isprocessing", result.get("isprocessing"));
        finalResult.put("amount", result.get("amount"));
        finalResult.put("discountamount", result.get("discountamount"));
        finalResult.put("zptransid", result.get("zptransid"));
        return finalResult;
    }
}
