package com.atguigu.educenter.controller;

import com.atguigu.common.utils.JwtUtils;
import com.atguigu.educenter.entity.UcenterMember;
import com.atguigu.educenter.service.UcenterMemberService;
import com.atguigu.educenter.utils.ConstantWxUtils;
import com.atguigu.educenter.utils.HttpClientUtils;
import com.atguigu.serviceBase.exceptionhandler.GuliException;
import com.google.gson.Gson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.URLEncoder;
import java.util.HashMap;

@CrossOrigin
@Controller
@RequestMapping("/api/ucenter/wx")

public class WxApiController {

    @Autowired
    private UcenterMemberService memberService;

    //2.获取扫码人信息，添加数据
    @GetMapping("callback")
    public String callback(String code,String state){
        try {
            //1.获取code值，临时票据，类似验证码

            //2.拿着code请求微信固定地址，得到两个值，access_token和openId
            String baseAccessTokenUrl = "https://api.weixin.qq.com/sns/oauth2/access_token" +
                    "?appid=%s" +
                    "&secret=%s" +
                    "&code=%s" +
                    "&grant_type=authorization_code";

            //拼接三个参数，id密钥code值
            String accessTokenUrl = String.format(
                    baseAccessTokenUrl,
                    ConstantWxUtils.WX_OPEN_APP_ID,
                    ConstantWxUtils.WX_OPEN_APP_SECRET,
                    code
            );
            //请求这个拼接好的地址，得到返回两个值access_token和openId
            //使用httpclient发送请求，得到返回结果
            String accessTokenInfo = HttpClientUtils.get(accessTokenUrl);
            //从accessTokenInfo取出两个值access_token和openId
            //字符串转换为map集合，根据key获得值
            Gson gson = new Gson();
            HashMap mapAccessToken = gson.fromJson(accessTokenInfo, HashMap.class);
            String access_token = (String) mapAccessToken.get("access_token");
            String openid = (String) mapAccessToken.get("openid");

            //把扫码人信息添加到数据库
            //判断数据库表里是否存在 相同信息
            UcenterMember member=memberService.getOpenIdMember(openid);
            if (member==null){
                //3.拿着得到的access_token和openId，再去请求微信的固定地址，获取扫码人信息
                String baseUserInfoUrl = "https://api.weixin.qq.com/sns/userinfo" +
                        "?access_token=%s" +
                        "&openid=%s";
                //拼接两个参数
                String userInfoUrl = String.format(baseUserInfoUrl, access_token, openid);
                //发送请求
                String userInfo = HttpClientUtils.get(userInfoUrl);
                //获取返回userInfo字符串的扫码人的信息
                HashMap userInfoMap = gson.fromJson(userInfo, HashMap.class);
                String nickname = (String) userInfoMap.get("nickname");
                String headimgurl = (String) userInfoMap.get("headimgurl");
                member = new UcenterMember();
                member.setOpenid(openid);
                member.setNickname(nickname);
                member.setAvatar(headimgurl);
                memberService.save(member);
            }

            //使用jwt根据member对象生成token对象
            String jwtToken = JwtUtils.getJwtToken(member.getId(), member.getNickname());
            //最后。返回首页面，通过路径传递token字符串
            return "redirect:http://localhost:3000?token="+jwtToken;
        }catch (Exception e){
            throw new GuliException(20001,"登录失败");
        }
    }

    //1.生成微信扫描的二维码
    @GetMapping("login")
    public String getWxCode(){
        // 微信开放平台授权baseUrl,%s相当于问号表示占位符
        String baseUrl = "https://open.weixin.qq.com/connect/qrconnect" +
                "?appid=%s" +
                "&redirect_uri=%s" +
                "&response_type=code" +
                "&scope=snsapi_login" +
                "&state=%s" +
                "#wechat_redirect";

        //对redirect_url进行URLEncode编码
        String redirectUrl=ConstantWxUtils.WX_OPEN_REDIRECT_URL;
        try {
            redirectUrl=URLEncoder.encode(redirectUrl,"utf-8");
        }catch (Exception e){

        }


        //设置%s里面的值
        String url=String.format(
                baseUrl,
                ConstantWxUtils.WX_OPEN_APP_ID,
                ConstantWxUtils.WX_OPEN_REDIRECT_URL,
                "atguigu"
                );
        //请求微信地址
        return "redirect:"+url;
    }
}
