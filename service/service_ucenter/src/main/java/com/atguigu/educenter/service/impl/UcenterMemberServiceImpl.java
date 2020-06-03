package com.atguigu.educenter.service.impl;

import com.atguigu.common.utils.JwtUtils;
import com.atguigu.common.utils.MD5;
import com.atguigu.educenter.entity.UcenterMember;
import com.atguigu.educenter.entity.vo.RegisterVo;
import com.atguigu.educenter.mapper.UcenterMemberMapper;
import com.atguigu.educenter.service.UcenterMemberService;
import com.atguigu.serviceBase.exceptionhandler.GuliException;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 会员表 服务实现类
 * </p>
 *
 * @author szy
 * @since 2020-06-03
 */
@Service
public class UcenterMemberServiceImpl extends ServiceImpl<UcenterMemberMapper, UcenterMember> implements UcenterMemberService {
    @Autowired
    private RedisTemplate<String,String> redisTemplate;
    //登录
    @Override
    public String login(UcenterMember member) {
        //获取登录的手机号和密码
        String mobile = member.getMobile();
        String password = member.getPassword();

        //判断手机号密码非空
        if (StringUtils.isEmpty(mobile)|| StringUtils.isEmpty(password)){
            throw new GuliException(20001,"登录失败");
        }

        //判断手机号是否正确
        QueryWrapper<UcenterMember> wrapper=new QueryWrapper<>();
        wrapper.eq("mobile",mobile);
        UcenterMember mobileMenber=baseMapper.selectOne(wrapper);
        //判断是否为空
        if (mobileMenber==null){
            throw new GuliException(20001,"手机号不存在");
        }

        //判断密码
        //输入的密码加密再和数据库的密码比较
        if (!MD5.encrypt(password).equals(mobileMenber.getPassword())){
            throw new GuliException(20001,"密码错误");
        }

        //判断用户是否被禁用
        if (mobileMenber.getIsDisabled()){
            throw new GuliException(20001,"用户被禁用");
        }

        //登录成功
        //生成token
        String jwtToken = JwtUtils.getJwtToken(mobileMenber.getId(), mobileMenber.getNickname());
        return jwtToken;
    }
    //注册
    @Override
    public void register(RegisterVo registerVo) {
        //获取注册的数据
        String code = registerVo.getCode();//验证码
        String mobile = registerVo.getMobile();
        String nickname = registerVo.getNickname();
        String password = registerVo.getPassword();

        //非空非空
        if (StringUtils.isEmpty(code)|| StringUtils.isEmpty(mobile)|| StringUtils.isEmpty(nickname)|| StringUtils.isEmpty(password)){
            throw new GuliException(20001,"信息填写不完全");
        }

        //判断手机验证码
        //获取redis里的验证码
        String redisCode = redisTemplate.opsForValue().get(mobile);
        if (!code.equals(redisCode)){
            throw new GuliException(20001,"验证码错误");
        }

        //手机号不能重复判断
        QueryWrapper<UcenterMember> wrapper=new QueryWrapper<>();
        wrapper.eq("mobile",mobile);
        Integer count = baseMapper.selectCount(wrapper);
        if (count>0){
            throw new GuliException(20001,"此手机号已被注册");
        }

        //数据添加到数据库中
        UcenterMember member = new UcenterMember();
        member.setMobile(mobile);
        member.setNickname(nickname);
        member.setPassword(MD5.encrypt(password));
        member.setIsDisabled(false);
        member.setAvatar("https://edu-20200515.oss-cn-hangzhou.aliyuncs.com/2020/05/18/21876fe537174b309f07be331b39becbfile.png");
        baseMapper.insert(member);
    }
}
