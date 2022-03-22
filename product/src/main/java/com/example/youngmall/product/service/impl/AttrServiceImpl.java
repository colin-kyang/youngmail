package com.example.youngmall.product.service.impl;

import io.renren.common.utils.Query;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;
import java.util.Map;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.common.utils.PageUtils;

import com.example.youngmall.product.dao.AttrDao;
import com.example.youngmall.product.entity.AttrEntity;
import com.example.youngmall.product.service.AttrService;


@Service("attrService")
public class AttrServiceImpl extends ServiceImpl<AttrDao, AttrEntity> implements AttrService {

    @Override
    public PageUtils queryPage(Map<String, Object> params, Long catlogId) {
        QueryWrapper<AttrEntity> wrapper = new QueryWrapper<>();
        String key = (String) params.getOrDefault("key",null);
        //如果catlogId 为0,则为检索全部
        if(catlogId != 0)
        {
            wrapper.eq("catelog_id",catlogId);
        }
        if(!StringUtils.isEmpty(key))
        {
            wrapper.and((obj)->{obj.eq("attr_name",key).or().like("value_select",key);});
        }
        IPage<AttrEntity> page = this.page(
                new Query<AttrEntity>().getPage(params),
                wrapper
        );

        return new PageUtils(page);
    }

}