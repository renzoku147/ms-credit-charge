package com.pring.mschargecredit.service.impl;

import com.pring.mschargecredit.entity.Credit;
import com.pring.mschargecredit.repository.CreditRepository;
import com.pring.mschargecredit.service.CreditService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class CreditServiceImpl implements CreditService {
    @Autowired
    CreditRepository creditRepository;

    @Override
    public Mono<Credit> create(Credit t) {
        return creditRepository.save(t);
    }

    @Override
    public Flux<Credit> findAll() {
        return creditRepository.findAll();
    }

    @Override
    public Mono<Credit> findById(String id) {
        return creditRepository.findById(id);
    }

    @Override
    public Mono<Credit> update(Credit t) {
        return creditRepository.save(t);
    }

    @Override
    public Mono<Boolean> delete(String t) {
        return creditRepository.findById(t)
                .flatMap(credit -> creditRepository.delete(credit).then(Mono.just(Boolean.TRUE)))
                .defaultIfEmpty(Boolean.FALSE);
    }

    @Override
    public Mono<Long> findCountCreditCardId(String t) {
        return  creditRepository.findByCreditCardId(t).count();
    }

    @Override
    public Mono<Double> findTotalConsumptionCreditCardId(String t) {
        return  creditRepository.findByCreditCardId(t)
                .collectList()
                .map(credit -> credit.stream().mapToDouble(cdt -> cdt.getAmount()).sum());
    }
}
