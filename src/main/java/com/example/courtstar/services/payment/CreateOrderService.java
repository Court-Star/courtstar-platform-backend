package com.example.courtstar.services.payment;

import com.example.courtstar.dto.request.OrderRequest;
import com.example.courtstar.util.HMACUtil;
import lombok.Data;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
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
import java.text.SimpleDateFormat;
import java.util.*;
@Data
@Service
public class CreateOrderService {
    @Value("${payment.zalopay.APP_ID}")
    private String APPID;

    @Value("${payment.zalopay.KEY1}")
    private String KEY1;

    @Value("${payment.zalopay.KEY2}")
    private String KEY2;

    @Value("${payment.zalopay.ORDER_CREATE_ENDPOINT}")
    private String ORDER_CREATE_ENDPOINT;

    private String getCurrentTimeString(String format) {

        Calendar cal = new GregorianCalendar(TimeZone.getTimeZone("GMT+7"));
        SimpleDateFormat fmt = new SimpleDateFormat(format);
        fmt.setCalendar(cal);
        return fmt.format(cal.getTimeInMillis());
    }

    public Map<String, Object> createOrder(OrderRequest orderRequest) throws IOException, JSONException {

        final Map embeddata = new HashMap(){{
            put("redirecturl", "http://localhost:3000/payment/result");//truyen url trang web muon tra ve klhi thanh toan xong
        }};

        Map<String, Object> order = new HashMap<String, Object>(){{
            put("appid", APPID);
            put("apptransid", getCurrentTimeString("yyMMdd") +"_"+ new Date().getTime());
            put("apptime", System.currentTimeMillis());
            put("appuser", "CourtStar");
            put("amount", orderRequest.getAmount());
            put("bankcode", "");
            put("item", "[]");
            put("embeddata", new JSONObject(embeddata).toString());
            put("callback_url", "http://localhost:8080/courtstar/payment/callback"); // URL callback
        }};

        order.put("description", "CourtStar - Booking Court " + order.get("apptransid"));

        String data = order.get("appid") + "|" + order.get("apptransid") + "|" + order.get("appuser") + "|" + order.get("amount")
                + "|" + order.get("apptime") + "|" + order.get("embeddata") + "|" + order.get("item");
        order.put("mac", HMACUtil.HMacHexStringEncode(HMACUtil.HMACSHA256, KEY1, data));

        CloseableHttpClient client = HttpClients.createDefault();
        HttpPost post = new HttpPost(ORDER_CREATE_ENDPOINT);

        List<NameValuePair> params = new ArrayList<>();
        for (Map.Entry<String, Object> e : order.entrySet()) {
            params.add(new BasicNameValuePair(e.getKey(), e.getValue().toString()));
        }

        post.setEntity(new UrlEncodedFormEntity(params));

        HttpResponse res = client.execute(post);
        BufferedReader rd = new BufferedReader(new InputStreamReader(res.getEntity().getContent()));
        StringBuilder resultJsonStr = new StringBuilder();
        String line;

        while ((line = rd.readLine()) != null) {
            resultJsonStr.append(line);
        }

        JSONObject jsonResult = new JSONObject(resultJsonStr.toString());
        Map<String, Object> finalResult = new HashMap<>();
        for (Iterator<String> it = jsonResult.keys(); it.hasNext(); ) {
            String key = it.next();
            finalResult.put(key, jsonResult.get(key));
        }

        return finalResult;
    }
}
