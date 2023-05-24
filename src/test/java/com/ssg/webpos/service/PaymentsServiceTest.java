package com.ssg.webpos.service;

import com.ssg.webpos.domain.*;
import com.ssg.webpos.domain.enums.CouponStatus;
import com.ssg.webpos.domain.enums.OrderStatus;
import com.ssg.webpos.dto.*;
import com.ssg.webpos.repository.CouponRepository;
import com.ssg.webpos.repository.PointUseHistoryRepository;
import com.ssg.webpos.repository.cart.CartRedisRepository;
import com.ssg.webpos.repository.order.OrderRepository;
import com.ssg.webpos.repository.product.ProductRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;

import javax.transaction.Transactional;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@Rollback(value = false)
public class PaymentsServiceTest {
  @Autowired
  PaymentsService paymentsService;
  @Autowired
  PointUseHistoryRepository pointUseHistoryRepository;
  @Autowired
  CartRedisRepository cartRedisRepository;
  @Autowired
  CouponRepository couponRepository;
  @Autowired
  CouponService couponService;
  @Autowired
  OrderRepository orderRepository;
  @Autowired
  ProductRepository productRepository;
  @BeforeEach
  void setup() {


  }

  @Test
  void addToCartOnly() throws Exception {
    PosStoreCompositeId posStoreCompositeId = new PosStoreCompositeId();
    posStoreCompositeId.setPos_id(2L);
    posStoreCompositeId.setStore_id(2L);
    CartAddRequestDTO requestDTO = new CartAddRequestDTO();
    requestDTO.setPosId(posStoreCompositeId.getPos_id());
    requestDTO.setStoreId(posStoreCompositeId.getStore_id());
    requestDTO.setTotalPrice(10000);

    List<CartAddDTO> cartItemList = new ArrayList<>();

    Long productId1 = 1L;
    Long productId2 = 2L;
    CartAddDTO cartAddDTO1 = new CartAddDTO();
    cartAddDTO1.setProductId(productId1);
    cartAddDTO1.setCartQty(5);
    cartItemList.add(cartAddDTO1);

    CartAddDTO cartAddDTO2 = new CartAddDTO();
    cartAddDTO2.setProductId(productId2);
    cartAddDTO2.setCartQty(5);
    cartItemList.add(cartAddDTO2);
    requestDTO.setCartItemList(cartItemList);
    cartRedisRepository.saveCart(requestDTO);

    Product product1 = productRepository.findById(productId1).get();
    Product product2 = productRepository.findById(productId2).get();
    int beforeStock1 = product1.getStock();
    int beforeStock2 = product2.getStock();

    System.out.println("beforeStock1 = " + beforeStock1);
    System.out.println("beforeStock2 = " + beforeStock2);

    PaymentsDTO paymentsDTO = new PaymentsDTO();
    paymentsDTO.setPosId(2L);
    paymentsDTO.setStoreId(2L);
    paymentsDTO.setSuccess(true);
    paymentsDTO.setName("사과");
    paymentsDTO.setPaid_amount(BigDecimal.valueOf(10000));
    paymentsDTO.setPg("kakaopay");
    paymentsService.processPaymentCallback(paymentsDTO);

    int afterStock1 = product1.getStock();
    int afterStock2 = product2.getStock();

    System.out.println("afterStock1 = " + afterStock1);
    System.out.println("afterStock2 = " + afterStock2);





  }
  @Test
  void addToCartWithCoupon() throws Exception {
    Coupon coupon = new Coupon();
    coupon.setCouponStatus(CouponStatus.NOT_USED);
    coupon.setName("500원");
    coupon.setSerialNumber("23455");
    coupon.setDeductedPrice(500);
    coupon.setExpiredDate(LocalDate.now().plusDays(7));
    couponRepository.save(coupon);

    CouponAddRequestDTO couponAddRequestDTO = new CouponAddRequestDTO();
    couponAddRequestDTO.setPosId(2L);
    couponAddRequestDTO.setStoreId(2L);
    couponAddRequestDTO.setSerialNumber(coupon.getSerialNumber());
    cartRedisRepository.saveCoupon(couponAddRequestDTO);

    Map<String, Map<String, List<Object>>> cartall = cartRedisRepository.findAll();
    System.out.println("cartall = " + cartall);
  }

  @Test
  void addToCartWithCouponAndPointUse() throws Exception {
    PointDTO pointDTO = new PointDTO();
    pointDTO.setPhoneNumber("01012345678");
    pointDTO.setPointMethod("phoneNumber");
    pointDTO.setStoreId(1L);
    pointDTO.setPosId(1L);

//    PointUseDTO pointUseDTO = new PointUseDTO();
//    pointUseDTO.setStoreId(1L);
//    pointUseDTO.setPosId(1L);
//    pointUseDTO.setAmount(10);
//    cartRedisRepository.savePointAmount(pointUseDTO);
    cartRedisRepository.savePoint(pointDTO);


    Map<String, Map<String, List<Object>>> cartall = cartRedisRepository.findAll();
    System.out.println("cartall = " + cartall);

    PaymentsDTO paymentsDTO = new PaymentsDTO();
    paymentsDTO.setPosId(1L);
    paymentsDTO.setStoreId(1L);
    paymentsDTO.setSuccess(true);
    paymentsDTO.setName("사과");
    paymentsDTO.setPaid_amount(BigDecimal.valueOf(1000));
    paymentsDTO.setPg("kakaopay");
    paymentsService.processPaymentCallback(paymentsDTO);
  }

  @Test
  void addToCartWithCouponAndPointUseAndPointSave() throws Exception {

  }

  @Test
  void paymentsFail() throws Exception {




  }


  @Test
  void paymentsPointTest() throws Exception {

  }

}
