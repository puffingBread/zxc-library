package org.humor.zxc.library;

import org.humor.zxc.library.commons.util.utils.OkHttpUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;

@SpringBootTest(classes = TestApplication.class)
@RunWith(SpringRunner.class)
public class BaseTest {

    @Resource
    private OkHttpUtils okHttpUtils;

    @Test
    public void testGet() {

        String url = "https://asset-api-fat-alhz.inzm.com/test/snakeflow";
        String response = okHttpUtils.get(url);
        System.out.println(response);

        String postUrl = "https://asset-api-fat-alhz.inzm.com/assetAccount/pointCardGroup/cardGroupStatistics";
        String reqBody = "{\"userId\": 1557579918}";
        String post = okHttpUtils.post(postUrl, reqBody);
        System.out.println(post);
    }
}
