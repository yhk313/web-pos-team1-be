package com.ssg.webpos.repository;

import com.ssg.webpos.domain.PosStoreCompositeId;
import com.ssg.webpos.dto.CartAddDTO;
import com.ssg.webpos.dto.CartAddRequestDTO;
import com.ssg.webpos.dto.PointDTO;
import com.ssg.webpos.repository.cart.CartRedisImplRepository;
import com.ssg.webpos.service.CartRedisService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@SpringBootTest
@Rollback(value = false)
public class CartRedisRepositoryTest {
  @Autowired
  CartRedisImplRepository cartRedisRepository;

  @Autowired
  UserRepository userRepository;

  @Autowired
  CartRedisService cartRedisService;

    @Test
    void readRedis() throws Exception {
      Map<String, Map<String, List<Object>>> redis = cartRedisRepository.findAll();
      System.out.println("redis = " + redis);
    }

    @Test
    void saveCartToDBTest() throws Exception {
      PosStoreCompositeId posStoreCompositeId = new PosStoreCompositeId();
      posStoreCompositeId.setPos_id(1L);
      posStoreCompositeId.setStore_id(3L);

      CartAddRequestDTO requestDTO = new CartAddRequestDTO();
      requestDTO.setPosId(posStoreCompositeId.getPos_id());
      requestDTO.setStoreId(posStoreCompositeId.getStore_id());
      requestDTO.setTotalPrice(10000);

      List<CartAddDTO> cartItemList = new ArrayList<>();

      CartAddDTO cartAddDTO1 = new CartAddDTO();
      cartAddDTO1.setProductId(5L);
      cartAddDTO1.setCartQty(5);
      cartItemList.add(cartAddDTO1);

      CartAddDTO cartAddDTO2 = new CartAddDTO();
      cartAddDTO2.setProductId(2L);
      cartAddDTO2.setCartQty(5);
      cartItemList.add(cartAddDTO2);

      requestDTO.setCartItemList(cartItemList);

      cartRedisRepository.saveCart(requestDTO);

      Map<String, Map<String, List<Object>>> all = cartRedisRepository.findAll();

      System.out.println("all = " + all);
  
      String compositeId = posStoreCompositeId.getStore_id() + "-" + posStoreCompositeId.getPos_id();
      System.out.println("compositeId = " + compositeId);
      cartRedisService.saveCartToDB(compositeId);
      

    }
    
    @Test
    void readPhoneNumber() throws Exception {
      PointDTO pointDTO = new PointDTO();
      pointDTO.setPhoneNumber("01011113333");
      pointDTO.setPointMethod("phoneNumber");
      pointDTO.setStoreId(3L);
      pointDTO.setPosId(1L);
      cartRedisRepository.savePoint(pointDTO);

      Map<String, Map<String, List<Object>>> all = cartRedisRepository.findAll();
      List<String> phoneNumbersByCompositeId = cartRedisRepository.findPhoneNumbersByCompositeId(pointDTO.getStoreId() + "-" + pointDTO.getPosId());
      System.out.println("phoneNumbersByCompositeId = " + phoneNumbersByCompositeId);

      System.out.println("all = " + all);

    }

  @Test
  void findUserId() throws Exception {
    PointDTO pointDTO = new PointDTO();
    pointDTO.setPhoneNumber("01011113333");
    pointDTO.setPointMethod("phoneNumber");
    pointDTO.setStoreId(3L);
    pointDTO.setPosId(1L);
    cartRedisRepository.savePoint(pointDTO);

    Map<String, Map<String, List<Object>>> all = cartRedisRepository.findAll();
    Long userId = cartRedisRepository.findUserId(pointDTO.getStoreId() + "-" + pointDTO.getPosId());
    System.out.println("userId = " + userId);

    System.out.println("all = " + all);

  }

    @Test
    @DisplayName("카트 redis 저장")
    public void readCartInfoFromRedisWithPosId () throws Exception {
      PosStoreCompositeId posStoreCompositeId = new PosStoreCompositeId();
      posStoreCompositeId.setPos_id(1L);
      posStoreCompositeId.setStore_id(3L);

      CartAddRequestDTO requestDTO = new CartAddRequestDTO();
      requestDTO.setPosId(posStoreCompositeId.getPos_id());
      requestDTO.setStoreId(posStoreCompositeId.getStore_id());
      requestDTO.setTotalPrice(10000);

      List<CartAddDTO> cartItemList = new ArrayList<>();

      CartAddDTO cartAddDTO1 = new CartAddDTO();
      cartAddDTO1.setProductId(5L);
      cartAddDTO1.setCartQty(5);
      cartItemList.add(cartAddDTO1);

      CartAddDTO cartAddDTO2 = new CartAddDTO();
      cartAddDTO2.setProductId(2L);
      cartAddDTO2.setCartQty(5);
      cartItemList.add(cartAddDTO2);

      requestDTO.setCartItemList(cartItemList);

      cartRedisRepository.saveCart(requestDTO);

      Map<String, Map<String, List<Object>>> cartall = cartRedisRepository.findAll();
      System.out.println("cartall = " + cartall);

    }

    @Test
    @DisplayName("포인트 redis 저장")
    public void readPointInfoFromRedisAll () throws Exception {

      PointDTO pointDTO = new PointDTO();
      pointDTO.setPhoneNumber("01011112222");
      pointDTO.setPointMethod("phoneNumber");
      pointDTO.setStoreId(3L);
      pointDTO.setPosId(1L);
      cartRedisRepository.savePoint(pointDTO);

      Map<String, Map<String, List<Object>>> all = cartRedisRepository.findAll();
      System.out.println("all = " + all);
//      Map<String, List<Object>> byId = cartRedisRepository.findById(String.valueOf(pointDTO.getPosStoreCompositeId()));
//
//      Assertions.assertEquals(2, byId.size());


    }


  @Test
  @DisplayName("해당 posId 삭제")
  public void deleteFromRedisWithPosId() throws Exception {
    PosStoreCompositeId posStoreCompositeId = new PosStoreCompositeId();
    posStoreCompositeId.setPos_id(1L);
    posStoreCompositeId.setStore_id(3L);

    CartAddRequestDTO requestDTO = new CartAddRequestDTO();
    requestDTO.setPosId(posStoreCompositeId.getPos_id());
    requestDTO.setStoreId(posStoreCompositeId.getStore_id());
    requestDTO.setTotalPrice(10000);

    List<CartAddDTO> cartItemList = new ArrayList<>();

    CartAddDTO cartAddDTO1 = new CartAddDTO();
    cartAddDTO1.setProductId(5L);
    cartAddDTO1.setCartQty(5);
    cartItemList.add(cartAddDTO1);
    requestDTO.setCartItemList(cartItemList);

    cartRedisRepository.saveCart(requestDTO);

    String compositeId = posStoreCompositeId.getPos_id() + "-" + posStoreCompositeId.getStore_id();

    // when
    cartRedisRepository.delete(compositeId);

    Map<String, Map<String, List<Object>>> allAfterDeletion = cartRedisRepository.findAll();
    System.out.println("allAfterDeletion = " + allAfterDeletion);

    // then
    Assertions.assertFalse(allAfterDeletion.containsKey(compositeId));
  }



  @Test
  void deleteAll() throws Exception {
    cartRedisRepository.deleteAll();

    Map<String, Map<String, List<Object>>> allAfterDeletion = cartRedisRepository.findAll();
    System.out.println("After deletion: " + allAfterDeletion);

    Assertions.assertTrue(allAfterDeletion.isEmpty());
  }
}

