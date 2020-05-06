package com.example.cockroachdemo;

import static org.assertj.core.api.Assertions.assertThat;

import com.example.cockroachdemo.model.Account;
import com.example.cockroachdemo.model.BatchResults;
import com.example.cockroachdemo.service.AccountService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.util.TestPropertyValues;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.testcontainers.containers.CockroachContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

@SpringBootTest
@Testcontainers
@ContextConfiguration(initializers = {CockroachDemoApplicationTests.Initializer.class})
@ActiveProfiles("test")
class CockroachDemoApplicationTests {

    @Autowired
    private AccountService accountService;
    @Container
    private static CockroachContainer cockroachDb = new CockroachContainer();

    static class Initializer implements ApplicationContextInitializer<ConfigurableApplicationContext> {
        @Override
        public void initialize(ConfigurableApplicationContext applicationContext) {
            TestPropertyValues.of(
                "spring.datasource.driver-class-name=" + cockroachDb.getDriverClassName(),
                "spring.datasource.url=" + cockroachDb.getJdbcUrl(),
                "spring.datasource.username=" + cockroachDb.getUsername()
              ).applyTo(applicationContext.getEnvironment());
        }
    }

    @BeforeEach
    private void setup() {
        accountService.createAccountsTable();
        accountService.deleteAllAccounts();
    }

    @Test
    void contextLoads() {
        assertThat(true).isTrue();
    }

    @Test
    public void testInserts() {
        Account account1 = new Account();
        account1.setId(1);
        account1.setBalance(1000);

        Account account2 = new Account();
        account2.setId(2);
        account2.setBalance(250);
        BatchResults results = accountService.addAccounts(account1, account2);
        
        assertThat(results.getNumberOfBatches()).isEqualTo(1);
        assertThat(results.getTotalRowsAffected()).isEqualTo(2);
    }

    @Test
    public void testTransfer() {
        Account account1 = new Account();
        account1.setId(1);
        account1.setBalance(1000);

        Account account2 = new Account();
        account2.setId(2);
        account2.setBalance(250);
        BatchResults results = accountService.addAccounts(account1, account2);
        
        assertThat(results.getNumberOfBatches()).isEqualTo(1);
        assertThat(results.getTotalRowsAffected()).isEqualTo(2);

        int transferAmount = 100;
        int transferredAccounts = accountService.transferFunds(account1.getId(), account2.getId(), transferAmount);
        assertThat(transferredAccounts).isEqualTo(2);
        assertThat(accountService.getAccount(1).get().getBalance()).isEqualTo(900);
        assertThat(accountService.getAccount(2).get().getBalance()).isEqualTo(350);
    }

    @Test
    public void testMassInsertWith500() {
        BatchResults results = accountService.bulkInsertRandomAccountData(500);
        assertThat(results.getNumberOfBatches()).isEqualTo(4);
        assertThat(results.getTotalRowsAffected()).isEqualTo(500);
        assertThat(accountService.findCountOfAccounts()).isEqualTo(500);
    }

    @Test
    public void testMassInsertWith128() {
        BatchResults results = accountService.bulkInsertRandomAccountData(128);
        assertThat(results.getNumberOfBatches()).isEqualTo(1);
        assertThat(results.getTotalRowsAffected()).isEqualTo(128);
        assertThat(accountService.findCountOfAccounts()).isEqualTo(128);
    }

    @Test
    public void testMassInsertWith15() {
        BatchResults results = accountService.bulkInsertRandomAccountData(15);
        assertThat(results.getNumberOfBatches()).isEqualTo(1);
        assertThat(results.getTotalRowsAffected()).isEqualTo(15);
        assertThat(accountService.findCountOfAccounts()).isEqualTo(15);
    }
}
