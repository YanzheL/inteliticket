//package org.yanzhe.inteliticket.core.payment;
//
//import com.alipay.api.AlipayApiException;
//import com.alipay.api.AlipayClient;
//import com.alipay.api.AlipayResponse;
//import com.alipay.api.DefaultAlipayClient;
//import com.alipay.api.domain.AlipayTradeAppPayModel;
//import com.alipay.api.request.AlipayTradeAppPayRequest;
//import org.yanzhe.inteliticket.bean.AlipayConfig;
//import org.yanzhe.inteliticket.bean.QueryResultBean;
//import org.yanzhe.inteliticket.utils.UIUtils;
//
//import java.awt.*;
//import java.io.File;
//import java.io.FileOutputStream;
//import java.net.URI;
//import java.util.concurrent.TimeUnit;
//
//public class AirlineBuy {
//  private AlipayClient alipayClient =
//      new DefaultAlipayClient(
//              AlipayConfig.gatewayUrl,
//          AlipayConfig.app_id,
//              AlipayConfig.merchant_private_key,
//              AlipayConfig.format,
//              AlipayConfig.charset,
//              AlipayConfig.alipay_public_key,
//              AlipayConfig.sign_type);
//
//  public AirlineBuy() {}
//
//  protected boolean buy(QueryResultBean data) {
//    AlipayTradeAppPayRequest request = new AlipayTradeAppPayRequest();
//    AlipayTradeAppPayModel model = new AlipayTradeAppPayModel();
//    model.setBody(
//        String.format("%s --> %s,%f", data.getSrc(), data.getDst(), data.getPrice()));
//    model.setSubject("InteliTicket");
//    model.setOutTradeNo(AlipayConfig.outTradeNo);
//    model.setTimeoutExpress("30m");
//    model.setSellerId("UID2088102172157042");
//    model.setTotalAmount(String.valueOf(data.getPrice()));
//    model.setProductCode("QUICK_MSECURITY_PAY");
//    request.setBizModel(model);
//          request.setNotifyUrl("https://matriciber.com/api/payment/alipay/authRedirect");
//    try {
//      // 这里和普通的接口调用不同，使用的是sdkExecute
//      AlipayResponse response = alipayClient.pageExecute(request);
//      File page=new File("/tmp/pay.html");
//
//      try(FileOutputStream out=new FileOutputStream(page)){
//        out.write(response.getBody().getBytes());
//
//        Desktop.getDesktop().browse(page.toURI());
//      }catch (Exception e){
//        e.printStackTrace();
//      }
//
//
////      response.getParams().forEach((k,v)->System.out.println(String.format("%s = %s",k,v)));
//      System.out.println(response.getBody()); // 就是orderString 可以直接给客户端请求，无需再做处理。
//    } catch (AlipayApiException e) {
//        System.out.println(e);
//        e.printStackTrace();
////      e.printStackTrace();
//    }
//    return true;
//  }
//
//  public static void main(String[] args) {
//    AirlineBuy client = new AirlineBuy();
//    try {
//      client.buy(
//          new QueryResultBean(
//              0,
//              "b",
//              "j",
//              TimeUnit.MILLISECONDS.toMinutes(UIUtils.hourMinFM.parse("21:10").getTime()),
//              TimeUnit.MILLISECONDS.toMinutes(UIUtils.hourMinFM.parse("23:30").getTime()),
//              140,
//              500,
//              null));
//    } catch (Exception e) {
//      e.printStackTrace();
//    }
//  }
//}
