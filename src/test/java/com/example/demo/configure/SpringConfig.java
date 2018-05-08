package com.example.demo.configure;

import org.mockito.Mockito;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.oauth2.resource.ResourceServerProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.test.context.TestPropertySource;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author : cabbage
 */
@Configuration
@ComponentScan(
    basePackages = "com.example.demo",
    excludeFilters = {
        @ComponentScan.Filter(
            classes = {
                //EnableDiscoveryClient.class,
                Transactional.class,
                SpringBootApplication.class})
    })
@TestPropertySource(properties = {"spring.cloud.discovery.enabled = false"})
public class SpringConfig {

    @Bean
    public ResourceServerProperties ResourceServerProperties() {
        return new ResourceServerProperties();
    }

    @Bean
    public JedisConnectionFactory jedisConnectionFactory() {
        return Mockito.mock(JedisConnectionFactory.class);
    }

   /* @Bean
    public AdminSettingsClient adminSettingsClient() {

        AdminSettingsClient client = Mockito.mock(AdminSettingsClient.class);

        AdminSettingsDto adminSettingsDto = new AdminSettingsDto();
        adminSettingsDto.setAdminId(1L);
        adminSettingsDto.setSettingsId("1");
        adminSettingsDto.setIsDefault("Y");

        Mockito.when(client.getDefaultAdminSettingsByAdminId(Matchers.anyLong())).thenReturn(adminSettingsDto);

        InitSettingsResultDto initSettingsResultDto = new InitSettingsResultDto();
        initSettingsResultDto.setPaymentId("aaaa");
        initSettingsResultDto.setBenefitPromotionId("bbb");
        initSettingsResultDto.setRegPromotionId("ccc");

        Mockito.when(client.initAdminSettings(Matchers.anyLong())).thenReturn(initSettingsResultDto);

        return client;
    }

    @Bean
    public MoneyOperationClient moneyOperationClient() {

        MoneyOperationClient client = Mockito.mock(MoneyOperationClient.class);

        UserBalanceInitResultDto resultDto = new UserBalanceInitResultDto();
        resultDto.setSuccess(true);

        Mockito.when(client.initUserBalance(Matchers.anyObject())).thenReturn(resultDto);

        return client;
    }
//    @Bean
//    public WalletProviderClient walletProviderClient() {
//        WalletProviderClient WalletProviderClient=Mockito.mock(WalletProviderClient.class);
//        return WalletProviderClient;
//    }


    @Bean
    public AdminCalculateOfferAmountClient adminCalculateOfferAmountClient() {
        AdminCalculateOfferAmountClient adminCalculateOfferAmountClient = Mockito.mock(AdminCalculateOfferAmountClient.class);
        CalculateOfferAmountRequestDto calculateOfferAmountRequestDto = new CalculateOfferAmountRequestDto();
        //   calculateOfferAmountRequestDto.setAmount(Matchers.anyDouble());
        // calculateOfferAmountRequestDto.setSettingsId(Matchers.anyString());
        calculateOfferAmountRequestDto.setAmount(1d);
        calculateOfferAmountRequestDto.setSettingsId("1");
        BonusInfo bonusInfo = createDummyBonusInfo();
        Mockito.when(adminCalculateOfferAmountClient.firstTimeTopUpOfferAmount(calculateOfferAmountRequestDto)).thenReturn(bonusInfo);
        return adminCalculateOfferAmountClient;

    }

    private BonusInfo createDummyBonusInfo() {
        BonusInfo bonusInfo = new BonusInfo();
        bonusInfo.setBonus(BigDecimal.valueOf(0.1).setScale(4, BigDecimal.ROUND_HALF_UP));
        bonusInfo.setBonusBetMultiplier(BigDecimal.valueOf(10).setScale(4, BigDecimal.ROUND_HALF_UP));
        return bonusInfo;
    }


    @Bean
    public UserOauthClient userOauthClient () {
        return Mockito.mock(UserOauthClient.class);
    }


    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public UserBankCardClient userBankCardClient() {
        UserBankCardClient userBankCardClient = Mockito.mock(UserBankCardClient.class);
        Mockito.when(userBankCardClient.checkHasCards(Matchers.anyLong())).thenReturn(true);
        return userBankCardClient;
    }

    @Bean
    public UserLocationApiClient userLocationApiClient() {
        UserLocationApiClient userLocationApiClient = Mockito.mock(UserLocationApiClient.class);
        Mockito.when(userLocationApiClient.getLocationByIP(Matchers.anyString())).thenReturn("china");
        return userLocationApiClient;
    }*/


}
