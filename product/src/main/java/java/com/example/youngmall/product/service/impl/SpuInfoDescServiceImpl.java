package java.com.example.youngmall.product.service.impl;

import io.renren.common.utils.Query;
import org.springframework.stereotype.Service;

import java.com.example.youngmall.product.dao.SpuInfoDescDao;
import java.com.example.youngmall.product.entity.SpuInfoDescEntity;
import java.com.example.youngmall.product.service.SpuInfoDescService;
import java.util.Map;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.common.utils.PageUtils;



@Service("spuInfoDescService")
public class SpuInfoDescServiceImpl extends ServiceImpl<SpuInfoDescDao, SpuInfoDescEntity> implements SpuInfoDescService {

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<SpuInfoDescEntity> page = this.page(
                new Query<SpuInfoDescEntity>().getPage(params),
                new QueryWrapper<SpuInfoDescEntity>()
        );

        return new PageUtils(page);
    }

}