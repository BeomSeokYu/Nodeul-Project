package com.bookitaka.NodeulProject.payproc;

import com.bookitaka.NodeulProject.member.model.Member;
import com.bookitaka.NodeulProject.payment.Payment;
import com.bookitaka.NodeulProject.payment.PaymentRepository;
import com.bookitaka.NodeulProject.sheet.SheetRepository;
import com.bookitaka.NodeulProject.sheet.mysheet.Mysheet;
import com.bookitaka.NodeulProject.sheet.mysheet.MysheetRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class PayprocServiceImpl implements PayprocService {

    private final PaymentRepository paymentRepository;
    private final SheetRepository sheetRepository;
    private final MysheetRepository mysheetRepository;
    @Override
    public boolean makePay(PayMakeDto payMakeDto, Member member) {

        //결제 한번 더 확인.

        //payment 등록.
        Payment payment = new Payment();
        payment.setPaymentUuid(payMakeDto.getPaymentUuid()); //미리 받은 번호.
        payment.setPaymentPrice(payMakeDto.getPaymentPrice());
        payment.setPaymentInfo(payMakeDto.getPaymentInfo());
        payment.setMember(member);

        paymentRepository.save(payment);


        // 현재 시간
        Date nowDate = new Date();

        // 일주일 뒤의 시간
        long oneWeekInMillis = 7 * 24 * 60 * 60 * 1000; // 1주일을 밀리초로 변환
        long oneWeekLaterInMillis = System.currentTimeMillis() + oneWeekInMillis;
        Date afterWeekDate = new Date(oneWeekLaterInMillis);

        List<Long> sheetNoList = payMakeDto.getSheetNoList();
        for (Long sheetNo:sheetNoList) {
            //Mysheet에 리스트 전체를 등록 + 구매 수 업데이트
            Mysheet mysheet = new Mysheet();

            mysheet.setMember(member);
            mysheet.setMysheetMeans(payMakeDto.getSheetMeans());

            mysheet.setMysheetStartdate(nowDate);
            mysheet.setMysheetEnddate(afterWeekDate);

            mysheet.setSheet(sheetRepository.findSheetByNo(Math.toIntExact(sheetNo)).orElse(null));

            log.info("Payproc member = {}", member);
            //구매수 업데이트
            sheetRepository.plusOneSheetBuyCnt(sheetNo.intValue());

            mysheetRepository.save(mysheet);
        }

        return true;
    }
}