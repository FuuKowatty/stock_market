package pl.stock_market.modules.wallet;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Set;

@Repository
interface WalletRepository extends JpaRepository<Wallet, Long> {
    List<Wallet> findByIdIn(Set<Long> walletIds);
    boolean existsByIdAndUserId(Long id, Long userId);}
