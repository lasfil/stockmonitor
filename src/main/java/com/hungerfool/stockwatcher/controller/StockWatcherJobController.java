package com.hungerfool.stockwatcher.controller;

import javax.annotation.Resource;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.hungerfool.stockwatcher.http.HttpAPIService;

@RestController
@RequestMapping(value="/job")
public class StockWatcherJobController {
	@Resource
    private HttpAPIService httpAPIService;

    @RequestMapping("/httpclient")
    public String test() throws Exception {
        String str = httpAPIService.doGet("http://hq.sinajs.cn/list=sh601006,sh600000");
        System.out.println(str);
        return "hello";
    }
}
