package com.example.heroku.service.products;

import com.example.heroku.controller.products.request.ReviewRequest;
import com.example.heroku.entity.member.Member;
import com.example.heroku.entity.order.OrderInfo;
import com.example.heroku.entity.order.PaymentState;
import com.example.heroku.entity.products.Product;
import com.example.heroku.entity.products.Review;
import com.example.heroku.repository.member.MemberRepository;
import com.example.heroku.repository.order.OrderInfoRepository;
import com.example.heroku.repository.products.ProductsRepository;
import com.example.heroku.repository.products.ReviewRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URLDecoder;
import java.util.*;

@Slf4j
@Service
public class ReviewServiceImpl implements ReviewService{

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private ReviewRepository reviewRepository;

    @Autowired
    private ProductsRepository productsRepository;

    @Autowired
    private OrderInfoRepository orderInfoRepository;
    @Override
    public void register(ReviewRequest reviewRequest) {
        Optional<Member> maybeMember = memberRepository.findById(reviewRequest.getMemberId());
        Member member = maybeMember.get();

        Optional<OrderInfo> maybeOrderInfo = orderInfoRepository.findById(reviewRequest.getOrderId());
        OrderInfo orderInfo = maybeOrderInfo.get();

        Optional<Product> maybeProduct = productsRepository.findById(reviewRequest.getProductNo());
        Product product = maybeProduct.get();
        Review review = Review.builder()
                .member(member)
                .product(product)
                .rate(reviewRequest.getRate())
                .content(reviewRequest.getContent())
                .orderInfo(orderInfo)
                .build();

        orderInfo.setOrderState(PaymentState.WRITE_REVIEW);
        orderInfoRepository.save(orderInfo);
        reviewRepository.save(review);

    }

    @Override
    public void registerWithImg(ReviewRequest reviewRequest, MultipartFile image) {
        Optional<Member> maybeMember = memberRepository.findById(reviewRequest.getMemberId());
        Member member = maybeMember.get();

        Optional<OrderInfo> maybeOrderInfo = orderInfoRepository.findById(reviewRequest.getOrderId());
        OrderInfo orderInfo = maybeOrderInfo.get();

        Optional<Product> maybeProduct = productsRepository.findById(reviewRequest.getProductNo());
        Product product = maybeProduct.get();


        String thumbnailFileName = image.getOriginalFilename();

        try {
            log.info("requestUploadFilesWitText() - Make file: " + thumbnailFileName);

            FileOutputStream writer = new FileOutputStream(
                    "../ztz_web/src/assets/products/uploadImg/" + thumbnailFileName
            );
            FileOutputStream appWriter = new FileOutputStream(
                    "../ztz_app/assets/images/uploadImg/" + thumbnailFileName
            );

            log.info("디렉토리에 파일 배치 성공");

            writer.write(image.getBytes());
            appWriter.write(image.getBytes());
            writer.close();
            appWriter.close();
        } catch (FileNotFoundException e) {
            throw new RuntimeException((e));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }





        Review review = Review.builder()
                .member(member)
                .product(product)
                .rate(reviewRequest.getRate())
                .content(reviewRequest.getContent())
                .thumbnailFileName(thumbnailFileName)
                .orderInfo(orderInfo)
                .build();

        orderInfo.setOrderState(PaymentState.WRITE_REVIEW);
        orderInfoRepository.save(orderInfo);

        reviewRepository.save(review);
    }

    @Override
    public List<Review> productReviewRead(Long productNo) {
        return reviewRepository.findByProductNo(productNo);
    }

    @Override
    public List<Review> memberReviewRead(Long memberId) {
        return reviewRepository.findByMemberId(memberId);
    }

    @Override
    public void deleteReview(Long reviewNo) {
        Optional<Review> maybeReview = reviewRepository.findById(reviewNo);
        String fileName = maybeReview.get().getThumbnailFileName();

        if (fileName != null) {
            log.info("삭제할 파일 이름" + fileName);

            try {
                File webfile = new File("../ztz_web/src/assets/products/uploadImg/" + URLDecoder.decode(fileName, "UTF-8"));
                webfile.delete();

                File appfile = new File("../ztz_app/assets/images/uploadImg/" + URLDecoder.decode(fileName, "UTF-8"));
                appfile.delete();

            } catch (Exception e) {
                log.info("원본 파일 삭제 오류 발생");
                e.printStackTrace();
            }
        }
        reviewRepository.deleteById(reviewNo);
    }

    @Override
    public void modify(Long reviewNo, ReviewRequest reviewRequest) {
        Optional<Review> maybeReview = reviewRepository.findById(reviewNo);
        Review review = maybeReview.get();

        review.setRate(reviewRequest.getRate());
        review.setContent(reviewRequest.getContent());

        reviewRepository.save(review);
    }

    @Override
    public void modifyWithImg(Long reviewNo, ReviewRequest reviewRequest, MultipartFile image) {
        Optional<Review> maybeReview = reviewRepository.findById(reviewNo);
        Review review = maybeReview.get();

        String oldFileName = review.getThumbnailFileName();
        String newFileName = image.getOriginalFilename();

        if( oldFileName == null){
            try{
                log.info("requestUploadFilesWitText() - Make file: " +
                        newFileName);
                FileOutputStream writer = new FileOutputStream(
                        "../ztz_web/src/assets/products/uploadImg/" + newFileName
                );

                FileOutputStream appWriter = new FileOutputStream(
                        "../ztz_app/assets/images/uploadImg/" + newFileName
                );
                log.info("디렉토리에 파일 배치 성공");

                writer.write(image.getBytes());
                appWriter.write(image.getBytes());

                writer.close();
                appWriter.close();

            } catch (FileNotFoundException e) {
                throw new RuntimeException((e));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

        }else if(newFileName != oldFileName) {
            try {
                log.info("삭제할 파일 이름: " + oldFileName);
                File webfile = new File("../ztz_web/src/assets/products/uploadImg/" + URLDecoder.decode(oldFileName, "UTF-8"));
                webfile.delete();

                File appfile = new File("../ztz_app/assets/images/uploadImg/" + URLDecoder.decode(oldFileName, "UTF-8"));
                appfile.delete();


                log.info("requestUploadFilesWitText() - Make file: " +
                        newFileName);
                FileOutputStream writer = new FileOutputStream(
                        "../ztz_web/src/assets/products/uploadImg/" + newFileName
                );

                FileOutputStream appWriter = new FileOutputStream(
                        "../ztz_app/assets/images/uploadImg/" + newFileName
                );
                log.info("디렉토리에 파일 배치 성공");

                writer.write(image.getBytes());
                appWriter.write(image.getBytes());

                writer.close();
                appWriter.close();

            } catch (FileNotFoundException e) {
                throw new RuntimeException((e));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

        review.setRate(reviewRequest.getRate());
        review.setContent(reviewRequest.getContent());
        review.setThumbnailFileName(newFileName);

        reviewRepository.save(review);
    }


    @Override
    public List<Map<String, Object>> reviewAverage(Long productNo) {
        List<Review> maybeReview = reviewRepository.findByProductNo(productNo);
        if(maybeReview == null){
            return null;
        }else {
            Double average = 0.0;
            Integer reviewCnt = maybeReview.size();
            for (int i = 0; i < maybeReview.size(); i++) {
                average += maybeReview.get(i).getRate();
            }
            average = average/reviewCnt;
            if (average.isNaN()) {
                average = 0.0;
            }
            average = Double.parseDouble(String.format(Locale.KOREAN, "%.1f", average));
            List<Map<String , Object>> productReview = new ArrayList<>();
            Map<String , Object> reviewAverage = new HashMap<>();
            reviewAverage.put("average", average);
            reviewAverage.put("reviewCnt", reviewCnt);
            productReview.add(reviewAverage);

            System.out.println(reviewAverage.toString());
            return productReview;
        }
    }


}
