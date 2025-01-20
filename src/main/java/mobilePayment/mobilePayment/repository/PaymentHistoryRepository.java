package mobilePayment.mobilePayment.repository;

import mobilePayment.mobilePayment.models.PaymentHistory;
import mobilePayment.mobilePayment.models.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Слой репозитория для сущности платежа.
 */
@Repository
public interface PaymentHistoryRepository extends JpaRepository<PaymentHistory, Integer> {

    /**
     * Метод возвращающий платежи по пользователю и данным пагинации.
     * @param owner владелец платежа.
     * @param pageable данные пагинации.
     * @return странички/страничка с платежами.
     */
    Page<PaymentHistory> findAllByOwner(User owner, Pageable pageable);
}
