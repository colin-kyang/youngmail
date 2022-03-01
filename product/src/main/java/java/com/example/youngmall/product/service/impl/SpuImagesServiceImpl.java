package java.com.example.youngmall.product.service.impl;

import io.renren.common.utils.Query;
import org.springframework.stereotype.Service;

import java.com.example.youngmall.product.dao.SpuImagesDao;
import java.com.example.youngmall.product.entity.SpuImagesEntity;
import java.com.example.youngmall.product.service.SpuImagesService;
import java.util.Map;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.common.utils.PageUtils;


@Service("spuImagesService")
public class SpuImagesServiceImpl extends ServiceImpl<SpuImagesDao, SpuImagesEntity> implements SpuImagesService {

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<SpuImagesEntity> page = this.page(
                new Query<SpuImagesEntity>().getPage(params),
                new QueryWrapper<SpuImagesEntity>()
        );

        return new PageUtils(page);
    }

}