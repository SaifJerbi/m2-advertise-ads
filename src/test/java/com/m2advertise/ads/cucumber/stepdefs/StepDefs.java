package com.m2advertise.ads.cucumber.stepdefs;

import com.m2advertise.ads.M2AdvertiseAdsApp;

import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.ResultActions;

import org.springframework.boot.test.context.SpringBootTest;

@WebAppConfiguration
@SpringBootTest
@ContextConfiguration(classes = M2AdvertiseAdsApp.class)
public abstract class StepDefs {

    protected ResultActions actions;

}
