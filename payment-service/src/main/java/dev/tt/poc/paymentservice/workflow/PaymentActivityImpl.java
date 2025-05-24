package dev.tt.poc.paymentservice.workflow;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import dev.tt.poc.paymentservice.model.Payment;
import dev.tt.poc.paymentservice.repositories.PaymentRepository;
import dev.tt.poc.workflow.activities.PaymentActivity;

@Slf4j
@Component
public class PaymentActivityImpl implements PaymentActivity {
    private PaymentRepository paymentRepository;

    @Autowired
    public void setPaymentRepository(PaymentRepository paymentRepository) {
        this.paymentRepository = paymentRepository;
    }

    @Override
    public void processPayment(Long orderId, double amount) {
        log.info("Payment processed for order id: {} and amount: {}", orderId, amount);
        var payment = new Payment();
        payment.setOrderId(orderId);
        payment.setAmount(amount);
        payment.setStatus("SUCCESS");
        paymentRepository.save(payment);

        log.info("Payment saved successfully for order id: {}", orderId);
    }

}
