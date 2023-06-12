package com.bookitaka.NodeulProject.faq;

import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class FaqServiceImpl implements FaqService{

    private final FaqRepository faqRepository;

    @Override
    public void registerFaq(Faq faq) {
        faqRepository.save(faq);
    }

//    @Override
//    public List<Faq> getAllFaq() {
//        return faqRepository.findAll();
//    }

    @Override
    public Optional<Faq> getOneFaq(Long faqNo) {
        return faqRepository.findById(faqNo);
    }

    @Override
//    @Modifying(clearAutomatically = true)
    public void modifyFaq(Faq faqModified) {
        Faq faqFound = faqRepository.findById(faqModified.getFaqNo()).get();
        faqFound.setFaqQuestion(faqModified.getFaqQuestion());
        faqFound.setFaqAnswer(faqModified.getFaqAnswer());
        faqFound.setFaqCategory(faqModified.getFaqCategory());
        faqFound.setFaqBest(faqModified.getFaqBest());
        faqRepository.save(faqFound);
    }

    @Override
    public void removeFaq(Faq faq) {
        faqRepository.delete(faq);
    }

    @Override
    public long countFaq() {
        return faqRepository.count();
    }

    @Override
    public List<String> getAllFaqCategory() {
        return FaqCategory.faqAllCategory;
    }



    @Override
    public Page<Faq> getAllFaqByFaqCategory(String faqCategory, Pageable pageable) {
        if(faqCategory.equals("best")) {
            return faqRepository.findAllByFaqBestOrderByFaqRegdateDesc(1, pageable);
        } else {
            return faqRepository.findAllByFaqCategoryOrderByFaqRegdateDesc(faqCategory, pageable);
        }
    }

    @Override
    public Page<Faq> getAllFaqContaningKeyword(String keyword, Pageable pageable) {
        return faqRepository.findAllByFaqQuestionContaining(keyword, pageable);
//        return null;
    }


}