package com.pring.mschargecredit.service.impl;

import com.pring.mschargecredit.entity.Credit;
import com.pring.mschargecredit.entity.CreditCard;
import com.pring.mschargecredit.entity.CreditTransaction;
import com.pring.mschargecredit.entity.DebitCardTransaction;
import com.pring.mschargecredit.repository.CreditRepository;
import com.pring.mschargecredit.service.CreditService;

import lombok.extern.slf4j.Slf4j;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
@Slf4j
public class CreditServiceImpl implements CreditService {

    WebClient webClient = WebClient.create("http://localhost:8887/ms-creditcard/creditCard");
    
    WebClient webClientCreditPay = WebClient.create("http://localhost:8887/ms-credit-pay/creditPaid");
    
    WebClient webClientDebitCardTransaction = WebClient.create("http://localhost:8887/ms-debitcard-transaction/debitCardTransaction");

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

//    @Override
    public Mono<Double> findTotalConsumptionCreditCardId(String t) {
        return  creditRepository.findByCreditCardId(t)
                .collectList()
                .map(credit -> credit.stream().mapToDouble(cdt -> cdt.getAmount()).sum());
    }

    @Override
    public Mono<CreditCard> findCreditCard(String id) {
        return webClient.get().uri("/find/{id}", id)
                .accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .bodyToMono(CreditCard.class);
    }

	@Override
	public Flux<Credit> findByCreditCardCustomerId(String id) {
		return creditRepository.findByCreditCardCustomerId(id);
	}

	@Override
	public Mono<Boolean> verifyExpiredDebt(String idcustomer) {
		return webClientCreditPay.get().uri("/findByCreditCreditCardCustomerId/{id}", idcustomer)
		        .accept(MediaType.APPLICATION_JSON)
		        .retrieve()
		        .bodyToFlux(CreditTransaction.class) // ms-credit-pay
		        .collectList()
		        .map(creditTransaction -> creditTransaction.stream()
		        						.mapToDouble(ct->ct.getTransactionAmount()).sum()) // TOTAL PAGADO [ms-credit-pay]
		        .flatMap(amount1 -> webClientDebitCardTransaction.get().uri("/findByCreditCreditCardCustomerId/{id}", idcustomer)
						        .accept(MediaType.APPLICATION_JSON)
						        .retrieve()
						        .bodyToFlux(DebitCardTransaction.class) // ms-debitcard-transaction
						        .collectList()
						        .map(debitCardTransaction -> debitCardTransaction.stream()
		        						.mapToDouble(dct->dct.getTransactionAmount()).sum()) //TOTAL PAGADO [ms-debitcard-transaction]
						        .flatMap(amount2 -> creditRepository.findByCreditCardCustomerId(idcustomer) // ms-credit-charge
											        .collectList()
										        	.map(credit -> credit.stream()
										        				// CANTIDAD QUE DEBERIA HABERSE PAGADO EN ESTE LAPSO DE TIEMPO
								        						.mapToDouble(c -> (c.getAmount()/c.getNumberQuota())*ChronoUnit.MONTHS.between(c.getDate(),LocalDateTime.now()))
								        						.sum()
					        						)
										        	// SI LO PAGADO ES MAYOR QUE LA DEUDA
										        	.filter(totalDebt -> {
										        		log.info("Amount1 : " + amount1);
										        		log.info("Amount2 : " + amount2);
										        		log.info("TotalDebt : " + totalDebt);
										        		log.info("return : " + (totalDebt <= amount1 + amount2));
										        		return totalDebt <= amount1 + amount2;
									        		})
								        			.map(totalDebt -> true)
										        	
						        		)
				        
        		)
		        .defaultIfEmpty(Boolean.FALSE);
	}
}
