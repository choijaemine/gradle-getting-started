package com.example.heroku.service.order;

import com.example.heroku.controller.order.request.AddCartRequest;
import com.example.heroku.controller.order.request.ChangeItemCountRequest;
import com.example.heroku.controller.order.request.SelectCartItemRequest;
import com.example.heroku.entity.member.Member;
import com.example.heroku.entity.order.Cart;
import com.example.heroku.entity.order.Item;
import com.example.heroku.entity.products.Product;
import com.example.heroku.repository.member.MemberRepository;
import com.example.heroku.repository.order.CartRepository;
import com.example.heroku.repository.order.ItemRepository;
import com.example.heroku.repository.products.ProductsRepository;
import com.example.heroku.service.security.RedisService;
import com.example.heroku.utility.order.validationToken;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
@Slf4j
public class CartServiceImpl implements CartService {

    @Autowired
    private CartRepository cartRepository;
    @Autowired
    ItemRepository itemRepository;
    @Autowired
    private MemberRepository memberRepository;
    @Autowired
    private ProductsRepository productsRepository;
    @Autowired
    private RedisService redisService;



    public void deleteCartItem(SelectCartItemRequest selectCartItemRequest){
        List<Long> deleteCartItemNo = selectCartItemRequest.getSelectCartItemNo();

        for (int i = 0; i <deleteCartItemNo.size() ; i++) {
            itemRepository.deleteById(deleteCartItemNo.get(i));
        }
    }
    public List<Item> returnCartItemList(String userToken){
        String returnToken = validationToken.validationToken(userToken);
        Long id = redisService.getValueByKey(returnToken);

        return itemRepository.findCartListByMemberId(id);
    }
    @Override
    public List<Item> deleteCartItem(long itemNo, String token) {
        String returnToken = validationToken.validationToken(token);
        Long id = redisService.getValueByKey(returnToken);
        itemRepository.deleteById(itemNo);

        return itemRepository.findCartListByMemberId(id);
    }
    @Override
    public String changeCartItemCount(ChangeItemCountRequest changeItemCountRequest) {
        Item item = itemRepository.findItemByItemNo(changeItemCountRequest.getItemNo());

        log.info(item.toString());
        item.setCount(changeItemCountRequest.getCount());
        item.setSelectedProductAmount(changeItemCountRequest.getSelectedProductAmount());

        itemRepository.save(item);
        return "1";
    }
    @Override
    public String addProductInCart(AddCartRequest addCartRequest) {
        String returnToken = validationToken.validationToken(addCartRequest.getToken());
        Long id = redisService.getValueByKey(returnToken);

        Optional<Cart> maybeCart = cartRepository.findByMemberId(id);

        //1. 해당 멤버의 장바구니 없는 경우 생성
        if(maybeCart.isEmpty()){
            Optional<Member> maybeMember = memberRepository.findById(id);
            Member member = maybeMember.get();

            Cart firstCart = Cart.builder()
                    .member(member)
                    .totalCount(0)
                    .build();
            cartRepository.save(firstCart);
            log.info("카트 최초 생성");
        } else {
            log.info("이미 해당 회원의 카트가 있음");
        }




        List<Item> myCartItems = itemRepository.findCartListByMemberId(id);
        Long productNo = addCartRequest.getProductNo();
        if(!myCartItems.isEmpty()) {
            log.info("상품 이미 있음");
            Item isExistItem = isExistItemCheck(addCartRequest, id);
            if(isExistItem == null) {
                Cart cart = cartRepository.findCartByMemberId(id);
                int count = addCartRequest.getCount();
                Product product = productsRepository.findProductByProductNo(productNo);

                int totalMount = count * product.getPrice();
                Item item1 = new Item(cart, product, count, totalMount);

                itemRepository.save(item1);
            } else {
                int addCount = isExistItem.getCount() + addCartRequest.getCount();
                isExistItem.setCount(addCount);
                isExistItem.setSelectedProductAmount(addCount * isExistItem.getProduct().getPrice());
                itemRepository.save(isExistItem);
            }

        } else {
            //없으면 아래처럼 저장
            Cart cart = cartRepository.findCartByMemberId(id);
            int count = addCartRequest.getCount();
            Product product = productsRepository.findProductByProductNo(productNo);

            int totalMount = count * product.getPrice();
            Item item1 = new Item(cart, product, count, totalMount);

            itemRepository.save(item1);
        }



        return "success";
    }

    @Override
    public Item isExistItemCheck(AddCartRequest addCartRequest, long id) {

        List<Item> myCartItems = itemRepository.findCartListByMemberId(id);
        Long productNo = addCartRequest.getProductNo();

        for (Item item : myCartItems) {
            log.info("내 카트 안의 상품 번호 " + item.getProduct().getProductNo() + "새로 추가할 상품 번호" + productNo);
            if(Objects.equals(item.getProduct().getProductNo(), productNo)) {
                return item;
            }
        }
        return null;
    }

}