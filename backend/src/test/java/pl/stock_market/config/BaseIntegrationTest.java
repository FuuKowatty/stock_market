package pl.stock_market.config;

import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import pl.stock_market.modules.holder.HolderFacade;
import pl.stock_market.modules.wallet.WalletFacade;

@AutoConfigureMockMvc
@SpringBootTest
@Import(TestContainersConfiguration.class)
public abstract class BaseIntegrationTest {
    @MockitoBean
    protected WalletFacade walletFacadeMock;
    @MockitoBean
    protected HolderFacade holderFacadeMock;
}
